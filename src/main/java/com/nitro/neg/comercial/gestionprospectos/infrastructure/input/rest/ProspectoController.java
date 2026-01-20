package com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.port.input.CreateProspectoUseCase;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.dto.request.ProspectoRequest;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.mapper.ProspectoRestMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/prospectos")
@Tag(name = "Prospectos", description = "Gestión de Prospectos Comerciales")
@RequiredArgsConstructor
@Slf4j
public class ProspectoController {

    private final CreateProspectoUseCase createUseCase;
    private final ProspectoRestMapper restMapper;

    @Operation(summary = "Crear Prospecto")
    @ApiResponse(responseCode = "201", description = "Creado exitosamente")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Map<String, String>> crearProspecto(@RequestBody ProspectoRequest request) {

        log.info("📩 [POST] Payload recibido: {}", request);

        Prospecto domain = restMapper.toDomain(request);

        return createUseCase.ejecutar(domain)
                .map(p -> Map.of(
                        "id", p.getId().toString(),
                        "uuid", p.getUuid(),
                        "mensaje", "Prospecto registrado exitosamente"
                ));
    }
}