package com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.advice;

import com.nitro.neg.comercial.gestionprospectos.domain.exception.DomainException;
import lombok.extern.slf4j.Slf4j; // 1. Importante: Agregar Lombok @Slf4j
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.support.WebExchangeBindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.resource.NoResourceFoundException;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DomainException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleDomainException(DomainException ex) {
        log.warn("Error de Negocio: {}", ex.getMessage()); // Loguear warning
        return Mono.just(ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Regla de Negocio",
                        "mensaje", ex.getMessage()
                )));
    }

    @ExceptionHandler(WebExchangeBindException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleValidationException(WebExchangeBindException ex) {
        String errores = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "));

        log.warn("Error de Validación: {}", errores);

        return Mono.just(ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Validación Fallida",
                        "mensaje", errores
                )));
    }

    @ExceptionHandler(Exception.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleGeneric(Exception ex) {
        log.error("Error Inesperado en el servidor: ", ex);

        return Mono.just(ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Error Interno",
                        "mensaje", "Ha ocurrido un error inesperado en el servidor."
                )));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Mono<ResponseEntity<Map<String, Object>>> handleNoResourceFound(NoResourceFoundException ex) {
        return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "timestamp", LocalDateTime.now(),
                        "error", "Endpoint No Encontrado",
                        "mensaje", "La ruta solicitada no existe o el método HTTP es incorrecto."
                )));
    }
}