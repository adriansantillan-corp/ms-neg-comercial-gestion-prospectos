package com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.port.input.CreateProspectoUseCase;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.api.ProspectoApi;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.dto.request.ProspectoRequest;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.mapper.ProspectoRestMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@Slf4j
public class ProspectoController implements ProspectoApi {

    private final CreateProspectoUseCase createUseCase;
    private final ProspectoRestMapper restMapper;

    public ProspectoController(CreateProspectoUseCase createUseCase, ProspectoRestMapper restMapper) {
        this.createUseCase = createUseCase;
        this.restMapper = restMapper;
        log.info("✅ CONTROLLER INICIADO: Mapeos heredados de ProspectoApi");
    }

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Map<String, String>> crearProspecto(ProspectoRequest request) {

        log.info("📩 Recibiendo petición POST crearProspecto");
        Prospecto domain = restMapper.toDomain(request);

        return createUseCase.ejecutar(domain)
                .map(p -> Map.of(
                        "id", p.getId().toString(),
                        "uuid", p.getUuid(),
                        "mensaje", "Prospecto registrado exitosamente"
                ));
    }
}