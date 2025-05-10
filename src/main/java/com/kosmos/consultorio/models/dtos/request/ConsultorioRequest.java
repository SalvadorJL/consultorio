package com.kosmos.consultorio.models.dtos.request;

public record ConsultorioRequest(
        Long id,
        String nombre,
        String direccion,
        String telefono,
        String tipoConsultorio,
        String especialidad,
        String nombreMedico
) {}