package com.kosmos.consultorio.service.interfaces;

import com.kosmos.consultorio.models.dtos.request.CitaRequest;
import com.kosmos.consultorio.models.dtos.response.CitaResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface CitaService {
    public CitaResponse agendarCita(CitaRequest citaRequest);
    public List<CitaResponse> obtenerPorFecha(LocalDate fecha);
    public List<CitaResponse> obtenerPorDoctorAndFecha(Long idPaciente, LocalDate fecha);
    public List<CitaResponse> obtenerPorConsultorioAndFecha(Long idConsultorio, LocalDate fecha);
    public boolean cancelarCita(Long idCita);
    public CitaResponse editarCita(Long idCita, CitaRequest citaRequest);
}
