package com.nitro.neg.comercial.gestionprospectos.domain.model.entity;

import com.nitro.neg.comercial.gestionprospectos.domain.exception.DomainException;

public record Solicitud(
        String id,
        String uuid,
        String estado,
        String tipo,
        String recordTypeId,
        boolean enviarParaAprobacion,
        boolean creadoEnNitroApp
) {
    public Solicitud {
        if (uuid == null || uuid.isBlank()) {
            throw new DomainException("El UUID de la solicitud es obligatorio.");
        }
    }
}