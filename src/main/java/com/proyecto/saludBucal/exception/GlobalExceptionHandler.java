package com.proyecto.saludBucal.exception;
import java.time.LocalDateTime;
import java.util.Map;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.proyecto.saludBucal.dto.response.ErrorResponse;



@RestControllerAdvice
public class GlobalExceptionHandler {

   
    @ExceptionHandler(DniDuplicadoException.class)
    public ResponseEntity<ErrorResponse> manejarDniDuplicado(
            DniDuplicadoException ex) {

        ErrorResponse error = ErrorResponse.builder()
                .fecha(LocalDateTime.now())
                .status(HttpStatus.CONFLICT.value())
                .error("Conflicto")
                .mensaje(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

  
    @ExceptionHandler(PacienteNoEncontradoException.class)
    public ResponseEntity<ErrorResponse> manejarPacienteNoEncontrado(
            PacienteNoEncontradoException ex) {

        ErrorResponse error = ErrorResponse.builder()
                .fecha(LocalDateTime.now())
                .status(HttpStatus.NOT_FOUND.value())
                .error("No encontrado")
                .mensaje(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> manejarValidaciones(
            MethodArgumentNotValidException ex) {

        Map<String, String> detalles = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                        error -> error.getField(),
                        error -> error.getDefaultMessage(),
                        (mensajeAnterior, mensajeNuevo) -> mensajeAnterior
                ));

        ErrorResponse error = ErrorResponse.builder()
                .fecha(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Error de validación")
                .mensaje("Los datos enviados no son válidos")
                .detalles(detalles)
                .build();

        return ResponseEntity.badRequest().body(error);
    }
    
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResponse> manejarViolacionDeRestricciones(
            ConstraintViolationException ex) {

        Map<String, String> detalles = ex.getConstraintViolations()
                .stream()
                .collect(java.util.stream.Collectors.toMap(
                        violation -> violation.getPropertyPath().toString(),
                        violation -> violation.getMessage(),
                        (anterior, nuevo) -> anterior
                ));

        ErrorResponse error = ErrorResponse.builder()
                .fecha(LocalDateTime.now())
                .status(HttpStatus.BAD_REQUEST.value())
                .error("Error de validación")
                .mensaje("Los datos enviados no son válidos")
                .detalles(detalles)
                .build();

        return ResponseEntity.badRequest().body(error);
    }
}