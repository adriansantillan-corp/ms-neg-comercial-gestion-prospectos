package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.external.adapter;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.DocumentoIdentidad;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SalesforceWebClientAdapterTest {

    @Mock private WebClient webClient;
    @Mock private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock private WebClient.RequestBodySpec requestBodySpec;
    @Mock private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock private WebClient.ResponseSpec responseSpec;

    private SalesforceWebClientAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new SalesforceWebClientAdapter(webClient);
        ReflectionTestUtils.setField(adapter, "sfApiUrl", "http://fake-sf");
        ReflectionTestUtils.setField(adapter, "sfToken", "fake-token");
    }

    @Test
    void enviarProspecto_Exitoso_DeberiaRetornarId() {

        Prospecto p = new Prospecto.Builder()
                .nombreRazonSocial("Test")
                .documentoIdentidad(new DocumentoIdentidad("RUC", "201"))
                .build();

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(any())).thenReturn(requestBodySpec);
        when(requestBodySpec.header(any(), any())).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        Map<String, Object> sfResponse = Map.of("success", true, "id", "sf-id-123");
        when(responseSpec.bodyToMono(Map.class)).thenReturn(Mono.just(sfResponse));

        StepVerifier.create(adapter.enviarProspecto(p))
                .expectNext("sf-id-123")
                .verifyComplete();
    }
}