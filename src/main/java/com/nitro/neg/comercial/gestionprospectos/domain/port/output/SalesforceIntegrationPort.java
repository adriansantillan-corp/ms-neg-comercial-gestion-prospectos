package com.nitro.neg.comercial.gestionprospectos.domain.port.output;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import reactor.core.publisher.Mono;

/**
 * Contrato para la comunicación con el CRM Salesforce.
 */
public interface SalesforceIntegrationPort {

    /**
     * Envía el prospecto a Salesforce y retorna el ID remoto generado.
     */
    Mono<String> enviarProspecto(Prospecto prospecto);
}