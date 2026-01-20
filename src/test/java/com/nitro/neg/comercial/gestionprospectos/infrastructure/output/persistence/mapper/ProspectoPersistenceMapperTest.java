package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.mapper;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Foto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Modulo;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Solicitud;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.Contacto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.Direccion;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.DocumentoIdentidad;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.ProspectoId;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.FotoEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.ModuloEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.ProspectoEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.SolicitudEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProspectoPersistenceMapperTest {

    @InjectMocks
    private ProspectoPersistenceMapper mapper;

    @Test
    void toEntity_DeberiaMapearCorrectamenteYMarcarComoNuevo_CuandoNoTieneSalesforceId() {

        Prospecto prospecto = crearProspectoDominio(null);

        ProspectoEntity entity = mapper.toEntity(prospecto);

        assertNotNull(entity);
        assertTrue(entity.isNew(), "Debería ser isNew=true porque no tiene SalesforceId");
        assertEquals(prospecto.getId().value(), entity.getId());
        assertEquals("Empresa SAC", entity.getNombreRazonSocial());
        assertEquals("RUC", entity.getDocumentoIdentificacion());
        assertEquals("12345678901", entity.getNumeroDocumento());
        assertEquals("Calle 123", entity.getBillingStreet());
        assertEquals(true, entity.getUsarDireccionFacturacion());
    }

    @Test
    void toEntity_DeberiaMarcarComoNoNuevo_CuandoTieneSalesforceId() {

        Prospecto prospecto = crearProspectoDominio("SF-123");
        ProspectoEntity entity = mapper.toEntity(prospecto);

        assertNotNull(entity);
        assertFalse(entity.isNew(), "Debería ser isNew=false porque TIENE SalesforceId");
        assertEquals("SF-123", entity.getSalesforceId());
    }

    @Test
    void toModuloEntity_DeberiaMapearCorrectamente() {
        Modulo modulo = new Modulo(null, "uuid-mod", "LUN", "MOD-1", "SEMANAL");
        String prospectoId = "p-1";

        ModuloEntity entity = mapper.toModuloEntity(modulo, prospectoId, true);

        assertNotNull(entity);
        assertNotNull(entity.getId(), "Debería generar un ID si viene nulo");
        assertEquals("uuid-mod", entity.getUuid());
        assertEquals(prospectoId, entity.getProspectoId());
        assertTrue(entity.isNew());
    }

    @Test
    void toSolicitudEntity_DeberiaMapearCorrectamente() {
        Solicitud solicitud = new Solicitud("sol-id", "uuid-sol", "PENDIENTE", "ALTA", "RT-1", true, true);
        String prospectoId = "p-1";

        SolicitudEntity entity = mapper.toSolicitudEntity(solicitud, prospectoId, false);

        assertNotNull(entity);
        assertEquals("sol-id", entity.getId());
        assertEquals(prospectoId, entity.getProspectoId());
        assertFalse(entity.isNew());
    }

    @Test
    void toFotoEntity_DeberiaMapearCorrectamente() {

        Foto foto = new Foto(null, "http://url", "path/s3", "foto.jpg");
        String prospectoId = "p-1";

        FotoEntity entity = mapper.toFotoEntity(foto, prospectoId, true);

        assertNotNull(entity);
        assertNotNull(entity.getId());
        assertEquals("http://url", entity.getUrl());
        assertEquals("path/s3", entity.getPath());
        assertEquals(prospectoId, entity.getProspectoId());
    }

    @Test
    void toDomain_DeberiaReconstruirElAgregadoCompleto() {
        ProspectoEntity pEntity = ProspectoEntity.builder()
                .id("p-1")
                .uuid("uuid-1")
                .nombreRazonSocial("Empresa")
                .documentoIdentificacion("RUC")
                .numeroDocumento("20202020202")
                .billingStreet("Calle A")
                .shippingStreet("Calle B")
                .build();

        ModuloEntity mEntity = ModuloEntity.builder().id("m-1").uuid("u-m").build();
        SolicitudEntity sEntity = SolicitudEntity.builder().id("s-1").uuid("u-s").estado("PENDIENTE").build();
        FotoEntity fEntity = FotoEntity.builder().id("f-1").url("url").build();

        Prospecto result = mapper.toDomain(pEntity, mEntity, sEntity, List.of(fEntity));

        assertNotNull(result);
        assertEquals("p-1", result.getId().value());
        assertEquals("Empresa", result.getNombreRazonSocial());
        assertNotNull(result.getDocumentoIdentidad());
        assertEquals("RUC", result.getDocumentoIdentidad().tipo());
        assertNotNull(result.getDireccionFacturacion());
        assertEquals("Calle A", result.getDireccionFacturacion().calle());
        assertNotNull(result.getModulo());
        assertEquals("m-1", result.getModulo().getId());
        assertNotNull(result.getSolicitud());
        assertEquals("PENDIENTE", result.getSolicitud().estado());
        assertFalse(result.getFotos().isEmpty());
    }

    private Prospecto crearProspectoDominio(String sfId) {
        return new Prospecto.Builder()
                .id(new ProspectoId("1"))
                .uuid(UUID.randomUUID().toString())
                .salesforceId(sfId)
                .nombreRazonSocial("Empresa SAC")
                .documentoIdentidad(new DocumentoIdentidad("RUC", "12345678901"))
                .contacto(new Contacto("123", "456", "a@a.com", "b@b.com"))
                .direccionFacturacion(new Direccion("Calle 123", "1", null, null, null, "Lima", "Lima", "PE", "15001", null, null, null, null, "Ref"))
                .usarDireccionFacturacion(true)
                .fechaNacimiento(LocalDate.now())
                .creadoEnNitroApp(true)
                .build();
    }
}