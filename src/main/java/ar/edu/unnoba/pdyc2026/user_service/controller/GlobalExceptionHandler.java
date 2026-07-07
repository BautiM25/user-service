package ar.edu.unnoba.pdyc2026.user_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<String> handleIllegalStateException(IllegalStateException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(feign.FeignException.class)
    public ResponseEntity<String> handleFeignException(feign.FeignException ex) {
        if (ex.status() == 403) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("El recurso solicitado no está disponible (puede estar en estado 'tentative' o no existir).");
        }
        if (ex.status() == 404) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El recurso solicitado no fue encontrado.");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Error de comunicación entre microservicios: " + ex.getMessage());
    }
}