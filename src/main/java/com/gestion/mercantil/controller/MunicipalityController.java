
package com.gestion.mercantil.controller;

import com.gestion.mercantil.dto.ApiResponse;
import com.gestion.mercantil.entity.Municipality;
import com.gestion.mercantil.service.MunicipalityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MunicipalityController {
    private final MunicipalityService municipalityService;

    @GetMapping("/departments/{departmentId}/municipalities")
    public ResponseEntity<ApiResponse<List<Municipality>>> getMunicipalitiesByDepartment(@PathVariable Integer departmentId) {
        List<Municipality> municipalities = municipalityService.findByDepartmentId(departmentId);
        return ResponseEntity.ok(new ApiResponse<>(true, "Municipios consultados exitosamente.", municipalities));
    }
}
