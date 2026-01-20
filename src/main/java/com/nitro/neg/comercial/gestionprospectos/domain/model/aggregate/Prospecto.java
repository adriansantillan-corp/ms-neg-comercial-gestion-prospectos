package com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate;

import com.nitro.neg.comercial.gestionprospectos.domain.exception.DomainException;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Foto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Modulo;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Solicitud;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.Contacto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.Direccion;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.DocumentoIdentidad;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.ProspectoId;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class Prospecto {

    private final ProspectoId id;
    private String salesforceId;
    private final String uuid;

    private final String nombreRazonSocial;
    private final String nombreContacto;
    private final String apellidoPaterno;
    private final String apellidoMaterno;
    private final DocumentoIdentidad documentoIdentidad;
    private final String idFiscal;
    private final String giro;
    private final String subgiro;
    private final String descripcion;
    private final LocalDate fechaNacimiento;

    private final Direccion direccionEnvio;
    private final Direccion direccionFacturacion;
    private final boolean usarDireccionFacturacion;

    private String canalComercial;

    private final Contacto contacto;

    private final Modulo modulo;
    private final Solicitud solicitud;
    private final List<Foto> fotos;

    private final String latitud;
    private final String longitud;
    private final String listaPrecios;
    private final String recordTypeId;
    private final String paisIso;
    private boolean creadoEnNitroApp;

    private Prospecto(Builder builder) {
        this.id = builder.id;
        this.salesforceId = builder.salesforceId;
        this.uuid = builder.uuid;
        this.nombreRazonSocial = builder.nombreRazonSocial;
        this.nombreContacto = builder.nombreContacto;
        this.apellidoPaterno = builder.apellidoPaterno;
        this.apellidoMaterno = builder.apellidoMaterno;
        this.documentoIdentidad = builder.documentoIdentidad;
        this.idFiscal = builder.idFiscal;
        this.giro = builder.giro;
        this.subgiro = builder.subgiro;
        this.descripcion = builder.descripcion;
        this.fechaNacimiento = builder.fechaNacimiento;
        this.direccionEnvio = builder.direccionEnvio;
        this.direccionFacturacion = builder.direccionFacturacion;
        this.usarDireccionFacturacion = builder.usarDireccionFacturacion;
        this.contacto = builder.contacto;
        this.modulo = builder.modulo;
        this.solicitud = builder.solicitud;
        this.fotos = builder.fotos;
        this.latitud = builder.latitud;
        this.longitud = builder.longitud;
        this.listaPrecios = builder.listaPrecios;
        this.recordTypeId = builder.recordTypeId;
        this.paisIso = builder.paisIso;
        this.creadoEnNitroApp = builder.creadoEnNitroApp;
    }

    public void confirmarSincronizacion(String sfId) {
        if (sfId == null || sfId.trim().isEmpty()) {
            throw new DomainException("El ID de Salesforce es inválido para confirmar la sincronización.");
        }
        this.salesforceId = sfId;
    }

    public void agregarFoto(Foto foto) {
        if (foto == null) {
            throw new DomainException("No se puede agregar una foto nula al prospecto.");
        }
        this.fotos.add(foto);
    }

    public ProspectoId getId() { return id; }
    public String getSalesforceId() { return salesforceId; }
    public String getUuid() { return uuid; }
    public String getNombreRazonSocial() { return nombreRazonSocial; }
    public String getNombreContacto() { return nombreContacto; }
    public String getApellidoPaterno() { return apellidoPaterno; }
    public String getApellidoMaterno() { return apellidoMaterno; }
    public DocumentoIdentidad getDocumentoIdentidad() { return documentoIdentidad; }
    public String getIdFiscal() { return idFiscal; }
    public String getGiro() { return giro; }
    public String getSubgiro() { return subgiro; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaNacimiento() { return fechaNacimiento; }
    public Direccion getDireccionEnvio() { return direccionEnvio; }
    public Direccion getDireccionFacturacion() { return direccionFacturacion; }
    public boolean isUsarDireccionFacturacion() { return usarDireccionFacturacion; }
    public Contacto getContacto() { return contacto; }
    public Modulo getModulo() { return modulo; }
    public Solicitud getSolicitud() { return solicitud; }
    public String getLatitud() { return latitud; }
    public String getLongitud() { return longitud; }
    public String getListaPrecios() { return listaPrecios; }
    public String getRecordTypeId() { return recordTypeId; }
    public String getPaisIso() { return paisIso; }
    public boolean isCreadoEnNitroApp() { return creadoEnNitroApp; }
    public String getCanalComercial() { return canalComercial; }

    public List<Foto> getFotos() {
        return Collections.unmodifiableList(fotos);
    }

    public static class Builder {
        private ProspectoId id;
        private String salesforceId;
        private String uuid;
        private String nombreRazonSocial;
        private String nombreContacto;
        private String apellidoPaterno;
        private String apellidoMaterno;
        private DocumentoIdentidad documentoIdentidad;
        private String idFiscal;
        private String giro;
        private String subgiro;
        private String descripcion;
        private LocalDate fechaNacimiento;
        private Direccion direccionEnvio;
        private Direccion direccionFacturacion;
        private boolean usarDireccionFacturacion;
        private Contacto contacto;
        private Modulo modulo;
        private Solicitud solicitud;
        private List<Foto> fotos;
        private String latitud;
        private String longitud;
        private String listaPrecios;
        private String recordTypeId;
        private String paisIso;
        private boolean creadoEnNitroApp;
        private String canalComercial;
        public Builder canalComercial(String val) { this.canalComercial = val; return this; }

        public Builder() {
            this.id = ProspectoId.random();
            this.uuid = UUID.randomUUID().toString();
            this.fotos = new ArrayList<>();
            this.creadoEnNitroApp = true;
        }

        public Builder id(ProspectoId id) { this.id = id; return this; }
        public Builder id(String idStr) { this.id = new ProspectoId(idStr); return this; }
        public Builder salesforceId(String val) { this.salesforceId = val; return this; }
        public Builder uuid(String val) { this.uuid = val; return this; }
        public Builder nombreRazonSocial(String val) { this.nombreRazonSocial = val; return this; }
        public Builder nombreContacto(String val) { this.nombreContacto = val; return this; }
        public Builder apellidoPaterno(String val) { this.apellidoPaterno = val; return this; }
        public Builder apellidoMaterno(String val) { this.apellidoMaterno = val; return this; }
        public Builder documentoIdentidad(DocumentoIdentidad val) { this.documentoIdentidad = val; return this; }
        public Builder idFiscal(String val) { this.idFiscal = val; return this; }
        public Builder giro(String val) { this.giro = val; return this; }
        public Builder subgiro(String val) { this.subgiro = val; return this; }
        public Builder descripcion(String val) { this.descripcion = val; return this; }
        public Builder fechaNacimiento(LocalDate val) { this.fechaNacimiento = val; return this; }
        public Builder direccionEnvio(Direccion val) { this.direccionEnvio = val; return this; }
        public Builder direccionFacturacion(Direccion val) { this.direccionFacturacion = val; return this; }
        public Builder usarDireccionFacturacion(boolean val) { this.usarDireccionFacturacion = val; return this; }
        public Builder contacto(Contacto val) { this.contacto = val; return this; }
        public Builder modulo(Modulo val) { this.modulo = val; return this; }
        public Builder solicitud(Solicitud val) { this.solicitud = val; return this; }

        public Builder fotos(List<Foto> fotos) {
            if (fotos != null) this.fotos = new ArrayList<>(fotos);
            return this;
        }

        public Builder agregarFoto(Foto foto) {
            this.fotos.add(foto);
            return this;
        }

        public Builder latitud(String val) { this.latitud = val; return this; }
        public Builder longitud(String val) { this.longitud = val; return this; }
        public Builder listaPrecios(String val) { this.listaPrecios = val; return this; }
        public Builder recordTypeId(String val) { this.recordTypeId = val; return this; }
        public Builder paisIso(String val) { this.paisIso = val; return this; }
        public Builder creadoEnNitroApp(boolean val) { this.creadoEnNitroApp = val; return this; }

        public Prospecto build() {
            validarEstadoInvariante();
            return new Prospecto(this);
        }

        private void validarEstadoInvariante() {
            if (nombreRazonSocial == null || nombreRazonSocial.isBlank()) {
                throw new DomainException("El prospecto debe tener una Razón Social válida.");
            }
            if (documentoIdentidad == null) {
                throw new DomainException("El Documento de Identidad es obligatorio para crear un prospecto.");
            }
        }
    }
}