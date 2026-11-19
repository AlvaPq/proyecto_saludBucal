package com.proyecto.saludBucal.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.saludBucal.entity.Atencion;


@Repository
public interface AtencionRepository extends JpaRepository<Atencion, Integer> {

    List<Atencion> findByPaciente_IdOrderByFechaDescIdDesc(Integer pacienteId);

}