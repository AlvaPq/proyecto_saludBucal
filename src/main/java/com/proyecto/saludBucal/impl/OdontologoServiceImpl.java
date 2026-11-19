package com.proyecto.saludBucal.impl;
import java.util.List;

import org.springframework.stereotype.Service;

import com.proyecto.saludBucal.dto.request.OdontologoRequest;
import com.proyecto.saludBucal.dto.response.OdontologoResponse;
import com.proyecto.saludBucal.entity.Odontologo;
import com.proyecto.saludBucal.exception.OdontologoDniDuplicadoException;
import com.proyecto.saludBucal.exception.OdontologoNoEncontradoException;
import com.proyecto.saludBucal.mapper.OdontologoMapper;
import com.proyecto.saludBucal.repository.OdontologoRepository;
import com.proyecto.saludBucal.service.OdontologoService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OdontologoServiceImpl implements OdontologoService {

    private final OdontologoRepository odontologoRepository;
    private final OdontologoMapper odontologoMapper;

    @Override
    public OdontologoResponse registrarOdontologo(OdontologoRequest request) {
        if (odontologoRepository.existsByDni(request.getDni())) {
            throw new OdontologoDniDuplicadoException(
                "Ya existe un odontólogo registrado con ese DNI"
            );
        }
        Odontologo odontologo = odontologoMapper.toEntity(request);
        Odontologo odontologoGuardado =
                odontologoRepository.save(odontologo);
        return odontologoMapper.toResponse(odontologoGuardado);
    }

    @Override
    public List<OdontologoResponse> listarOdontologos() {
        return odontologoRepository.findAll()
                .stream()
                .map(odontologoMapper::toResponse)
                .toList();
    }

    @Override
    public OdontologoResponse buscarOdontologoPorId(Integer id) {
        Odontologo odontologo = odontologoRepository.findById(id)
                .orElseThrow(() -> new OdontologoNoEncontradoException(
                    "No se encontró un odontólogo con el ID: " + id
                ));
        return odontologoMapper.toResponse(odontologo);
    }

    @Override
    public OdontologoResponse buscarOdontologoPorDni(String dni) {
        Odontologo odontologo = odontologoRepository.findByDni(dni)
                .orElseThrow(() -> new OdontologoNoEncontradoException(
                    "No se encontró un odontólogo con el DNI: " + dni
                ));
        return odontologoMapper.toResponse(odontologo);
    }
}