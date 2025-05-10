package com.kosmos.consultorio.repository;

import com.kosmos.consultorio.models.entity.Cita;
import com.kosmos.consultorio.models.entity.Consultorio;
import com.kosmos.consultorio.models.entity.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Long> {
    boolean existsByConsultorioAndFechaCitaBetween(
            Consultorio consultorio,
            LocalDateTime fechaCitaInicio,
            LocalDateTime fechaCitaFin
    );

    boolean existsByDoctorAndFechaCitaBetween(
            Doctor doctor,
            LocalDateTime fechaCitaInicio,
            LocalDateTime fechaCitaFin
    );

    List<Cita> findByNombrePacienteAndFechaCitaBetween(
            String nombrePaciente,
            LocalDateTime fechaCitaInicio,
            LocalDateTime fechaCitaFin
    );

    int countByDoctorAndFechaCitaBetween(
            Doctor doctor,
            LocalDateTime fechaCitaInicio,
            LocalDateTime fechaCitaFin
    );

    List<Cita> findByFechaCitaBetween(
            LocalDateTime fechaCitaInicio,
            LocalDateTime fechaCitaFin
    );

    List<Cita> findByDoctorAndFechaCitaBetween(
            Doctor doctor,
            LocalDateTime fechaCitaInicio,
            LocalDateTime fechaCitaFin
    );

    List<Cita> findByConsultorioAndFechaCitaBetween(
            Consultorio consultorio,
            LocalDateTime fechaCitaInicio,
            LocalDateTime fechaCitaFin
    );
}
