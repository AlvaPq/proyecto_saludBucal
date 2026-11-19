package com.proyecto.saludBucal.service;

import java.util.List;

import com.proyecto.saludBucal.dto.request.OdontologoRequest;
import com.proyecto.saludBucal.dto.response.OdontologoResponse;

public interface OdontologoService {

    OdontologoResponse registrarOdontologo(OdontologoRequest request);
    List<OdontologoResponse> listarOdontologos();
    OdontologoResponse buscarOdontologoPorId(Integer id);
    OdontologoResponse buscarOdontologoPorDni(String dni);
}