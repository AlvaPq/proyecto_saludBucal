package com.proyecto.saludBucal.dto.response;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AtencionResponse {

    private Integer id;
    private LocalDate fecha;
    private String diagnostico;
    private String tratamiento;
    private Integer pacienteId;
    private String pacienteDni;
    private String pacienteNombre;
}