package com.nitro.neg.comercial.gestionprospectos.domain.model.enums;

public enum TipoDocumento {
    RUC, DNI, CE, PASAPORTE, OTRO;

    public static TipoDocumento fromString(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return TipoDocumento.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return OTRO;
        }
    }
}