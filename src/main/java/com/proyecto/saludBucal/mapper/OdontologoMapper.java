package com.proyecto.saludBucal.mapper;

import org.springframework.stereotype.Component;

import com.proyecto.saludBucal.dto.request.OdontologoRequest;
import com.proyecto.saludBucal.dto.response.OdontologoResponse;
import com.proyecto.saludBucal.entity.Odontologo;

@Component
public class OdontologoMapper {

    public Odontologo toEntity(OdontologoRequest request) {

        return Odontologo.builder()
                .dni(request.getDni())
                .nombres(request.getNombres())
                .apellidos(request.getApellidos())
                .build();
    }

    public OdontologoResponse toResponse(Odontologo odontologo) {

        return OdontologoResponse.builder()
                .id(odontologo.getId())
                .dni(odontologo.getDni())
                .nombres(odontologo.getNombres())
                .apellidos(odontologo.getApellidos())
                .build();
    }
}