package com.nitro.neg.comercial.gestionprospectos.domain.model.enums;

public enum EstadoSolicitud {
    PENDIENTE, APROBADO, RECHAZADO, EN_REVISION;

    public static EstadoSolicitud fromString(String value) {
        if (value == null) return PENDIENTE;
        try {
            return EstadoSolicitud.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return PENDIENTE;
        }
    }
}