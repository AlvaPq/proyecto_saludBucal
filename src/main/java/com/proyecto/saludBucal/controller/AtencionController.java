package com.proyecto.saludBucal.controller;
import java.util.List;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.saludBucal.dto.request.AtencionRequest;
import com.proyecto.saludBucal.dto.response.AtencionResponse;
import com.proyecto.saludBucal.service.AtencionService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/atenciones")
@RequiredArgsConstructor
@Validated
public class AtencionController {

    private final AtencionService atencionService;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AtencionResponse registrar(
            @Valid @RequestBody AtencionRequest request) {
        return atencionService.registrarAtencion(request);
    }

    @GetMapping("/historial/dni/{dni}")
    public List<AtencionResponse> consultarPorDni(
            @PathVariable
            @Pattern(regexp = "^[0-9]{8}$",
                     message = "El DNI debe tener 8 dígitos")  String dni) 
    {  return atencionService.consultarHistorialPorDni(dni); }
    
    @GetMapping("/historial/nombre")
    public List<AtencionResponse> consultarPorNombre(
            @RequestParam
            @NotBlank(message = "El nombre es obligatorio")
            String nombre) {
        return atencionService.consultarHistorialPorNombre(nombre);
    }
}