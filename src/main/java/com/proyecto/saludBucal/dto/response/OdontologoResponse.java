package com.proyecto.saludBucal.dto.response;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OdontologoResponse {

    private Integer id;
    private String dni;
    private String nombres;
    private String apellidos;
}