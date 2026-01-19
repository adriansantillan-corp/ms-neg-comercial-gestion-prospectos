package com.nitro.neg.comercial.gestionprospectos.domain.model.vo;

public record DocumentoIdentidad(String tipo, String numero) {
    public DocumentoIdentidad {
        if (tipo == null || tipo.isBlank()) throw new IllegalArgumentException("El tipo de documento es obligatorio");
        if (numero == null || numero.isBlank()) throw new IllegalArgumentException("El número de documento es obligatorio");
    }
}