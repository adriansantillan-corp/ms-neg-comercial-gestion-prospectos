package com.nitro.neg.comercial.gestionprospectos.domain.port.input;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import reactor.core.publisher.Mono;

@FunctionalInterface
public interface CreateProspectoUseCase {

    /**
     * Ejecuta la lógica de negocio para la creación y persistencia de un prospecto.
     *
     * @param prospecto El agregado con los datos validados.
     * @return El prospecto creado y persistido.
     */
    Mono<Prospecto> ejecutar(Prospecto prospecto);
}