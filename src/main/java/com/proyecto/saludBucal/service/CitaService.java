package com.proyecto.saludBucal.service;

import java.time.LocalDate;
import java.util.List;

import com.proyecto.saludBucal.dto.request.CitaRequest;
import com.proyecto.saludBucal.dto.response.CitaResponse;

public interface CitaService {

    CitaResponse registrarCita(CitaRequest request);
    List<CitaResponse> listarCitas();
    CitaResponse buscarCitaPorId(Integer id);
    List<CitaResponse> listarCitasPorPaciente(Integer pacienteId);
    List<CitaResponse> listarCitasPorOdontologo(Integer odontologoId);
    List<CitaResponse> listarCitasPorFecha(LocalDate fecha);
    CitaResponse cancelarCita(Integer id);
    CitaResponse reprogramarCita(
            Integer id,
            LocalDate nuevaFecha,
            java.time.LocalTime nuevaHora
    );
}