package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("public.prospecto")
public class ProspectoEntity {

    @Id
    @Column("id")
    private String id;

    @Column("uuid__c")
    private String uuid;

    @Column("salesforce_id__c")
    private String salesforceId;

    @Column("owner_id")
    private String ownerId;

    @Column("is_deleted")
    private Boolean isDeleted;

    @Column("name")
    private String name;

    @Column("created_date")
    private LocalDateTime createdDate;

    @Column("created_by_id")
    private String createdById;

    @Column("last_modified_date")
    private LocalDateTime lastModifiedDate;

    @Column("system_modstamp")
    private LocalDateTime systemModstamp;

    @Column("nombre_razon_social__c")
    private String nombreRazonSocial;

    @Column("nombre_contacto__c")
    private String nombreContacto;

    @Column("apellido_paterno__c")
    private String apellidoPaterno;

    @Column("apellido_materno__c")
    private String apellidoMaterno;

    @Column("documento_identificacion__c")
    private String documentoIdentificacion;

    @Column("numero_documento__c")
    private String numeroDocumento;

    @Column("id_fiscal__c")
    private String idFiscal;

    @Column("correo_electronico__c")
    private String correoElectronico;

    @Column("correo_persona_contacto__c")
    private String correoPersonaContacto;

    @Column("telefono__c")
    private String telefono;

    @Column("telefono_contacto__c")
    private String telefonoContacto;

    @Column("telefono_alternativo_1__c")
    private String telefonoAlternativo1;

    @Column("billing_address__c") private String billingAddressFull;
    @Column("billing_street__c") private String billingStreet;
    @Column("billing_numero__c") private String billingNumero;
    @Column("billing_interior__c") private String billingInterior;
    @Column("billing_lote__c") private String billingLote;
    @Column("billing_manzana__c") private String billingManzana;
    @Column("billing_city__c") private String billingCity;
    @Column("billing_state__c") private String billingState;
    @Column("billing_country__c") private String billingCountry;
    @Column("billing_postal_code__c") private String billingPostalCode;
    @Column("billing_clasificacion__c") private String billingClasificacion;
    @Column("billing_detalle_clasificacion__c") private String billingDetalleClasificacion;
    @Column("billing_tipo_via__c") private String billingTipoVia;
    @Column("billing_detalle_via__c") private String billingDetalleVia;
    @Column("billing_referencia__c") private String billingReferencia;

    @Column("shipping_address__c") private String shippingAddressFull;
    @Column("shipping_street__c") private String shippingStreet;
    @Column("shipping_numero__c") private String shippingNumero;
    @Column("shipping_interior__c") private String shippingInterior;
    @Column("shipping_lote__c") private String shippingLote;
    @Column("shipping_manzana__c") private String shippingManzana;
    @Column("shipping_city__c") private String shippingCity;
    @Column("shipping_state__c") private String shippingState;
    @Column("shipping_country__c") private String shippingCountry;
    @Column("shipping_postal_code__c") private String shippingPostalCode;
    @Column("shipping_clasificacion__c") private String shippingClasificacion;
    @Column("shipping_detalle_clasificacion__c") private String shippingDetalleClasificacion;
    @Column("shipping_tipo_via__c") private String shippingTipoVia;
    @Column("shipping_detalle_via__c") private String shippingDetalleVia;
    @Column("shipping_referencia__c") private String shippingReferencia;

    @Column("giro__c") private String giro;
    @Column("subgiro__c") private String subgiro;
    @Column("descripcion__c") private String descripcion;
    @Column("fecha_de_nacimiento__c") private LocalDate fechaNacimiento;
    @Column("latitud__c") private String latitud;
    @Column("longitud__c") private String longitud;
    @Column("lista_de_precios__c") private String listaDePrecios;
    @Column("record_type_id") private String recordTypeId;
    @Column("pais__c") private String pais;
    @Column("creado_en_nitro_app__c") private Boolean creadoEnNitroApp;
    @Column("usar_direccion_facturacion__c") private Boolean usarDireccionFacturacion;

    @Column("canal_comercial__c")
    private String canalComercial;
}