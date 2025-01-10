package com.gestion.mercantil.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.Date;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "establishment", schema = "test")
public class Establishment {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "establishment_seq")
    @SequenceGenerator(name = "establishment_seq", sequenceName = "test.establishment_seq", allocationSize = 1)
    private Integer establishment_id;
    
    @ManyToOne
    @JoinColumn(name = "businessman_id")
    @NotNull(message = "El campo Businessman es obligatorio.")
    private Businessman businessman;

    @Column(name = "name")
    @NotBlank(message = "El campo Nombre del Establecimiento es obligatorio.")
    private String nombre;

    @Column(name = "revenue")
    @NotNull(message = "El campo Ingresos es obligatorio.")
    @Positive(message = "El campo Ingresos debe ser un número positivo.")
    private Double ingresos;

    @Column(name = "number_of_employees")
    @NotNull(message = "El campo Número de Empleados es obligatorio.")
    @Positive(message = "El campo Número de Empleados debe ser un número positivo.")
    private Integer numeroEmpleados;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "created_by")
    private String createdBy;
}
