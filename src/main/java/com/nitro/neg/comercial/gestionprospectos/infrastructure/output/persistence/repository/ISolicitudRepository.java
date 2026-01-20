package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository;

import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.SolicitudEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISolicitudRepository extends ReactiveCrudRepository<SolicitudEntity, String> {}