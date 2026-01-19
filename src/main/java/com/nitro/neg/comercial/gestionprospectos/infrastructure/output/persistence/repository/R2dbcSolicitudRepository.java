package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository;

import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.SolicitudEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface R2dbcSolicitudRepository extends ReactiveCrudRepository<SolicitudEntity, String> {

    Mono<SolicitudEntity> findByProspectoId(String prospectoId);
}