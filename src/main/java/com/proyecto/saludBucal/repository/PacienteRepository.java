package com.proyecto.saludBucal.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.proyecto.saludBucal.entity.Paciente;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Integer> {
	boolean existsByDni(String dni);
	 Optional<Paciente> findByDni(String dni);
	 
	 @Query("""
			    SELECT p FROM Paciente p
			    WHERE LOWER(p.nombre) LIKE LOWER(CONCAT('%', :nombre, '%'))
			       OR LOWER(p.apellidos) LIKE LOWER(CONCAT('%', :nombre, '%'))
			""")
			List<Paciente> buscarPorNombre(@Param("nombre") String nombre);

}
