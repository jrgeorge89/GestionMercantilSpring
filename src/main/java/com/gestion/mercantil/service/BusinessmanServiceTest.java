package com.gestion.mercantil.service;

import com.gestion.mercantil.entity.Businessman;
import com.gestion.mercantil.entity.Department;
import com.gestion.mercantil.entity.Municipality;
import com.gestion.mercantil.exception.BusinessmanException;
import com.gestion.mercantil.repository.BusinessmanRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class BusinessmanServiceTest {

    @Mock
    private BusinessmanRepository businessmanRepository;

    @InjectMocks
    private BusinessmanService businessmanService;

    private Businessman businessman;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        businessman = new Businessman();
        businessman.setNombreRazonSocial("Nuevo Comerciante");
        businessman.setDepartment(new Department());
        businessman.setMunicipality(new Municipality());
    }

    @Test
    public void testCreateBusinessman_Success() {
        when(businessmanRepository.save(any(Businessman.class))).thenReturn(businessman);
        Businessman createdBusinessman = businessmanService.create(businessman);
        assertNotNull(createdBusinessman);
        assertEquals(businessman.getNombreRazonSocial(), createdBusinessman.getNombreRazonSocial());
        verify(businessmanRepository, times(1)).save(businessman);
    }

    @Test
    public void testCreateBusinessman_DataIntegrityViolation() {
        when(businessmanRepository.save(any(Businessman.class))).thenThrow(new DataIntegrityViolationException("Entrada duplicada"));
        BusinessmanException exception = assertThrows(BusinessmanException.class, () -> {
            businessmanService.create(businessman);
        });
        assertEquals("Error al crear comercial: entrada duplicada", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getHttpStatus());
    }

    @Test
    public void testGetBusinessmanById_Success() {
        when(businessmanRepository.findById(1)).thenReturn(Optional.of(businessman));
        Businessman retrievedBusinessman = businessmanService.findById(1);
        assertNotNull(retrievedBusinessman);
        assertEquals(businessman.getNombreRazonSocial(), retrievedBusinessman.getNombreRazonSocial());
    }

    @Test
    public void testGetBusinessmanById_NotFound() {
        when(businessmanRepository.findById(1)).thenReturn(Optional.empty());
        Businessman retrievedBusinessman = businessmanService.findById(1);
        assertNull(retrievedBusinessman);
    }

    @Test
    public void testUpdateBusinessman_Success() {
        when(businessmanRepository.findById(1)).thenReturn(Optional.of(businessman));
        when(businessmanRepository.save(any(Businessman.class))).thenReturn(businessman);
        Businessman updatedBusinessman = businessmanService.update(1, businessman);
        assertNotNull(updatedBusinessman);
        assertEquals(businessman.getNombreRazonSocial(), updatedBusinessman.getNombreRazonSocial());
        verify(businessmanRepository, times(1)).save(businessman);
    }
}
