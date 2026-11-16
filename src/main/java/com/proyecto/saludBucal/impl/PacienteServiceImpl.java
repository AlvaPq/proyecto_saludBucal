
package com.proyecto.saludBucal.impl;




import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import com.proyecto.saludBucal.dto.request.PacienteRequest;
import com.proyecto.saludBucal.dto.response.PacienteResponse;
import com.proyecto.saludBucal.entity.Paciente;
import com.proyecto.saludBucal.exception.DniDuplicadoException;
import com.proyecto.saludBucal.mapper.PacienteMapper;
import com.proyecto.saludBucal.repository.PacienteRepository;
import com.proyecto.saludBucal.service.PacienteService;

@Service
@RequiredArgsConstructor
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    @Override
    public PacienteResponse registrarPaciente(PacienteRequest request) {

        // 1. Verificar si el DNI ya existe
        if (pacienteRepository.existsByDni(request.getDni())) {
            throw new DniDuplicadoException(
                "Ya existe un paciente registrado con ese DNI"
            );
        }

        // 2. Convertir el DTO en entidad
        Paciente paciente = pacienteMapper.toEntity(request);

        // 3. Guardar el paciente en la base de datos
        Paciente pacienteGuardado = pacienteRepository.save(paciente);

        // 4. Convertir la entidad guardada en DTO de respuesta
        return pacienteMapper.toResponse(pacienteGuardado);
    }
}
