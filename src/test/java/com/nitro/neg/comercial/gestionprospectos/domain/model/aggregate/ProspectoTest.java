package com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate;

import com.nitro.neg.comercial.gestionprospectos.domain.exception.DomainException;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Foto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.DocumentoIdentidad;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ProspectoTest {

    @Test
    void builder_DeberiaCrearProspecto_CuandoDatosSonValidos() {
        Prospecto p = new Prospecto.Builder()
                .nombreRazonSocial("Valido")
                .documentoIdentidad(new DocumentoIdentidad("DNI", "123"))
                .build();
        assertNotNull(p.getId());
        assertNotNull(p.getUuid());
    }

    @Test
    void builder_DeberiaLanzarExcepcion_CuandoFaltaRazonSocial() {
        assertThrows(DomainException.class, () ->
                new Prospecto.Builder()
                        .documentoIdentidad(new DocumentoIdentidad("DNI", "123"))
                        .build()
        );
    }

    @Test
    void confirmarSincronizacion_DeberiaActualizarSalesforceId() {
        Prospecto p = new Prospecto.Builder()
                .nombreRazonSocial("Test")
                .documentoIdentidad(new DocumentoIdentidad("DNI", "123"))
                .build();

        p.confirmarSincronizacion("SF-NEW-ID");

        assertEquals("SF-NEW-ID", p.getSalesforceId());
    }

    @Test
    void agregarFoto_DeberiaAgregarALista() {
        Prospecto p = new Prospecto.Builder()
                .nombreRazonSocial("Test")
                .documentoIdentidad(new DocumentoIdentidad("DNI", "123"))
                .build();

        p.agregarFoto(new Foto("1", "url", "path", "name"));

        assertEquals(1, p.getFotos().size());
    }
}