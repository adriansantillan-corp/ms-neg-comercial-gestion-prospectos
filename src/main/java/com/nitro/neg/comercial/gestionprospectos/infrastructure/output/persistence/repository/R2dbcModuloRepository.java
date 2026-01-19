package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository;

import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.ModuloEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface R2dbcModuloRepository extends ReactiveCrudRepository<ModuloEntity, String> {

    Mono<ModuloEntity> findByProspectoId(String prospectoId);
}