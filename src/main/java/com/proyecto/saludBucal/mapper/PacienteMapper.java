package com.proyecto.saludBucal.mapper;

import org.springframework.stereotype.Component;

import com.proyecto.saludBucal.dto.request.PacienteRequest;
import com.proyecto.saludBucal.dto.response.PacienteResponse;
import com.proyecto.saludBucal.entity.Paciente;

@Component
public class PacienteMapper {

   
    public Paciente toEntity(PacienteRequest request) {

        return Paciente.builder()
                .dni(request.getDni())
                .nombre(request.getNombres())
                .apellidos(request.getApellidos())
                .edad(request.getEdad())
                .build();
    }


    public PacienteResponse toResponse(Paciente paciente) {

        return PacienteResponse.builder()
                .id(paciente.getId())
                .dni(paciente.getDni())
                .nombre(paciente.getNombre())
                .apellidos(paciente.getApellidos())
                .edad(paciente.getEdad())
                .build();
    }
}