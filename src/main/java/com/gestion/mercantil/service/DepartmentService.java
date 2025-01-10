
package com.gestion.mercantil.service;

import com.gestion.mercantil.entity.Department;
import com.gestion.mercantil.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    @Cacheable("departments")
    public List<Department> findAll() {
        return departmentRepository.findAll();
    }
}
