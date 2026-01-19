package com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Schema(description = "Solicitud de creación de prospecto. Mapea la estructura JSON 'snake_case__c' de la App móvil/Salesforce.")
public class ProspectoRequest {

    @Schema(description = "Razón social o Nombre del negocio", example = "Bodegas Unidas SAC", requiredMode = Schema.RequiredMode.REQUIRED)
    @JsonProperty("nombre_razon_social__c")
    private String nombreRazonSocial;

    @Schema(description = "Nombre del contacto principal", example = "Juan")
    @JsonProperty("nombre_contacto__c")
    private String nombreContacto;

    @Schema(description = "Apellido paterno del contacto", example = "Perez")
    @JsonProperty("apellido_paterno__c")
    private String apellidoPaterno;

    @Schema(description = "Apellido materno del contacto", example = "Gomez")
    @JsonProperty("apellido_materno__c")
    private String apellidoMaterno;

    @Schema(description = "Tipo de documento (DNI, RUC, CE)", example = "RUC")
    @JsonProperty("documento_identificacion__c")
    private String tipoDocumento;

    @Schema(description = "Número de documento", example = "20100100101")
    @JsonProperty("numero_documento__c")
    private String numeroDocumento;

    @Schema(description = "Identificador fiscal (RUC) específico", example = "20100100101")
    @JsonProperty("id_fiscal__c")
    private String idFiscal;

    @Schema(description = "Giro del negocio", example = "Abarrotes")
    @JsonProperty("giro__c")
    private String giro;

    @Schema(description = "Subgiro del negocio", example = "Bodega")
    @JsonProperty("subgiro__c")
    private String subgiro;

    @Schema(description = "Descripción adicional del prospecto", example = "Cliente potencial zona norte")
    @JsonProperty("descripcion__c")
    private String descripcion;

    @Schema(description = "Fecha de nacimiento del contacto", example = "1990-05-15")
    @JsonProperty("fecha_de_nacimiento__c")
    private LocalDate fechaNacimiento;

    @Schema(description = "Teléfono principal del negocio", example = "015551234")
    @JsonProperty("telefono__c")
    private String telefono;

    @Schema(description = "Teléfono celular de contacto", example = "999888777")
    @JsonProperty("telefono_contacto__c")
    private String telefonoContacto;

    @Schema(description = "Correo electrónico principal", example = "ventas@bodega.com")
    @JsonProperty("correo_electronico__c")
    private String email;

    @Schema(description = "Correo personal del contacto", example = "juan.perez@gmail.com")
    @JsonProperty("correo_persona_contacto__c")
    private String emailContacto;

    @Schema(description = "Calle de facturación")
    @JsonProperty("billing_street__c") private String billingStreet;
    @JsonProperty("billing_numero__c") private String billingNumero;
    @JsonProperty("billing_interior__c") private String billingInterior;
    @JsonProperty("billing_lote__c") private String billingLote;
    @JsonProperty("billing_manzana__c") private String billingManzana;
    @JsonProperty("billing_city__c") private String billingCity;
    @JsonProperty("billing_state__c") private String billingState;
    @JsonProperty("billing_country__c") private String billingCountry;
    @JsonProperty("billing_postal_code__c") private String billingPostalCode;
    @JsonProperty("billing_clasificacion__c") private String billingClasificacion;
    @JsonProperty("billing_detalle_clasificacion__c") private String billingDetalleClasificacion;
    @JsonProperty("billing_tipo_via__c") private String billingTipoVia;
    @JsonProperty("billing_detalle_via__c") private String billingDetalleVia;
    @JsonProperty("billing_referencia__c") private String billingReferencia;

    @Schema(description = "Calle de envío")
    @JsonProperty("shipping_street__c") private String shippingStreet;
    @JsonProperty("shipping_numero__c") private String shippingNumero;
    @JsonProperty("shipping_interior__c") private String shippingInterior;
    @JsonProperty("shipping_lote__c") private String shippingLote;
    @JsonProperty("shipping_manzana__c") private String shippingManzana;
    @JsonProperty("shipping_city__c") private String shippingCity;
    @JsonProperty("shipping_state__c") private String shippingState;
    @JsonProperty("shipping_country__c") private String shippingCountry;
    @JsonProperty("shipping_postal_code__c") private String shippingPostalCode;
    @JsonProperty("shipping_clasificacion__c") private String shippingClasificacion;
    @JsonProperty("shipping_detalle_clasificacion__c") private String shippingDetalleClasificacion;
    @JsonProperty("shipping_tipo_via__c") private String shippingTipoVia;
    @JsonProperty("shipping_detalle_via__c") private String shippingDetalleVia;
    @JsonProperty("shipping_referencia__c") private String shippingReferencia;

    @Schema(description = "Indica si se debe usar la dirección de facturación para envíos", example = "true")
    @JsonProperty("usar_direccion_facturacion__c")
    private Boolean usarDireccionFacturacion;

    @Schema(description = "Objeto con configuración de visitas y logística")
    @JsonProperty("modulo")
    private ModuloDTO modulo;

    @Schema(description = "Objeto con información del flujo de aprobación")
    @JsonProperty("solicitud")
    private SolicitudDTO solicitud;

    @JsonProperty("latitud__c") private String latitud;
    @JsonProperty("longitud__c") private String longitud;
    @JsonProperty("lista_de_precios__c") private String listaPrecios;
    @JsonProperty("record_type_id") private String recordTypeId;
    @JsonProperty("pais__c") private String paisIso;

    @Data @NoArgsConstructor
    public static class ModuloDTO {
        @Schema(description = "UUID del módulo para sincronización", example = "550e8400-e29b-41d4-a716-446655440000")
        private String uuid;

        @Schema(description = "Días de visita separados por ;", example = "LUN;MAR")
        @JsonProperty("dias_visita")
        private String diasVisita;

        @JsonProperty("id_modulo_asignado")
        private String idModuloAsignado;

        @JsonProperty("periodo_visita")
        private String periodoVisita;
    }

    @Data @NoArgsConstructor
    public static class SolicitudDTO {
        @Schema(description = "UUID de la solicitud", example = "770e8400-e29b-41d4-a716-446655440011")
        private String uuid;

        @Schema(description = "Estado de la solicitud", example = "Pendiente")
        private String estado;

        @Schema(description = "Tipo de solicitud", example = "Alta")
        private String tipo;

        @JsonProperty("record_type_id")
        private String recordTypeId;

        @JsonProperty("enviar_para_aprobacion")
        private Boolean enviarParaAprobacion;
    }
}