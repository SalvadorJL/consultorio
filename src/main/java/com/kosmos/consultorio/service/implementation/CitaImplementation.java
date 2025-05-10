package com.kosmos.consultorio.service.implementation;

import com.kosmos.consultorio.models.dtos.request.CitaRequest;
import com.kosmos.consultorio.models.dtos.response.CitaResponse;
import com.kosmos.consultorio.models.entity.Cita;
import com.kosmos.consultorio.repository.CitaRepository;
import com.kosmos.consultorio.repository.ConsultorioRepository;
import com.kosmos.consultorio.repository.DoctorRepository;
import com.kosmos.consultorio.service.interfaces.CitaService;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Slf4j
@Service
public class CitaImplementation implements CitaService {
    private final CitaRepository citaRepository;
    private final DoctorRepository doctorRepository;
    private final ConsultorioRepository consultorioRepository;

    public CitaImplementation(
            CitaRepository citaRepository,
            DoctorRepository doctorRepository,
            ConsultorioRepository consultorioRepository
    ) {
        this.citaRepository = citaRepository;
        this.doctorRepository = doctorRepository;
        this.consultorioRepository = consultorioRepository;
    }

    @Override
    @Transactional
    public CitaResponse agendarCita(CitaRequest citaRequest) {
        validacioncitas(citaRequest, null);
        var cita = citaRepository.save(mapRequestToCita(citaRequest));

        return mapToCitasResponse(cita);
    }

    @Override
    public List<CitaResponse> obtenerPorFecha(LocalDate fecha) {
        var citas = citaRepository.findByFechaCitaBetween(fecha.atStartOfDay(), fecha.atTime(LocalTime.MAX));

        return citas.stream()
                .map(this::mapToCitasResponse)
                .toList();
    }

    @Override
    public List<CitaResponse> obtenerPorDoctorAndFecha(Long idPaciente, LocalDate fecha) {
        var doctor = doctorRepository.findById(idPaciente)
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        var citas = citaRepository.findByDoctorAndFechaCitaBetween(doctor, fecha.atStartOfDay(), fecha.atTime(LocalTime.MAX));

        return citas.stream()
                .map(this::mapToCitasResponse)
                .toList();
    }

    @Override
    public List<CitaResponse> obtenerPorConsultorioAndFecha(Long idConsultorio, LocalDate fecha) {
        var consultorio = consultorioRepository.findById(idConsultorio)
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado"));

        var citas = citaRepository.findByConsultorioAndFechaCitaBetween(consultorio, fecha.atStartOfDay(), fecha.atTime(LocalTime.MAX));

        return citas.stream()
                .map(this::mapToCitasResponse)
                .toList();
    }

    @Override
    @Transactional
    public boolean cancelarCita(Long idCita) {
        var cita = citaRepository.findById(idCita);
        if (cita.isPresent()) {
            citaRepository.deleteById(idCita);
            return true;
        }
        else {
            throw new RuntimeException("La Cita con ID: " + idCita + " no se encontro");
        }
    }

    @Override
    @Transactional
    public CitaResponse editarCita(Long idCita, CitaRequest citaRequest) {
        validacioncitas(citaRequest, idCita);
        log.info("ID Cita: " + idCita);
        var citaActualizada = citaRepository.findById(idCita)
                .map(existing -> {
                    var updatedCita = mapRequestToCita(citaRequest);
                    updatedCita.setId(idCita);

                    log.info(updatedCita.toString());
                    return citaRepository.save(updatedCita);
                })
                .orElseThrow(() -> new RuntimeException("Cita con ID: " + idCita + " no encontrada"));

        return mapToCitasResponse(citaActualizada);
    }

    private Cita mapRequestToCita(CitaRequest citaRequest) {
        return Cita.builder()
                .id(citaRequest.id())
                .consultorio(citaRequest.consultorio())
                .doctor(citaRequest.doctor())
                .fechaCita(citaRequest.fechaCita())
                .nombrePaciente(citaRequest.nombrePaciente())
                .build();
    }

    private CitaResponse mapToCitasResponse(Cita cita) {
        return new CitaResponse(
                cita.getId(),
                cita.getDoctor().getId(),
                cita.getConsultorio().getId(),
                cita.getFechaCita(),
                cita.getNombrePaciente()
        );
    }

    private void validacioncitas(CitaRequest citaRequest, Long idCitaActual){
        LocalDateTime inicioCita = citaRequest.fechaCita();
        LocalDate diaCita = inicioCita.toLocalDate();
        LocalDateTime finalCita = inicioCita.plusHours(1);

        var consultorio = consultorioRepository.findById(citaRequest.consultorio().getId())
                .orElseThrow(() -> new RuntimeException("Consultorio no encontrado"));

        var doctor = doctorRepository.findById(citaRequest.doctor().getId())
                .orElseThrow(() -> new RuntimeException("Doctor no encontrado"));

        boolean existeOtraCitaConsultorio = citaRepository
                .findByConsultorioAndFechaCitaBetween(consultorio, inicioCita, finalCita)
                .stream()
                .anyMatch(c -> !c.getId().equals(idCitaActual));

        if (existeOtraCitaConsultorio) {
            throw new RuntimeException("El consultorio ya tiene una cita agendada en ese horario");
        }

        boolean existeOtraCitaDoctor = citaRepository
                .findByDoctorAndFechaCitaBetween(doctor, inicioCita, finalCita)
                .stream()
                .anyMatch(c -> !c.getId().equals(idCitaActual));

        if (existeOtraCitaDoctor) {
            throw new RuntimeException("El doctor ya tiene una cita agendada en ese horario");
        }

        List<Cita> citasAgendadas = citaRepository
                .findByNombrePacienteAndFechaCitaBetween(citaRequest.nombrePaciente(), inicioCita.minusHours(2), finalCita.plusHours(2))
                .stream()
                .filter(c -> !c.getId().equals(idCitaActual))
                .toList();

        boolean citaCercana = citasAgendadas.stream().anyMatch(cita -> {
            LocalDateTime fechaInicioExistente = cita.getFechaCita();
            LocalDateTime fechaFinExistente = fechaInicioExistente.plusHours(1);
            return inicioCita.isBefore(fechaFinExistente.plusHours(2)) && finalCita.isAfter(fechaInicioExistente.plusHours(2));
        });

        if (citaCercana) {
            throw new RuntimeException("El paciente tiene una cita cercana agendada");
        }

        long totalCitasDoctor = citaRepository
                .findByDoctorAndFechaCitaBetween(doctor, diaCita.atStartOfDay(), diaCita.atTime(23, 59))
                .stream()
                .filter(c -> !c.getId().equals(idCitaActual))
                .count();

        if (totalCitasDoctor >= 8) {
            throw new RuntimeException("El doctor no puede tener más de 8 citas en un día");
        }
    }
}
