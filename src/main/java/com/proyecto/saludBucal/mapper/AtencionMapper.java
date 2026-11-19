package com.proyecto.saludBucal.mapper;

import org.springframework.stereotype.Component;

import com.proyecto.saludBucal.dto.request.AtencionRequest;
import com.proyecto.saludBucal.dto.response.AtencionResponse;
import com.proyecto.saludBucal.entity.Atencion;

@Component
public class AtencionMapper {

    public Atencion toEntity(AtencionRequest request) {
        return Atencion.builder()
                .fecha(request.getFecha())
                .diagnostico(request.getDiagnostico())
                .tratamiento(request.getTratamiento())
                .build();
    }
    public AtencionResponse toResponse(Atencion atencion) {
        return AtencionResponse.builder()
                .id(atencion.getId())
                .fecha(atencion.getFecha())
                .diagnostico(atencion.getDiagnostico())
                .tratamiento(atencion.getTratamiento())
                .pacienteId(atencion.getPaciente().getId())
                .pacienteDni(atencion.getPaciente().getDni())
                .pacienteNombre(
                        atencion.getPaciente().getNombre() + " "
                        + atencion.getPaciente().getApellidos()) .build();
    }
}