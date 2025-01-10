
package com.gestion.mercantil.repository;

import com.gestion.mercantil.entity.Businessman;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface BusinessmanRepository extends JpaRepository<Businessman, Integer> {

    @Query("SELECT b FROM Businessman b WHERE " +
           "(:name IS NULL OR b.nombreRazonSocial LIKE %:name%) AND " +
           "(:municipalitieId IS NULL OR b.municipality.id = :municipalitieId) AND " +
           "(:registrationDate IS NULL OR b.fechaRegistro = :registrationDate) AND " +
           "(:status IS NULL OR b.estado = :status)")
    Page<Businessman> findByFilters(@Param("name") String name,
                                    @Param("municipalitieId") Integer municipalitieId,
                                    @Param("registrationDate") Date registrationDate,
                                    @Param("status") String status,
                                    PageRequest pageRequest);
}
