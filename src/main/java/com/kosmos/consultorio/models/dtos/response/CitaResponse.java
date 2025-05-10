package com.kosmos.consultorio.models.dtos.response;

import com.kosmos.consultorio.models.entity.Consultorio;
import com.kosmos.consultorio.models.entity.Doctor;

import java.time.LocalDateTime;

public record CitaResponse (
        Long id,
        Long doctor,
        Long consultorio,
        LocalDateTime fechaCita,
        String nombrePaciente
) {}
