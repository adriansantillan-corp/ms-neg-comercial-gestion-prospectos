package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.external.adapter;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.port.output.SalesforceIntegrationPort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Slf4j
@Component
@ConditionalOnProperty(name = "salesforce.integration.enabled", havingValue = "false", matchIfMissing = true)
public class SalesforceStubAdapter implements SalesforceIntegrationPort {

    public SalesforceStubAdapter() {
        log.warn("⚠️ MODO STUB ACTIVADO: No se conectará con Salesforce real.");
    }

    @Override
    public Mono<String> enviarProspecto(Prospecto prospecto) {
        log.info("📝 [STUB] Simulando envío a Salesforce para: {}", prospecto.getNombreRazonSocial());
        return Mono.just("SF-MOCK-" + UUID.randomUUID().toString().substring(0, 8));
    }
}