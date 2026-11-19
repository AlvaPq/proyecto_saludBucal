package com.proyecto.saludBucal.dto.response;
import java.time.LocalDate;
import java.time.LocalTime;

import com.proyecto.saludBucal.entity.EstadoCita;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CitaResponse {

    private Integer id;
    private LocalDate fecha;
    private LocalTime hora;
    private EstadoCita estado;
    private Integer pacienteId;
    private String pacienteNombre;
    private Integer odontologoId;
    private String odontologoNombre;
}