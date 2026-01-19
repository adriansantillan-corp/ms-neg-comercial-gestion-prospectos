package com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.api;

import com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.dto.request.ProspectoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping; // Importar
import org.springframework.web.bind.annotation.RequestBody; // Importar
import org.springframework.web.bind.annotation.RequestMapping; // Importar
import reactor.core.publisher.Mono;

import java.util.Map;

@Tag(name = "Prospectos", description = "Gestión de Prospectos Comerciales")
@RequestMapping("/api/v1/prospectos")
public interface ProspectoApi {

    @Operation(summary = "Crear Prospecto", description = "Registra un prospecto localmente y lo sincroniza con Salesforce.")
    @ApiResponse(responseCode = "201", description = "Creado exitosamente", content = @Content(schema = @Schema(implementation = Map.class)))
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    Mono<Map<String, String>> crearProspecto(@RequestBody ProspectoRequest request);
}