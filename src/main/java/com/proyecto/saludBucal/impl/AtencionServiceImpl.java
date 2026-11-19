package com.proyecto.saludBucal.impl;
import java.util.Comparator;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.saludBucal.dto.request.AtencionRequest;
import com.proyecto.saludBucal.dto.response.AtencionResponse;
import com.proyecto.saludBucal.entity.Atencion;
import com.proyecto.saludBucal.entity.Paciente;
import com.proyecto.saludBucal.exception.PacienteNoEncontradoException;
import com.proyecto.saludBucal.mapper.AtencionMapper;
import com.proyecto.saludBucal.repository.AtencionRepository;
import com.proyecto.saludBucal.repository.PacienteRepository;
import com.proyecto.saludBucal.service.AtencionService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
@Transactional
public class AtencionServiceImpl implements AtencionService {

    private final AtencionRepository atencionRepository;
    private final PacienteRepository pacienteRepository;
    private final AtencionMapper atencionMapper;

    @Override
    public AtencionResponse registrarAtencion(AtencionRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new PacienteNoEncontradoException(
                        "No se encontró el paciente con ID: " + request.getPacienteId()));
        Atencion atencion = atencionMapper.toEntity(request);
        atencion.setPaciente(paciente);
        Atencion atencionGuardada = atencionRepository.save(atencion);
        return atencionMapper.toResponse(atencionGuardada);
    }
    @Override
    @Transactional(readOnly = true)
    public List<AtencionResponse> consultarHistorialPorDni(String dni) {
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new PacienteNoEncontradoException(
                        "No se encontró un paciente con DNI: " + dni));
        return atencionRepository
                .findByPaciente_IdOrderByFechaDescIdDesc(paciente.getId())
                .stream()
                .map(atencionMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtencionResponse> consultarHistorialPorNombre(String nombre) {
        List<Paciente> pacientes = pacienteRepository.buscarPorNombre(nombre);
        if (pacientes.isEmpty()) {
            throw new PacienteNoEncontradoException(
                    "No se encontraron pacientes con el nombre: " + nombre); }
        return pacientes.stream()
                .flatMap(paciente -> atencionRepository
                        .findByPaciente_IdOrderByFechaDescIdDesc(paciente.getId())
                        .stream())
                .map(atencionMapper::toResponse)
                .sorted(Comparator
                        .comparing(AtencionResponse::getFecha)
                        .reversed()
                        .thenComparing(AtencionResponse::getId, Comparator.reverseOrder()))
                .toList();
    }
}