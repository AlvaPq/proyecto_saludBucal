package com.proyecto.saludBucal.impl;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.proyecto.saludBucal.dto.request.CitaRequest;
import com.proyecto.saludBucal.dto.response.CitaResponse;
import com.proyecto.saludBucal.entity.Cita;
import com.proyecto.saludBucal.entity.EstadoCita;
import com.proyecto.saludBucal.entity.Odontologo;
import com.proyecto.saludBucal.entity.Paciente;
import com.proyecto.saludBucal.exception.CitaNoEncontradaException;
import com.proyecto.saludBucal.exception.EstadoCitaInvalidoException;
import com.proyecto.saludBucal.exception.FechaCitaInvalidaException;
import com.proyecto.saludBucal.exception.HorarioOcupadoException;
import com.proyecto.saludBucal.exception.OdontologoNoEncontradoException;
import com.proyecto.saludBucal.exception.PacienteNoEncontradoException;
import com.proyecto.saludBucal.mapper.CitaMapper;
import com.proyecto.saludBucal.repository.CitaRepository;
import com.proyecto.saludBucal.repository.OdontologoRepository;
import com.proyecto.saludBucal.repository.PacienteRepository;
import com.proyecto.saludBucal.service.CitaService;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class CitaServiceImpl implements CitaService {

    private final CitaRepository citaRepository;
    private final PacienteRepository pacienteRepository;
    private final OdontologoRepository odontologoRepository;
    private final CitaMapper citaMapper;

    @Override
    public CitaResponse registrarCita(CitaRequest request) {
        Paciente paciente = pacienteRepository.findById(request.getPacienteId())
                .orElseThrow(() -> new PacienteNoEncontradoException(
                        "No se encontró el paciente con ID: " + request.getPacienteId()
                ));
        Odontologo odontologo = odontologoRepository.findById(request.getOdontologoId())
                .orElseThrow(() -> new OdontologoNoEncontradoException(
                        "No se encontró el odontólogo con ID: " + request.getOdontologoId()
                ));

        validarDisponibilidad(
                paciente.getId(),
                odontologo.getId(),
                request.getFecha(),
                request.getHora()
        );

        Cita cita = citaMapper.toEntity(request);
        cita.setPaciente(paciente);
        cita.setOdontologo(odontologo);
        return citaMapper.toResponse(citaRepository.save(cita));
    }
    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listarCitas() {
        return citaRepository.findAll()
                .stream()
                .map(citaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public CitaResponse buscarCitaPorId(Integer id) {
        return citaMapper.toResponse(obtenerCita(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listarCitasPorPaciente(Integer pacienteId) {
        if (!pacienteRepository.existsById(pacienteId)) {
            throw new PacienteNoEncontradoException(
                    "No se encontró el paciente con ID: " + pacienteId); }
        return citaRepository.findByPaciente_IdOrderByFechaAscHoraAsc(pacienteId)
                .stream()
                .map(citaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listarCitasPorOdontologo(Integer odontologoId) {
        if (!odontologoRepository.existsById(odontologoId)) {
            throw new OdontologoNoEncontradoException(
                    "No se encontró el odontólogo con ID: " + odontologoId );}
        return citaRepository.findByOdontologo_IdOrderByFechaAscHoraAsc(odontologoId)
                .stream()
                .map(citaMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CitaResponse> listarCitasPorFecha(LocalDate fecha) {
        return citaRepository.findByFechaOrderByHoraAsc(fecha)
                .stream()
                .map(citaMapper::toResponse)
                .toList();
    }

    @Override
    public CitaResponse cancelarCita(Integer id) {
        Cita cita = obtenerCita(id);
        if (cita.getEstado() != EstadoCita.PROGRAMADA) {
            throw new EstadoCitaInvalidoException(
                    "Solo se pueden cancelar citas programadas"  ); }
        cita.setEstado(EstadoCita.CANCELADA);
        return citaMapper.toResponse(citaRepository.save(cita));}
    @Override
    public CitaResponse reprogramarCita(
            Integer id,
            LocalDate nuevaFecha,
            LocalTime nuevaHora
    ) {

        Cita cita = obtenerCita(id);
        if (cita.getEstado() != EstadoCita.PROGRAMADA) {
            throw new EstadoCitaInvalidoException(
                    "Solo se pueden reprogramar citas programadas"  );  }
        if (nuevaFecha.isBefore(LocalDate.now())) {
            throw new FechaCitaInvalidaException(
                    "La nueva fecha no puede ser anterior a hoy"
            );
        }

        validarDisponibilidad(
                cita.getPaciente().getId(),
                cita.getOdontologo().getId(),
                nuevaFecha,
                nuevaHora
        );
        cita.setFecha(nuevaFecha);
        cita.setHora(nuevaHora);
        return citaMapper.toResponse(citaRepository.save(cita));
    }

    private Cita obtenerCita(Integer id) {
        return citaRepository.findById(id)
                .orElseThrow(() -> new CitaNoEncontradaException(
                        "No se encontró la cita con ID: " + id
                ));
    
    }

    private void validarDisponibilidad(
            Integer pacienteId,
            Integer odontologoId,
            LocalDate fecha,
            LocalTime hora
    ) {

        boolean odontologoOcupado =
                citaRepository.existsByOdontologo_IdAndFechaAndHoraAndEstadoNot(
                        odontologoId, fecha, hora, EstadoCita.CANCELADA   );
        if (odontologoOcupado) {
            throw new HorarioOcupadoException(
                    "El odontólogo ya tiene una cita en ese horario" );}
        boolean pacienteOcupado =
                citaRepository.existsByPaciente_IdAndFechaAndHoraAndEstadoNot(
                        pacienteId, fecha, hora, EstadoCita.CANCELADA );
        if (pacienteOcupado) {
            throw new HorarioOcupadoException(
                    "El paciente ya tiene una cita en ese horario" );
        }
    }
}