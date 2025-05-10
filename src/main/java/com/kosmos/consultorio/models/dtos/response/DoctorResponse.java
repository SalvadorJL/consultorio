package com.kosmos.consultorio.models.dtos.response;

public record DoctorResponse (
        Long id,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String especialidad
) {}
