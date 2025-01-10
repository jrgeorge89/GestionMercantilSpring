package com.gestion.mercantil.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.Date;
import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "businessman", schema = "test")
public class Businessman {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer businessman_id;
    
    @Column(name = "nombre_razon_social")
    @NotBlank(message = "El campo Nombre o razón social es obligatorio.")
    private String nombreRazonSocial;
    
    @ManyToOne
    @JoinColumn(name = "department_id")
    @NotNull(message = "El campo Departamento es obligatorio.")
    private Department department;
    
    @ManyToOne
    @JoinColumn(name = "municipalitie_id")
    @NotNull(message = "El campo Municipio es obligatorio.")
    private Municipality municipality;
    
    @Column(name = "telefono")
    @Pattern(regexp = "^[0-9]*$", message = "El campo Teléfono solo debe contener números.")
    private String telefono; // Opcional
    
    @Column(name = "correo_electronico")
    @Email(message = "El campo Correo Electrónico debe ser una dirección de correo válida.")
    private String correoElectronico; // Opcional
    
    @Column(name = "fecha_registro")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "El campo Fecha de Registro es obligatorio.")
    @PastOrPresent(message = "El campo Fecha de Registro debe ser una fecha pasada o presente.")
    private Date fechaRegistro;
    
    @Column(name = "estado")
    @NotBlank(message = "El campo Estado es obligatorio.")
    private String estado;
    
    @Column(name = "fecha_actualizacion")
    private Date fechaActualizacion;
    
    @Column(name = "usuario")
    private String usuario;
    
    @Column(name = "total_activos")
    private BigDecimal totalActivos;
    
    @Column(name = "cantidad_empleados")
    private Integer cantidadEmpleados;
}
