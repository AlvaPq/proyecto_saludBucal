package com.proyecto.saludBucal.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.saludBucal.dto.request.PacienteRequest;
import com.proyecto.saludBucal.dto.response.PacienteResponse;
import com.proyecto.saludBucal.service.PacienteService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api/pacientes")
@RequiredArgsConstructor
public class PacienteController {

    private final PacienteService pacienteService;

    @PostMapping
    public ResponseEntity<PacienteResponse> registrarPaciente(
            @Valid @RequestBody PacienteRequest request) {

        PacienteResponse response =
                pacienteService.registrarPaciente(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }
    @GetMapping
    public ResponseEntity<List<PacienteResponse>> listarPacientes() {

        return ResponseEntity.ok(pacienteService.listarPacientes());
    }


    @GetMapping("/{id}")
    public ResponseEntity<PacienteResponse> buscarPacientePorId(
            @PathVariable Integer id) {

        return ResponseEntity.ok(
                pacienteService.buscarPacientePorId(id)
        );
    }


    @GetMapping("/dni/{dni}")
    public ResponseEntity<PacienteResponse> buscarPacientePorDni(
    		 @PathVariable
    	        @Pattern(
    	            regexp = "^[0-9]{8}$",
    	            message = "El DNI debe contener exactamente 8 dígitos"
    	        )
    	        String dni) {

        return ResponseEntity.ok(
                pacienteService.buscarPacientePorDni(dni)
        );
    }
}