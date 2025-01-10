
package com.gestion.mercantil.controller;

import com.gestion.mercantil.dto.ApiResponse;
import com.gestion.mercantil.entity.Department;
import com.gestion.mercantil.service.DepartmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class DepartmentController {
    private final DepartmentService departmentService;

    @GetMapping("/departments")
    public ResponseEntity<ApiResponse<List<Department>>> getAllDepartments() {
        List<Department> departments = departmentService.findAll();
        return ResponseEntity.ok(new ApiResponse<>(true, "Departamentos consultados exitosamente.", departments));
    }
}
