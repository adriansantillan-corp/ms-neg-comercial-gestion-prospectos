package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository;

import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.FotoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface R2dbcFotoRepository extends ReactiveCrudRepository<FotoEntity, String> {

    Flux<FotoEntity> findByProspectoId(String prospectoId);
}