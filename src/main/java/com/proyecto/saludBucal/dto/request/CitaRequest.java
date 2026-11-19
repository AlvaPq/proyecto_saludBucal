package com.proyecto.saludBucal.dto.request;
import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaRequest {

    @NotNull(message = "El ID del paciente es obligatorio")
    private Integer pacienteId;
    @NotNull(message = "El ID del odontólogo es obligatorio")
    private Integer odontologoId;
    @NotNull(message = "La fecha de la cita es obligatoria")
    @FutureOrPresent(message = "La fecha no puede ser anterior a hoy")
    private LocalDate fecha;
    @NotNull(message = "La hora de la cita es obligatoria")
    private LocalTime hora;
}