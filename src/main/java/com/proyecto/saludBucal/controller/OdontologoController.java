package com.proyecto.saludBucal.controller;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.proyecto.saludBucal.dto.request.OdontologoRequest;
import com.proyecto.saludBucal.dto.response.OdontologoResponse;
import com.proyecto.saludBucal.service.OdontologoService;

@Validated
@RestController
@RequestMapping("/api/odontologos")
@RequiredArgsConstructor
public class OdontologoController {

    private final OdontologoService odontologoService;

    @PostMapping
    public ResponseEntity<OdontologoResponse> registrarOdontologo(
            @Valid @RequestBody OdontologoRequest request) {
        OdontologoResponse response =
                odontologoService.registrarOdontologo(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<OdontologoResponse>> listarOdontologos() {
        return ResponseEntity.ok(
                odontologoService.listarOdontologos()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<OdontologoResponse> buscarOdontologoPorId(
            @PathVariable Integer id) {
        return ResponseEntity.ok(
                odontologoService.buscarOdontologoPorId(id)
        );
    }

    @GetMapping("/dni/{dni}")
    public ResponseEntity<OdontologoResponse> buscarOdontologoPorDni(
            @PathVariable
            @Pattern(
                regexp = "^[0-9]{8}$",
                message = "El DNI debe contener exactamente 8 dígitos"
            )
            String dni) {
        return ResponseEntity.ok(
                odontologoService.buscarOdontologoPorDni(dni)
        );
    }
}