package com.proyecto.saludBucal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.saludBucal.entity.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
	
	boolean existsByDni(String dni);
	
	 Optional<Paciente> findByDni(String dni);

}
