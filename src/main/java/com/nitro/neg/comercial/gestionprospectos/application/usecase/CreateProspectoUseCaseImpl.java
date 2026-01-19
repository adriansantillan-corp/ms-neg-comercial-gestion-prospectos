package com.nitro.neg.comercial.gestionprospectos.application.usecase;

import com.nitro.neg.comercial.gestionprospectos.domain.exception.DomainException;
import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.port.input.CreateProspectoUseCase;
import com.nitro.neg.comercial.gestionprospectos.domain.port.output.ProspectoRepositoryPort;
import com.nitro.neg.comercial.gestionprospectos.domain.port.output.SalesforceIntegrationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

/**
 * Implementación del Caso de Uso para Crear Prospectos.
 * Orquesta la persistencia local y la sincronización con Salesforce.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CreateProspectoUseCaseImpl implements CreateProspectoUseCase {

    private final ProspectoRepositoryPort repositoryPort;
    private final SalesforceIntegrationPort salesforcePort;

    @Override
    @Transactional
    public Mono<Prospecto> ejecutar(Prospecto prospectoInput) {
        log.info("Iniciando creación de prospecto: {}", prospectoInput.getId());

        return repositoryPort.save(prospectoInput)
                .flatMap(this::sincronizarConSalesforce)
                .onErrorResume(e -> manejarErrorCreacion(e, prospectoInput));
    }

    /**
     * Intenta enviar el prospecto guardado a Salesforce y actualiza su ID externo.
     */
    private Mono<Prospecto> sincronizarConSalesforce(Prospecto prospectoGuardado) {
        return salesforcePort.enviarProspecto(prospectoGuardado)
                .flatMap(salesforceId -> {
                    log.info("Sincronización exitosa. SF ID: {}", salesforceId);

                    prospectoGuardado.confirmarSincronizacion(salesforceId);

                    return repositoryPort.save(prospectoGuardado);
                })
                .doOnError(e -> log.warn("Fallo al sincronizar con Salesforce. Se mantiene registro local. Error: {}", e.getMessage()))
                .onErrorReturn(prospectoGuardado);
    }

    private Mono<Prospecto> manejarErrorCreacion(Throwable error, Prospecto prospecto) {
        log.error("Error crítico creando prospecto {}: {}", prospecto.getId(), error.getMessage());
        return Mono.error(new DomainException("No se pudo crear el prospecto debido a un error interno.", error));
    }
}