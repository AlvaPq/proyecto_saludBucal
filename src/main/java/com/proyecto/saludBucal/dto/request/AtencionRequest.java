package com.proyecto.saludBucal.dto.request;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtencionRequest {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Integer pacienteId;
    @NotNull(message = "La fecha de atención es obligatoria")
    private LocalDate fecha;
    @NotBlank(message = "El diagnóstico es obligatorio")
    @Size(max = 2000, message = "El diagnóstico no puede superar los 2000 caracteres")
    private String diagnostico;
    @NotBlank(message = "El tratamiento es obligatorio")
    @Size(max = 2000, message = "El tratamiento no puede superar los 2000 caracteres")
    private String tratamiento;
}