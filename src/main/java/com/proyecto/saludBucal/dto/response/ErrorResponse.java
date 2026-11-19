package com.proyecto.saludBucal.dto.response;
import java.time.LocalDateTime;
import java.util.Map;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private LocalDateTime fecha;
    private int status;
    private String error;
    private String mensaje;
    private Map<String, String> detalles;
}