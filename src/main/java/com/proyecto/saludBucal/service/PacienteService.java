package com.proyecto.saludBucal.service;

import java.util.List;

import com.proyecto.saludBucal.dto.request.PacienteRequest;
import com.proyecto.saludBucal.dto.response.PacienteResponse;

public interface PacienteService {

    PacienteResponse registrarPaciente(PacienteRequest request);
    
    List<PacienteResponse> listarPacientes();


    PacienteResponse buscarPacientePorId(Integer id);

    PacienteResponse buscarPacientePorDni(String dni);

}