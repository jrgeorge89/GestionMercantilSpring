package com.gestion.mercantil.controller;

import com.gestion.mercantil.dto.ApiResponse;
import com.gestion.mercantil.entity.Businessman;
import com.gestion.mercantil.service.BusinessmanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/api/businessman")
@RequiredArgsConstructor
public class BusinessmanController {

    private final BusinessmanService businessmanService;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<Businessman>>> getAllBusinessmen(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) Integer municipalitieId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date registrationDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Businessman> businessmen = businessmanService.findAll(name, municipalitieId, registrationDate, status, pageRequest);

        return ResponseEntity.ok(new ApiResponse<>(true, "Comerciales consultados exitosamente.", businessmen));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Businessman>> getBusinessmanById(@PathVariable Integer id) {
        Businessman businessman = businessmanService.findById(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Comerciales consultados exitosamente.", businessman));
    }

    @PostMapping 
    public ResponseEntity<ApiResponse<Businessman>> createBusinessman(@RequestBody @Valid Businessman businessman) {
        System.out.println("Fecha antes de enviar: " + businessman.getFechaRegistro());
        Businessman createdBusinessman = businessmanService.create(businessman);
        System.out.println("Fecha al recibir respuesta: " + createdBusinessman.getFechaRegistro());
        return ResponseEntity.ok(new ApiResponse<>(true, "Comercial creado exitosamente.", createdBusinessman));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Businessman>> updateBusinessman(@PathVariable Integer id, @RequestBody @Valid Businessman businessman) {
        Businessman updatedBusinessman = businessmanService.update(id, businessman);
        return ResponseEntity.ok(new ApiResponse<>(true, "Comercial actualizado exitosamente.", updatedBusinessman));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteBusinessman(@PathVariable Integer id) {
        businessmanService.delete(id);
        return ResponseEntity.ok(new ApiResponse<>(true, "Comercial Eliminado exitosamente.", null));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<Businessman>> updateBusinessmanStatus(@PathVariable Integer id, @RequestParam String status) {
        Businessman updatedBusinessman = businessmanService.updateStatus(id, status);
        return ResponseEntity.ok(new ApiResponse<>(true, "Estado del Comercial actualizado exitosamente.", updatedBusinessman));
    }
}
