package com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.DocumentoIdentidad;
import com.nitro.neg.comercial.gestionprospectos.domain.port.input.CreateProspectoUseCase;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.dto.request.ProspectoRequest;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.mapper.ProspectoRestMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProspectoControllerTest {

    @Mock
    private CreateProspectoUseCase createUseCase;

    @Mock
    private ProspectoRestMapper restMapper;

    @InjectMocks
    private ProspectoController controller;

    @Test
    void crearProspecto_DeberiaRetornar201() {

        ProspectoRequest req = new ProspectoRequest();
        req.setNombreRazonSocial("Test");

        Prospecto domain = new Prospecto.Builder()
                .nombreRazonSocial("Test")
                .documentoIdentidad(new DocumentoIdentidad("DNI", "123"))
                .build();

        when(restMapper.toDomain(req)).thenReturn(domain);
        when(createUseCase.ejecutar(domain)).thenReturn(Mono.just(domain));

        Mono<Map<String, String>> response = controller.crearProspecto(req);


        StepVerifier.create(response)
                .consumeNextWith(map -> {
                    assertEquals("Prospecto registrado exitosamente", map.get("mensaje"));
                    assertEquals(domain.getId().toString(), map.get("id"));
                })
                .verifyComplete();
    }
}