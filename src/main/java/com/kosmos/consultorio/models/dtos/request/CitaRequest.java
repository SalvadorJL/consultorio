package com.kosmos.consultorio.models.dtos.request;

import com.kosmos.consultorio.models.entity.Consultorio;
import com.kosmos.consultorio.models.entity.Doctor;

import java.time.LocalDateTime;

public record CitaRequest(
        Long id,
        Consultorio consultorio,
        Doctor doctor,
        LocalDateTime fechaCita,
        String nombrePaciente
) {}
