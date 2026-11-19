package com.proyecto.saludBucal.service;

import java.util.List;


import com.proyecto.saludBucal.dto.request.AtencionRequest;
import com.proyecto.saludBucal.dto.response.AtencionResponse;


public interface AtencionService {
    AtencionResponse registrarAtencion(AtencionRequest request);
    List<AtencionResponse> consultarHistorialPorDni(String dni);
    List<AtencionResponse> consultarHistorialPorNombre(String nombre);
    

}