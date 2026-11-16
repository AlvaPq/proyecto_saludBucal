package com.proyecto.saludBucal.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PacienteRequest {

	@NotBlank(message = "El Dni es Obligatorio")
	@Pattern(regexp = "^[0-9]{8}$", 
	         message = "El Dni debe contener 8 digitos")
	private String dni;
	
	@NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 100, message = "Los nombres no pueden superar los 100 caracteres")
    private String nombres;

    @NotBlank(message = "Los apellidos son obligatorios")
    @Size(max = 100, message = "Los apellidos no pueden superar los 100 caracteres")
    private String apellidos;
	
    private Integer edad;
}
