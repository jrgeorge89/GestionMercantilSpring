
package com.gestion.mercantil.service;

import com.gestion.mercantil.entity.Municipality;
import com.gestion.mercantil.repository.MunicipalityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MunicipalityService {
    private final MunicipalityRepository municipalityRepository;

    @Cacheable(value = "municipalities", key = "#departmentId")
    public List<Municipality> findByDepartmentId(Integer departmentId) {
        return municipalityRepository.findByDepartmentId(departmentId);
    }
}
