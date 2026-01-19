package com.nitro.neg.comercial.gestionprospectos.domain.model.vo;

import java.util.UUID;

public record ProspectoId(String value) {

    public ProspectoId {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El ID del prospecto no puede ser nulo o vacío");
        }
    }

    public static ProspectoId random() {
        return new ProspectoId(UUID.randomUUID().toString());
    }

    public static ProspectoId of(String value) {
        return new ProspectoId(value);
    }

    @Override
    public String toString() {
        return value;
    }
}