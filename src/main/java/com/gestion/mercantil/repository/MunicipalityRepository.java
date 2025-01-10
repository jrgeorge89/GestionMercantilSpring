
package com.gestion.mercantil.repository;

import com.gestion.mercantil.entity.Municipality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MunicipalityRepository extends JpaRepository<Municipality, Integer> {
    List<Municipality> findByDepartmentId(Integer departmentId);
}
