package com.proyecto.saludBucal.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "pacientes")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Paciente {
	
@Id
@GeneratedValue( strategy = GenerationType.IDENTITY)
private Integer id;

@Column(nullable = false, unique = true, length = 20)
private String dni;

@Column(nullable = false, length = 100)
private String nombre;

@Column(nullable = false, length = 200)
private String apellidos;

@Column(nullable =false, length = 200)
private Integer edad;


	
	
}
