package com.kosmos.consultorio.models.dtos.request;

import java.time.LocalDateTime;

public record UpdateCitaRequest(
        Long doctorId,
        Long consultorioId,
        LocalDateTime fechaCita,
        String nombrePaciente
) {}
