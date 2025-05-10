package com.kosmos.consultorio.models.dtos.response;

import lombok.Builder;

@Builder
public record ConsultorioResponse(
        Long id,
        Integer numConsultorio,
        Integer piso
) {}
