package com.gestion.mercantil.repository;

import com.gestion.mercantil.entity.Establishment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EstablishmentRepository extends JpaRepository<Establishment, Integer> {
    
    @Query("SELECT e FROM Establishment e WHERE e.businessman.businessman_id = :businessman_id")
    List<Establishment> findByBusinessmanId(@Param("businessman_id") Integer businessmanId);
}
