package com.nitro.neg.comercial.gestionprospectos.domain.model.enums;

public enum TipoSolicitud {
    ALTA, MODIFICACION, BAJA;

    public static TipoSolicitud fromString(String value) {
        if (value == null) return ALTA;
        try {
            return TipoSolicitud.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return ALTA;
        }
    }
}