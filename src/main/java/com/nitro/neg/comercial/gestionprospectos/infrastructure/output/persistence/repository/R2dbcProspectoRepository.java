package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository;

import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.ProspectoEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface R2dbcProspectoRepository extends ReactiveCrudRepository<ProspectoEntity, String> {

}