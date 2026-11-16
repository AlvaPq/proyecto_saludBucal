package com.proyecto.saludBucal.service;

import com.proyecto.saludBucal.dto.request.PacienteRequest;
import com.proyecto.saludBucal.dto.response.PacienteResponse;

public interface PacienteService {

    PacienteResponse registrarPaciente(PacienteRequest request);

}