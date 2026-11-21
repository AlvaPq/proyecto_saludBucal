
package com.proyecto.saludBucal.impl;

import lombok.RequiredArgsConstructor;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.saludBucal.dto.request.PacienteRequest;
import com.proyecto.saludBucal.dto.response.PacienteResponse;
import com.proyecto.saludBucal.entity.Paciente;
import com.proyecto.saludBucal.exception.DniDuplicadoException;
import com.proyecto.saludBucal.exception.PacienteNoEncontradoException;
import com.proyecto.saludBucal.mapper.PacienteMapper;
import com.proyecto.saludBucal.repository.PacienteRepository;
import com.proyecto.saludBucal.service.PacienteService;

@Service
@RequiredArgsConstructor
@Transactional
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    @Override
    public PacienteResponse registrarPaciente(PacienteRequest request) {
        if (pacienteRepository.existsByDni(request.getDni())) {
            throw new DniDuplicadoException("Ya existe un paciente registrado con ese DNI");
        }

        Paciente paciente = pacienteMapper.toEntity(request);
        Paciente pacienteGuardado = pacienteRepository.save(paciente);
        return pacienteMapper.toResponse(pacienteGuardado);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> listarPacientes() {
        return pacienteRepository.findAll()
                .stream()
                .map(pacienteMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse buscarPacientePorId(Integer id) {
        Paciente paciente = pacienteRepository.findById(id)
                .orElseThrow(() -> new PacienteNoEncontradoException(
                    "No se encontró un paciente con el ID: " + id
                ));
        return pacienteMapper.toResponse(paciente);
    }

    @Override
    @Transactional(readOnly = true)
    public PacienteResponse buscarPacientePorDni(String dni) {
        Paciente paciente = pacienteRepository.findByDni(dni)
                .orElseThrow(() -> new PacienteNoEncontradoException(
                    "No se encontró un paciente con el DNI: " + dni
                ));
        return pacienteMapper.toResponse(paciente);
    }
}
