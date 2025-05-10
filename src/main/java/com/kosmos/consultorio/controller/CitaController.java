package com.kosmos.consultorio.controller;

import com.kosmos.consultorio.models.dtos.request.CitaRequest;
import com.kosmos.consultorio.models.dtos.response.GeneralResponse;
import com.kosmos.consultorio.service.interfaces.CitaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/citas")
public class CitaController {

    private final CitaService citaService;

    public CitaController(CitaService citaService) {
        this.citaService = citaService;
    }

    @PostMapping
    public ResponseEntity<GeneralResponse> crearCita(@RequestBody CitaRequest citaRequest) {
        var cita = citaService.agendarCita(citaRequest);
        URI location = URI.create("/api/v1/citas/" + cita.id());

        return ResponseEntity.created(location).body(
                GeneralResponse.builder()
                .message("Cita creada")
                .status(HttpStatus.CREATED.value())
                .data(cita)
                .build()
        );
    }

    @GetMapping("/{fecha}")
    public ResponseEntity<GeneralResponse> obtenerCitasPorFecha(@PathVariable LocalDate fecha) {
        var citas = citaService.obtenerPorFecha(fecha);

        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Citas encontradas por fecha")
                .status(HttpStatus.OK.value())
                .data(citas)
                .build()
        );
    }

    @GetMapping("/doctor/{idDoctor}/{fecha}")
    public ResponseEntity<GeneralResponse> obtenerCitasPorDoctorYFecha(@PathVariable Long idDoctor, @PathVariable LocalDate fecha) {
        var citas = citaService.obtenerPorDoctorAndFecha(idDoctor, fecha);

        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Citas encontradas por doctor y fecha")
                .status(HttpStatus.OK.value())
                .data(citas)
                .build()
        );
    }

    @GetMapping("/consultorio/{consultorioId}/{fecha}")
    public ResponseEntity<GeneralResponse> obtenerCitasPorConsultorioYFecha(@PathVariable Long consultorioId, @PathVariable LocalDate fecha) {
        var citas = citaService.obtenerPorConsultorioAndFecha(consultorioId, fecha);

        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Citas encontradas por consultorio y fecha")
                .status(HttpStatus.OK.value())
                .data(citas)
                .build()
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<GeneralResponse> eliminarCita(@PathVariable Long id) {
        var citaEliminada = citaService.cancelarCita(id);

        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Cita eliminada")
                .status(HttpStatus.OK.value())
                .data(citaEliminada)
                .build()
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneralResponse> editarCita(@PathVariable Long id, @RequestBody CitaRequest citaRequest) {
        var citaActualizada = citaService.editarCita(id, citaRequest);

        return ResponseEntity.ok(GeneralResponse.builder()
                .message("Cita actualizada")
                .status(HttpStatus.OK.value())
                .data(citaActualizada)
                .build()
        );
    }
}
