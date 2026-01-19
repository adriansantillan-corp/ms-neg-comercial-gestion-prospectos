package com.nitro.neg.comercial.gestionprospectos.domain.port.output;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.ProspectoId;
import reactor.core.publisher.Mono;

/**
 * Contrato para la persistencia del Agregado Prospecto.
 */
public interface ProspectoRepositoryPort {

    Mono<Prospecto> save(Prospecto prospecto);

    Mono<Prospecto> findById(ProspectoId id);
}