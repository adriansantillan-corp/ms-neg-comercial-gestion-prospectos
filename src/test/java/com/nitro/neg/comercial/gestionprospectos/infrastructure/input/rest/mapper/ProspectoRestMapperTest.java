package com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.mapper;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.dto.request.ProspectoRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class ProspectoRestMapperTest {

    @InjectMocks
    private ProspectoRestMapper mapper;

    @Test
    void toDomain_DeberiaMapearCorrectamente_CuandoRequestEsValido() {

        ProspectoRequest req = new ProspectoRequest();
        req.setNombreRazonSocial("Empresa Test SAC");
        req.setTipoDocumento("RUC");
        req.setNumeroDocumento("20100200300");
        req.setTelefono("999888777");
        req.setBillingStreet("Av. Larco");
        req.setBillingNumero("123");

        ProspectoRequest.ModuloDTO moduloDTO = new ProspectoRequest.ModuloDTO();
        moduloDTO.setUuid("mod-uuid-1");
        req.setModulo(moduloDTO);

        ProspectoRequest.SolicitudDTO solicitudDTO = new ProspectoRequest.SolicitudDTO();
        solicitudDTO.setUuid("sol-uuid-1");
        solicitudDTO.setEstado("PENDIENTE");
        req.setSolicitud(solicitudDTO);

        Prospecto result = mapper.toDomain(req);

        assertNotNull(result);
        assertNotNull(result.getId());
        assertNotNull(result.getUuid());
        assertEquals("Empresa Test SAC", result.getNombreRazonSocial());

        assertNotNull(result.getDocumentoIdentidad());
        assertEquals("RUC", result.getDocumentoIdentidad().tipo());
        assertEquals("20100200300", result.getDocumentoIdentidad().numero());

        assertNotNull(result.getContacto());
        assertEquals("999888777", result.getContacto().telefono());

        assertNotNull(result.getDireccionFacturacion());
        assertEquals("Av. Larco", result.getDireccionFacturacion().calle());

        assertNotNull(result.getModulo());
        assertEquals("mod-uuid-1", result.getModulo().uuid());

        assertNotNull(result.getSolicitud());
        assertEquals("sol-uuid-1", result.getSolicitud().uuid());
        assertEquals("PENDIENTE", result.getSolicitud().estado());
    }

    @Test
    void toDomain_DeberiaRetornarNull_CuandoRequestEsNull() {
        assertNull(mapper.toDomain(null));
    }
}