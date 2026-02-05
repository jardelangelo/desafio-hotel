/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package br.com.jardel.desafio_hotel.api.exceptions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

/**
 *
 * @author jarde
 */

@RestControllerAdvice
public class ApiExceptionHandler {
    
    private static final Logger log = LogManager.getLogger(ApiExceptionHandler.class);

    public record ErroPadrao(Instant timestamp, int status, String erro) {}

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErroPadrao> tratarNaoEncontrado(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroPadrao(Instant.now(), 404, ex.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroPadrao> tratarRegra(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroPadrao(Instant.now(), 400, ex.getMessage()));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErroPadrao> tratarNaoEncontrado(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroPadrao(Instant.now(), 409, ex.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroPadrao> tratarErroGeral(Exception ex) {
        // Loga com stacktrace para investigação
        log.error("Erro inesperado na API", ex);

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroPadrao(Instant.now(), 500, "erro interno"));
    }
    
}
