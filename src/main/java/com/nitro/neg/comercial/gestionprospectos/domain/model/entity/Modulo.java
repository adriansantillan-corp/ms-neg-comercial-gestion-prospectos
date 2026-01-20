package com.nitro.neg.comercial.gestionprospectos.domain.model.entity;

import com.nitro.neg.comercial.gestionprospectos.domain.exception.DomainException;

public record Modulo(
        String id,
        String uuid,
        String diasVisita,
        String idModuloAsignado,
        String periodoVisita
) {
    public Modulo {
        if (uuid == null || uuid.isBlank()) {
            throw new DomainException("El UUID del módulo es obligatorio.");
        }
    }

    public String getId() { return id; }
}