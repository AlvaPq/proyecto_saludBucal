package com.proyecto.saludBucal.repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.saludBucal.entity.Cita;
import com.proyecto.saludBucal.entity.EstadoCita;

@Repository
public interface CitaRepository extends JpaRepository<Cita, Integer> {

    List<Cita> findByPaciente_IdOrderByFechaAscHoraAsc(Integer pacienteId);
    List<Cita> findByOdontologo_IdOrderByFechaAscHoraAsc(Integer odontologoId);
    List<Cita> findByFechaOrderByHoraAsc(LocalDate fecha);

    boolean existsByOdontologo_IdAndFechaAndHoraAndEstadoNot(
            Integer odontologoId,
            LocalDate fecha,
            LocalTime hora,
            EstadoCita estado
    );

    boolean existsByPaciente_IdAndFechaAndHoraAndEstadoNot(
            Integer pacienteId,
            LocalDate fecha,
            LocalTime hora,
            EstadoCita estado
    );
}