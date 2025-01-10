
package com.gestion.mercantil.controller;

import com.gestion.mercantil.dto.ApiResponse;
import com.gestion.mercantil.entity.Establishment;
import com.gestion.mercantil.service.EstablishmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/establishment")
@RequiredArgsConstructor
public class EstablishmentController {

    private final EstablishmentService establishmentService;

    @GetMapping("/businessman/{businessmanId}")
    public ResponseEntity<ApiResponse<List<Establishment>>> getEstablishmentsByBusinessmanId(@PathVariable Integer businessmanId) {
        List<Establishment> establishments = establishmentService.findByBusinessmanId(businessmanId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Establecimientos consultados exitosamente.", establishments));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Establishment>> createEstablishment(@Valid @RequestBody Establishment establishment) {
        Establishment createdEstablishment = establishmentService.create(establishment);
        return ResponseEntity.ok(new ApiResponse<>(true, "Establecimiento creado exitosamente.", createdEstablishment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Establishment>> updateEstablishment(@PathVariable Integer id, @Valid @RequestBody Establishment establishment) {
        Establishment updatedEstablishment = establishmentService.update(id, establishment);
        return ResponseEntity.ok(new ApiResponse<>(true, "Establecimiento actualizado exitosamente.", updatedEstablishment));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteEstablishment(@PathVariable Integer id) {
        establishmentService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Establecimiento eliminado exitosamente.", null));
    }
}
