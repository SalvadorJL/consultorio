package com.kosmos.consultorio.models.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "consultorios")
public class Consultorio {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "num_consultorio")
    private Integer numConsultorio;
    private Integer piso;

    @OneToMany(mappedBy = "consultorio")
    private List<Cita> citas;
}
