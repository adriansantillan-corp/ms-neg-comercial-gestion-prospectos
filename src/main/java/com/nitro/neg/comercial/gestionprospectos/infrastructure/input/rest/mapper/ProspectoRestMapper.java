package com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.mapper;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Modulo;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Solicitud;
import com.nitro.neg.comercial.gestionprospectos.domain.model.enums.EstadoSolicitud;
import com.nitro.neg.comercial.gestionprospectos.domain.model.enums.TipoDocumento;
import com.nitro.neg.comercial.gestionprospectos.domain.model.enums.TipoSolicitud;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.Contacto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.Direccion;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.DocumentoIdentidad;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.dto.request.ProspectoRequest;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ProspectoRestMapper {

    public Prospecto toDomain(ProspectoRequest req) {
        if (req == null) return null;

        return new Prospecto.Builder()
                .uuid(UUID.randomUUID().toString())

                .nombreRazonSocial(req.getNombreRazonSocial())
                .nombreContacto(req.getNombreContacto())
                .apellidoPaterno(req.getApellidoPaterno())
                .apellidoMaterno(req.getApellidoMaterno())
                .idFiscal(req.getIdFiscal())
                .giro(req.getGiro())
                .subgiro(req.getSubgiro())
                .descripcion(req.getDescripcion())
                .fechaNacimiento(req.getFechaNacimiento())

                .documentoIdentidad(new DocumentoIdentidad(
                        TipoDocumento.fromString(req.getTipoDocumento()).name(),
                        req.getNumeroDocumento()
                ))

                .contacto(new Contacto(
                        req.getTelefono(), req.getTelefonoContacto(),
                        req.getEmail(), req.getEmailContacto()
                ))

                .direccionFacturacion(mapBilling(req))
                .direccionEnvio(mapShipping(req))
                .usarDireccionFacturacion(Boolean.TRUE.equals(req.getUsarDireccionFacturacion()))

                .modulo(mapModulo(req.getModulo()))
                .solicitud(mapSolicitud(req.getSolicitud()))

                .latitud(req.getLatitud())
                .longitud(req.getLongitud())
                .listaPrecios(req.getListaPrecios())
                .recordTypeId(req.getRecordTypeId())
                .paisIso(req.getPaisIso())
                .creadoEnNitroApp(true)
                .build();
    }

    private Direccion mapBilling(ProspectoRequest r) {
        return new Direccion(
                r.getBillingStreet(), r.getBillingNumero(), r.getBillingInterior(),
                r.getBillingLote(), r.getBillingManzana(), r.getBillingCity(),
                r.getBillingState(), r.getBillingCountry(), r.getBillingPostalCode(),
                r.getBillingClasificacion(), r.getBillingDetalleClasificacion(),
                r.getBillingTipoVia(), r.getBillingDetalleVia(), r.getBillingReferencia()
        );
    }

    private Direccion mapShipping(ProspectoRequest r) {
        return new Direccion(
                r.getShippingStreet(), r.getShippingNumero(), r.getShippingInterior(),
                r.getShippingLote(), r.getShippingManzana(), r.getShippingCity(),
                r.getShippingState(), r.getShippingCountry(), r.getShippingPostalCode(),
                r.getShippingClasificacion(), r.getShippingDetalleClasificacion(),
                r.getShippingTipoVia(), r.getShippingDetalleVia(), r.getShippingReferencia()
        );
    }

    private Modulo mapModulo(ProspectoRequest.ModuloDTO dto) {
        if (dto == null) return null;
        return new Modulo(
                null,
                dto.getUuid(),
                dto.getDiasVisita(),
                dto.getIdModuloAsignado(),
                dto.getPeriodoVisita()
        );
    }

    private Solicitud mapSolicitud(ProspectoRequest.SolicitudDTO dto) {
        if (dto == null) return null;
        return new Solicitud(
                null,
                dto.getUuid(),
                EstadoSolicitud.fromString(dto.getEstado()).name(),
                TipoSolicitud.fromString(dto.getTipo()).name(),
                dto.getRecordTypeId(),
                Boolean.TRUE.equals(dto.getEnviarParaAprobacion()),
                true
        );
    }
}