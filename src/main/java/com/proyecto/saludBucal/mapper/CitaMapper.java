package com.proyecto.saludBucal.mapper;

import org.springframework.stereotype.Component;

import com.proyecto.saludBucal.dto.request.CitaRequest;
import com.proyecto.saludBucal.dto.response.CitaResponse;
import com.proyecto.saludBucal.entity.Cita;
import com.proyecto.saludBucal.entity.EstadoCita;

@Component
public class CitaMapper {

    public Cita toEntity(CitaRequest request) {

        return Cita.builder()
                .fecha(request.getFecha())
                .hora(request.getHora())
                .estado(EstadoCita.PROGRAMADA)
                .build();
    }

    public CitaResponse toResponse(Cita cita) {

        return CitaResponse.builder()
                .id(cita.getId())
                .fecha(cita.getFecha())
                .hora(cita.getHora())
                .estado(cita.getEstado())
                .pacienteId(cita.getPaciente().getId())
                .pacienteNombre(
                        cita.getPaciente().getNombre() + " "
                        + cita.getPaciente().getApellidos()
                )
                .odontologoId(cita.getOdontologo().getId())
                .odontologoNombre(
                        cita.getOdontologo().getNombres() + " "
                        + cita.getOdontologo().getApellidos()
                )
                .build();
    }
}