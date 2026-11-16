package com.proyecto.saludBucal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteResponse {

	
	private Integer id;
	private String dni;
	private String nombre;
    private String apellidos;
    private Integer edad;
}
