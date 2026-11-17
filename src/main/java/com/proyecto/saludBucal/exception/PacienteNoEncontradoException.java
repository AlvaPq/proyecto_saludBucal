package com.proyecto.saludBucal.exception;
public class PacienteNoEncontradoException extends RuntimeException {

    public PacienteNoEncontradoException(String mensaje) {
        super(mensaje);
    }
}