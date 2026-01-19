package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.mapper;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.DocumentoIdentidad;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.ProspectoId;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.ModuloEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.ProspectoEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.SolicitudEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProspectoPersistenceMapperTest {

    @InjectMocks
    private ProspectoPersistenceMapper mapper;

    @Test
    void toEntity_DeberiaMapearTodo_CuandoDominioEsCompleto() {
        Prospecto dominio = new Prospecto.Builder()
                .id(new ProspectoId("123"))
                .uuid("uuid-123")
                .salesforceId("SF-001")
                .nombreRazonSocial("Test Corp")
                .documentoIdentidad(new DocumentoIdentidad("RUC", "20555555551"))
                .build();

        ProspectoEntity entity = mapper.toEntity(dominio);

        assertNotNull(entity);
        assertEquals("123", entity.getId());
        assertEquals("SF-001", entity.getSalesforceId());
        assertEquals("Test Corp", entity.getNombreRazonSocial());
        assertEquals("RUC", entity.getDocumentoIdentificacion());
        assertEquals("20555555551", entity.getNumeroDocumento());
    }

    @Test
    void toDomain_DeberiaReconstruirAgregado() {
        ProspectoEntity entity = ProspectoEntity.builder()
                .id("123")
                .uuid("uuid-123")
                .documentoIdentificacion("DNI")
                .numeroDocumento("44444444")
                .nombreRazonSocial("Juan Perez")
                .creadoEnNitroApp(true)
                .build();

        ModuloEntity mod = ModuloEntity.builder().uuid("mod-1").build();
        SolicitudEntity sol = SolicitudEntity.builder().uuid("sol-1").estado("APROBADO").build();

        Prospecto result = mapper.toDomain(entity, mod, sol, Collections.emptyList());

        assertNotNull(result);
        assertEquals("Juan Perez", result.getNombreRazonSocial());
        assertEquals("DNI", result.getDocumentoIdentidad().tipo());
        assertTrue(result.isCreadoEnNitroApp());

        assertNotNull(result.getModulo());
        assertEquals("mod-1", result.getModulo().uuid());

        assertNotNull(result.getSolicitud());
        assertEquals("APROBADO", result.getSolicitud().estado());
    }
}