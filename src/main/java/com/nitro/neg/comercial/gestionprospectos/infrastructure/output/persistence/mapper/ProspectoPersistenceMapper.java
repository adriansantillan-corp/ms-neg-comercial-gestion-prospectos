package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.mapper;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Foto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Modulo;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Solicitud;
import com.nitro.neg.comercial.gestionprospectos.domain.model.enums.EstadoSolicitud;
import com.nitro.neg.comercial.gestionprospectos.domain.model.enums.TipoDocumento;
import com.nitro.neg.comercial.gestionprospectos.domain.model.enums.TipoSolicitud;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.Contacto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.Direccion;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.DocumentoIdentidad;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.ProspectoId;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.FotoEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.ModuloEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.ProspectoEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.SolicitudEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class ProspectoPersistenceMapper {

    public ProspectoEntity toEntity(Prospecto d) {
        if (d == null) return null;

        boolean esNuevo = d.getSalesforceId() == null;

        ProspectoEntity.ProspectoEntityBuilder builder = ProspectoEntity.builder();
        builder.isNew(esNuevo);

        builder.id(d.getId().value());
        builder.uuid(d.getUuid());
        builder.salesforceId(d.getSalesforceId());

        builder.nombreRazonSocial(d.getNombreRazonSocial());
        builder.nombreContacto(d.getNombreContacto());
        builder.apellidoPaterno(d.getApellidoPaterno());
        builder.apellidoMaterno(d.getApellidoMaterno());
        builder.idFiscal(d.getIdFiscal());
        builder.giro(d.getGiro());
        builder.subgiro(d.getSubgiro());
        builder.descripcion(d.getDescripcion());
        builder.fechaNacimiento(d.getFechaNacimiento());

        if (d.getDocumentoIdentidad() != null) {
            builder.documentoIdentificacion(d.getDocumentoIdentidad().tipo());
            builder.numeroDocumento(d.getDocumentoIdentidad().numero());
        }

        if (d.getContacto() != null) {
            builder.telefono(d.getContacto().telefono());
            builder.telefonoContacto(d.getContacto().telefonoContacto());
            builder.correoElectronico(d.getContacto().email());
            builder.correoPersonaContacto(d.getContacto().emailContacto());
        }

        mapBillingAddress(builder, d.getDireccionFacturacion());
        mapShippingAddress(builder, d.getDireccionEnvio());

        builder.latitud(d.getLatitud());
        builder.longitud(d.getLongitud());
        builder.listaDePrecios(d.getListaPrecios());
        builder.recordTypeId(d.getRecordTypeId());
        builder.pais(d.getPaisIso());
        builder.creadoEnNitroApp(d.isCreadoEnNitroApp());
        builder.usarDireccionFacturacion(d.isUsarDireccionFacturacion());
        builder.canalComercial(d.getCanalComercial());

        return builder.build();
    }

    public ModuloEntity toModuloEntity(Modulo m, String prospectoId, boolean esNuevo) {
        if (m == null) return null;
        return ModuloEntity.builder()
                .isNew(esNuevo)
                .id(m.getId() != null ? m.getId() : UUID.randomUUID().toString())
                .uuid(m.uuid())
                .prospectoId(prospectoId)
                .diasVisita(m.diasVisita())
                .periodoVisita(m.periodoVisita())
                .idModuloAsignado(m.idModuloAsignado())
                .build();
    }

    public SolicitudEntity toSolicitudEntity(Solicitud s, String prospectoId, boolean esNuevo) {
        if (s == null) return null;
        return SolicitudEntity.builder()
                .isNew(esNuevo)
                .id(s.getId() != null ? s.getId() : UUID.randomUUID().toString())
                .uuid(s.uuid())
                .prospectoId(prospectoId)
                .estado(s.estado())
                .tipo(s.tipo())
                .recordTypeId(s.recordTypeId())
                .enviarParaAprobacion(s.enviarParaAprobacion())
                .build();
    }

    public FotoEntity toFotoEntity(Foto f, String prospectoId, boolean esNuevo) {
        if (f == null) return null;
        return FotoEntity.builder()
                .isNew(esNuevo)
                .id(f.id() != null ? f.id() : UUID.randomUUID().toString())
                .prospectoId(prospectoId)
                .url(f.url())
                .path(f.path())
                .nombreArchivo(f.nombreArchivo())
                .build();
    }


    public Prospecto toDomain(ProspectoEntity e, ModuloEntity moduloEntity, SolicitudEntity solicitudEntity, List<FotoEntity> fotoEntities) {
        if (e == null) return null;

        String tipoDoc = e.getDocumentoIdentificacion();
        if (tipoDoc != null && !tipoDoc.isBlank()) {
            TipoDocumento tipoEnum = TipoDocumento.fromString(tipoDoc);
            tipoDoc = tipoEnum != null ? tipoEnum.name() : "OTRO";
        }

        return new Prospecto.Builder()
                .id(new ProspectoId(e.getId()))
                .uuid(e.getUuid())
                .salesforceId(e.getSalesforceId())
                .nombreRazonSocial(e.getNombreRazonSocial())
                .nombreContacto(e.getNombreContacto())
                .apellidoPaterno(e.getApellidoPaterno())
                .apellidoMaterno(e.getApellidoMaterno())
                .idFiscal(e.getIdFiscal())
                .documentoIdentidad(new DocumentoIdentidad(tipoDoc, e.getNumeroDocumento()))
                .contacto(new Contacto(
                        e.getTelefono(), e.getTelefonoContacto(),
                        e.getCorreoElectronico(), e.getCorreoPersonaContacto()))
                .direccionFacturacion(reconstructBilling(e))
                .direccionEnvio(reconstructShipping(e))
                .giro(e.getGiro())
                .subgiro(e.getSubgiro())
                .descripcion(e.getDescripcion())
                .fechaNacimiento(e.getFechaNacimiento())
                .latitud(e.getLatitud())
                .longitud(e.getLongitud())
                .listaPrecios(e.getListaDePrecios())
                .recordTypeId(e.getRecordTypeId())
                .paisIso(e.getPais())
                .creadoEnNitroApp(Boolean.TRUE.equals(e.getCreadoEnNitroApp()))
                .usarDireccionFacturacion(Boolean.TRUE.equals(e.getUsarDireccionFacturacion()))
                .canalComercial(e.getCanalComercial())

                .modulo(toDomainModulo(moduloEntity))
                .solicitud(toDomainSolicitud(solicitudEntity))
                .fotos(toDomainFotos(fotoEntities))
                .build();
    }

    private Modulo toDomainModulo(ModuloEntity e) {
        if (e == null) return null;
        return new Modulo(e.getId(), e.getUuid(), e.getDiasVisita(), e.getIdModuloAsignado(), e.getPeriodoVisita());
    }

    private Solicitud toDomainSolicitud(SolicitudEntity e) {
        if (e == null) return null;
        String estado = e.getEstado() != null ? EstadoSolicitud.fromString(e.getEstado()).name() : "PENDIENTE";
        String tipo = e.getTipo() != null ? TipoSolicitud.fromString(e.getTipo()).name() : "ALTA";
        return new Solicitud(e.getId(), e.getUuid(), estado, tipo, e.getRecordTypeId(), Boolean.TRUE.equals(e.getEnviarParaAprobacion()), true);
    }

    private List<Foto> toDomainFotos(List<FotoEntity> entities) {
        if (entities == null || entities.isEmpty()) return Collections.emptyList();
        return entities.stream()
                .map(e -> new Foto(e.getId(), e.getUrl(), e.getPath(), e.getNombreArchivo()))
                .collect(Collectors.toList());
    }

    private void mapBillingAddress(ProspectoEntity.ProspectoEntityBuilder b, Direccion d) {
        if (d == null) return;
        b.billingStreet(d.calle()); b.billingNumero(d.numero()); b.billingInterior(d.interior());
        b.billingLote(d.lote()); b.billingManzana(d.manzana()); b.billingCity(d.ciudad());
        b.billingState(d.estado()); b.billingCountry(d.pais()); b.billingPostalCode(d.codigoPostal());
        b.billingClasificacion(d.clasificacion()); b.billingDetalleClasificacion(d.detalleClasificacion());
        b.billingTipoVia(d.tipoVia()); b.billingDetalleVia(d.detalleVia()); b.billingReferencia(d.referencia());
        b.billingAddressFull(d.calle() + " " + (d.numero() != null ? d.numero() : ""));
    }

    private void mapShippingAddress(ProspectoEntity.ProspectoEntityBuilder b, Direccion d) {
        if (d == null) return;
        b.shippingStreet(d.calle()); b.shippingNumero(d.numero()); b.shippingInterior(d.interior());
        b.shippingLote(d.lote()); b.shippingManzana(d.manzana()); b.shippingCity(d.ciudad());
        b.shippingState(d.estado()); b.shippingCountry(d.pais()); b.shippingPostalCode(d.codigoPostal());
        b.shippingClasificacion(d.clasificacion()); b.shippingDetalleClasificacion(d.detalleClasificacion());
        b.shippingTipoVia(d.tipoVia()); b.shippingDetalleVia(d.detalleVia()); b.shippingReferencia(d.referencia());
        b.shippingAddressFull(d.calle() + " " + (d.numero() != null ? d.numero() : ""));
    }

    private Direccion reconstructBilling(ProspectoEntity e) {
        return new Direccion(
                e.getBillingStreet(), e.getBillingNumero(), e.getBillingInterior(),
                e.getBillingLote(), e.getBillingManzana(), e.getBillingCity(),
                e.getBillingState(), e.getBillingCountry(), e.getBillingPostalCode(),
                e.getBillingClasificacion(), e.getBillingDetalleClasificacion(),
                e.getBillingTipoVia(), e.getBillingDetalleVia(), e.getBillingReferencia()
        );
    }

    private Direccion reconstructShipping(ProspectoEntity e) {
        return new Direccion(
                e.getShippingStreet(), e.getShippingNumero(), e.getShippingInterior(),
                e.getShippingLote(), e.getShippingManzana(), e.getShippingCity(),
                e.getShippingState(), e.getShippingCountry(), e.getShippingPostalCode(),
                e.getShippingClasificacion(), e.getShippingDetalleClasificacion(),
                e.getShippingTipoVia(), e.getShippingDetalleVia(), e.getShippingReferencia()
        );
    }
}