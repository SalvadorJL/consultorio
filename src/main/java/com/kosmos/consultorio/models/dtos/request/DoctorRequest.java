package com.kosmos.consultorio.models.dtos.request;

public record DoctorRequest(
        Long id,
        String nombre,
        String apellidoPaterno,
        String apellidoMaterno,
        String especialidad
) {}
