package com.proyecto.saludBucal.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.saludBucal.entity.Odontologo;

@Repository
public interface OdontologoRepository extends JpaRepository<Odontologo, Integer> {

    boolean existsByDni(String dni);
    Optional<Odontologo> findByDni(String dni);
}