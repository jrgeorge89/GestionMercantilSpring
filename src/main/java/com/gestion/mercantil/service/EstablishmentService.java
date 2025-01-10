package com.gestion.mercantil.service;

import com.gestion.mercantil.entity.Establishment;
import com.gestion.mercantil.repository.EstablishmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import javax.sql.DataSource;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleTypes;

@Service
@RequiredArgsConstructor
public class EstablishmentService {

    private final EntityManager entityManager;
    private final EstablishmentRepository establishmentRepository;

    @Autowired
    private DataSource dataSource;

    @Transactional(readOnly = true)
    public List<Establishment> findByBusinessmanId(Integer businessmanId) {
        List<Establishment> establishments = new ArrayList<>();

        // Crear la consulta stored procedure con cursor
        String query = "{ call ? := test.establishment_pkg.get_estab_by_businessman(?) }";

        try (Connection connection = dataSource.getConnection();
             CallableStatement stmt = connection.prepareCall(query)) {

            // Registrar el cursor de salida y el parámetro de entrada
            stmt.registerOutParameter(1, OracleTypes.CURSOR);
            stmt.setInt(2, businessmanId);

            // Ejecutar la llamada
            stmt.execute();

            // Obtener el cursor de salida
            try (ResultSet rs = (ResultSet) stmt.getObject(1)) {
                while (rs.next()) {
                    Establishment establishment = mapToEstablishmentResult(rs);
                    establishments.add(establishment);
                }
            }
        } catch (SQLException e) {
            String errorMessage = "Error ejecutando el procedimiento almacenado: " + e.getMessage();
            throw new RuntimeException(errorMessage, e);
        }

        return establishments;
    }

    private Establishment mapToEstablishmentResult(ResultSet rs) throws SQLException {
        Establishment establishment = new Establishment();
        establishment.setEstablishment_id(rs.getInt("establishment_id"));
        establishment.setNombre(rs.getString("name"));
        establishment.setIngresos(rs.getDouble("revenue"));
        establishment.setNumeroEmpleados(rs.getInt("number_of_employees"));
        establishment.setUpdateDate(rs.getDate("update_date"));
        establishment.setCreatedBy(rs.getString("created_by"));
        return establishment;
    }

    @Transactional
    public Establishment create(Establishment establishment) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("test.establishment_pkg.insert_establishment")
                .registerStoredProcedureParameter("p_businessman_id", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_revenue", Double.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_number_of_employees", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_created_by", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_error_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_error_message", String.class, ParameterMode.OUT)
                .setParameter("p_businessman_id", establishment.getBusinessman().getBusinessman_id())
                .setParameter("p_name", establishment.getNombre())
                .setParameter("p_revenue", establishment.getIngresos())
                .setParameter("p_number_of_employees", establishment.getNumeroEmpleados())
                .setParameter("p_created_by", establishment.getCreatedBy());

        query.execute();

        int errorCode = (int) query.getOutputParameterValue("p_error_code");
        String errorMessage = (String) query.getOutputParameterValue("p_error_message");

        if (errorCode != 0) {
            throw new RuntimeException("Error creando caso: " + errorMessage);
        }

        return establishment;
    }

    @Transactional
    public Establishment update(Integer id, Establishment establishment) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("test.establishment_pkg.update_establishment")
                .registerStoredProcedureParameter("p_establishment_id", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_name", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_revenue", Double.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_number_of_employees", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_created_by", String.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_error_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_error_message", String.class, ParameterMode.OUT)
                .setParameter("p_establishment_id", id)
                .setParameter("p_name", establishment.getNombre())
                .setParameter("p_revenue", establishment.getIngresos())
                .setParameter("p_number_of_employees", establishment.getNumeroEmpleados())
                .setParameter("p_created_by", establishment.getCreatedBy());

        query.execute();

        int errorCode = (int) query.getOutputParameterValue("p_error_code");
        String errorMessage = (String) query.getOutputParameterValue("p_error_message");

        if (errorCode != 0) {
            throw new RuntimeException("Error actualizando caso: " + errorMessage);
        }

        return establishment;
    }

    @Transactional
    public void delete(Integer id) {
        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("test.establishment_pkg.delete_establishment")
                .registerStoredProcedureParameter("p_establishment_id", Integer.class, ParameterMode.IN)
                .registerStoredProcedureParameter("p_error_code", Integer.class, ParameterMode.OUT)
                .registerStoredProcedureParameter("p_error_message", String.class, ParameterMode.OUT)
                .setParameter("p_establishment_id", id);

        query.execute();

        int errorCode = (int) query.getOutputParameterValue("p_error_code");
        String errorMessage = (String) query.getOutputParameterValue("p_error_message");

        if (errorCode != 0) {
            throw new RuntimeException("Error eliminando caso: " + errorMessage);
        }
    }
}
