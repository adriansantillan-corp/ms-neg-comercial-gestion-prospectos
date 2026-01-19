package com.nitro.neg.comercial.gestionprospectos.domain.model.vo;

public record Direccion(
        String calle,
        String numero,
        String interior,
        String lote,
        String manzana,
        String ciudad,
        String estado,
        String pais,
        String codigoPostal,
        String clasificacion,
        String detalleClasificacion,
        String tipoVia,
        String detalleVia,
        String referencia
) {}