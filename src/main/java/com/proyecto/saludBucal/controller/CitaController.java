package com.proyecto.saludBucal.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import com.proyecto.saludBucal.dto.request.CitaRequest;
import com.proyecto.saludBucal.dto.response.CitaResponse;
import com.proyecto.saludBucal.service.CitaService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;

@RestController 
@RequestMapping("/api/citas")
@RequiredArgsConstructor
@Validated
public class CitaController {

    private final CitaService citaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CitaResponse registrarCita(@Valid @RequestBody CitaRequest request) {
        return citaService.registrarCita(request);
    }

    @GetMapping
    public List<CitaResponse> listarCitas() {
        return citaService.listarCitas();
    }

    @GetMapping("/{id}")
    public CitaResponse buscarCitaPorId(@PathVariable Integer id) {
        return citaService.buscarCitaPorId(id);
    }

    @GetMapping("/paciente/{pacienteId}")
    public List<CitaResponse> listarCitasPorPaciente(
            @PathVariable Integer pacienteId) {
        return citaService.listarCitasPorPaciente(pacienteId);
    }

    @GetMapping("/odontologo/{odontologoId}")
    public List<CitaResponse> listarCitasPorOdontologo(
            @PathVariable Integer odontologoId) {
        return citaService.listarCitasPorOdontologo(odontologoId);
    }

    @GetMapping("/fecha/{fecha}")
    public List<CitaResponse> listarCitasPorFecha(
            @PathVariable
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fecha) {
        return citaService.listarCitasPorFecha(fecha);
    }

    @PatchMapping("/{id}/cancelar")
    public CitaResponse cancelarCita(@PathVariable Integer id) {
        return citaService.cancelarCita(id);
    }

    @PatchMapping("/{id}/reprogramar")
    public CitaResponse reprogramarCita(
            @PathVariable Integer id,
            @RequestParam
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @FutureOrPresent
            LocalDate nuevaFecha,
            @RequestParam
            @NotNull
            @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
            LocalTime nuevaHora) {
        return citaService.reprogramarCita(id, nuevaFecha, nuevaHora);
    }
}