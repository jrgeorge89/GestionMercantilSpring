package com.gestion.mercantil.service;

import com.gestion.mercantil.entity.Businessman;
import com.gestion.mercantil.entity.Department;
import com.gestion.mercantil.entity.Municipality;
import com.gestion.mercantil.repository.BusinessmanRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

@Service
@RequiredArgsConstructor
public class BusinessmanService {

    private final EntityManager entityManager;
    private final BusinessmanRepository businessmanRepository;
    private final DataSource dataSource;

    @Transactional(readOnly = true)
    public Page<Businessman> findAll(String name, Integer municipalitieId, LocalDate registrationDate, String status, PageRequest pageRequest) {
        List<Businessman> businessmen = new ArrayList<>();
        int totalRecords = 0;
        String query = "{ call ? := test.businessman_pkg.get_businessman(?, ?, ?, ?, ?, ?, ?) }";

        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {

            // Registrar el cursor de salida y los parámetros de entrada
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setString(2, name);
            stmt.setObject(3, municipalitieId);
            stmt.setObject(4, registrationDate != null ? Date.valueOf(registrationDate) : null);
            stmt.setString(5, status);
            stmt.setInt(6, pageRequest.getPageNumber() + 1);
            stmt.setInt(7, pageRequest.getPageSize());

            // Registrar el parámetro de salida para el total de registros
            stmt.registerOutParameter(8, OracleTypes.INTEGER);

            // Ejecutar la llamada
            stmt.execute();

            // Obtener el cursor de salida y mapear los resultados
            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                boolean firstRecord = true;
                while (rs.next()) {
                    if (firstRecord) {
                        totalRecords = rs.getInt("total_records");
                        firstRecord = false;
                    }
                    businessmen.add(mapToBusinessmanResult(rs));
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar todos los Comerciantes: " + e.getMessage(), e);
        }

        return new PageImpl<>(businessmen, pageRequest, totalRecords);
    }
    

    @Transactional(readOnly = true)
    public Businessman findById(Integer id) {
        String query = "{ call ? := test.businessman_pkg.get_businessman_by_id(?) }";

        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {

            // Registrar el cursor de salida y el parámetro de entrada
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setInt(2, id);

            // Ejecutar la llamada
            stmt.execute();

            // Obtener el cursor de salida y mapear los resultados
            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                if (rs.next()) {
                    return mapToBusinessmanResult(rs);
                } else {
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al consultar al Comerciante por ID: " + e.getMessage(), e);
        }
    }
    
    private Businessman mapToBusinessmanResult(ResultSet rs) throws SQLException {
        Businessman businessman = new Businessman();
        businessman.setBusinessman_id(rs.getInt("businessman_id"));
        businessman.setNombreRazonSocial(rs.getString("nombre_razon_social"));

        businessman.setDepartment(new Department());
        businessman.getDepartment().setId(rs.getInt("department_id"));
        businessman.getDepartment().setName(rs.getString("departamento"));

        businessman.setMunicipality(new Municipality());
        businessman.getMunicipality().setId(rs.getInt("municipalitie_id"));
        businessman.getMunicipality().setName(rs.getString("municipio"));

        businessman.setTelefono(rs.getString("telefono"));
        businessman.setCorreoElectronico(rs.getString("correo_electronico"));
        businessman.setEstado(rs.getString("estado"));
        businessman.setTotalActivos(rs.getBigDecimal("total_activos"));
        businessman.setCantidadEmpleados(rs.getInt("cantidad_empleados"));

        Date sqlDate = rs.getDate("fecha_registro");
        if (sqlDate != null) {
            LocalDate localDate = sqlDate.toLocalDate();
            businessman.setFechaRegistro(localDate);
        }

        try {
            businessman.setCantidadEstablecimientos(rs.getInt("cantidad_establecimientos"));
        } catch (SQLException e) {
            businessman.setCantidadEstablecimientos(null);
        }
        try {
            businessman.setFechaActualizacion(rs.getDate("fecha_actualizacion"));
        } catch (SQLException e) {
            businessman.setFechaActualizacion(null);
        }
        try {
            businessman.setUsuario(rs.getString("usuario"));
        } catch (SQLException e) {
            businessman.setUsuario(null);
        }

        return businessman;
    }
    

    @Transactional
    public Businessman create(Businessman businessman) {
        // Convertir la fecha de LocalDate a java.sql.Date
        LocalDate localDate = businessman.getFechaRegistro();
        Date sqlDate = Date.valueOf(localDate);

        // Depuración para imprimir el valor ingresado y el valor formateado
//        System.out.println("Fecha ingresada: " + localDate);
//        System.out.println("Fecha SQL: " + sqlDate);

        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("test.businessman_pkg.insert_businessman")
                .registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_department_id", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_municipalitie_id", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_phone", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_registration_date", Date.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_status", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_user", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_error_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_error_message", String.class, ParameterMode.OUT)
                .setParameter("p_name", businessman.getNombreRazonSocial())
                .setParameter("p_department_id", businessman.getDepartment().getId())
                .setParameter("p_municipalitie_id", businessman.getMunicipality().getId())
                .setParameter("p_phone", businessman.getTelefono())
                .setParameter("p_email", businessman.getCorreoElectronico())
                .setParameter("p_registration_date", sqlDate)
                .setParameter("p_status", businessman.getEstado())
                .setParameter("p_user", businessman.getUsuario());

        query.execute();

        int errorCode = (int) query.getOutputParameterValue("p_error_code");
        String errorMessage = (String) query.getOutputParameterValue("p_error_message");

        if (errorCode != 0) {
            throw new RuntimeException("Error al crear Comerciante: " + errorMessage);
        }

        return businessman;
    }


    @Transactional
    public Businessman update(Integer id, Businessman businessman) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("test.businessman_pkg.update_businessman")
                .registerStoredProcedureParameter("p_businessman_id", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_department_id", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_municipalitie_id", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_phone", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_email", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_registration_date", Date.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_status", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_user", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_error_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_error_message", String.class, ParameterMode.OUT)
                .setParameter("p_businessman_id", id)
                .setParameter("p_name", businessman.getNombreRazonSocial())
                .setParameter("p_department_id", businessman.getDepartment().getId())
                .setParameter("p_municipalitie_id", businessman.getMunicipality().getId())
                .setParameter("p_phone", businessman.getTelefono())
                .setParameter("p_email", businessman.getCorreoElectronico())
                .setParameter("p_registration_date", businessman.getFechaRegistro())
                .setParameter("p_status", businessman.getEstado())
                .setParameter("p_user", businessman.getUsuario());

        query.execute();

        int errorCode = (int) query.getOutputParameterValue("p_error_code");
        String errorMessage = (String) query.getOutputParameterValue("p_error_message");

        if (errorCode != 0) {
            throw new RuntimeException("Error al actualizar Comerciante: " + errorMessage);
        }

        return businessman;
    }
    

    @Transactional
    public void delete(Integer id) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("test.businessman_pkg.delete_businessman")
                .registerStoredProcedureParameter("p_businessman_id", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_error_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_error_message", String.class, ParameterMode.OUT)
                .setParameter("p_businessman_id", id);

        query.execute();

        int errorCode = (int) query.getOutputParameterValue("p_error_code");
        String errorMessage = (String) query.getOutputParameterValue("p_error_message");

        if (errorCode != 0) {
            throw new RuntimeException("Error al eliminar Comerciante: " + errorMessage);
        }
    }
    

    @Transactional
    public Businessman updateStatus(Integer id, String status) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("test.businessman_pkg.update_businessman_status")
                .registerStoredProcedureParameter("p_businessman_id", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_status", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_error_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_error_message", String.class, ParameterMode.OUT)
                .setParameter("p_businessman_id", id)
                .setParameter("p_status", status);

        query.execute();

        int errorCode = (int) query.getOutputParameterValue("p_error_code");
        String errorMessage = (String) query.getOutputParameterValue("p_error_message");

        if (errorCode != 0) {
            throw new RuntimeException("Error al actualizar el estado del Comerciante: " + errorMessage);
        }

        return findById(id);
    }

}
