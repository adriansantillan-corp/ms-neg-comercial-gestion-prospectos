package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.external.adapter;

import com.nitro.neg.comercial.gestionprospectos.domain.exception.DomainException;
import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Modulo;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Solicitud;
import com.nitro.neg.comercial.gestionprospectos.domain.port.output.SalesforceIntegrationPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty; // <--- 1. IMPORTAR ESTO
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@ConditionalOnProperty(name = "salesforce.integration.enabled", havingValue = "true")
public class SalesforceWebClientAdapter implements SalesforceIntegrationPort {

    private final WebClient webClient;

    @Value("${salesforce.api.url}")
    private String sfApiUrl;

    @Value("${salesforce.api.token}")
    private String sfToken;

    @Override
    public Mono<String> enviarProspecto(Prospecto prospecto) {
        Map<String, Object> payload = mapToSalesforcePayload(prospecto);

        log.info("🚀 [REAL] Enviando prospecto a Salesforce. UUID: {}", prospecto.getUuid());

        return webClient.post()
                .uri(sfApiUrl + "/services/apexrest/ProspectoApp/")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + sfToken)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> {
                    boolean success = Boolean.TRUE.equals(response.get("success"));
                    if (!success) {
                        throw new DomainException("Salesforce rechazó el prospecto: " + response.get("message"));
                    }
                    return (String) response.get("id");
                })
                .onErrorResume(e -> {
                    log.error("Error de comunicación con Salesforce: {}", e.getMessage());
                    return Mono.error(new DomainException("Error al sincronizar con Salesforce", e));
                });
    }


    private Map<String, Object> mapToSalesforcePayload(Prospecto p) {
        Map<String, Object> map = new HashMap<>();

        map.put("nombre_razon_social__c", p.getNombreRazonSocial());
        map.put("nombre_contacto__c", p.getNombreContacto());
        map.put("apellido_paterno__c", p.getApellidoPaterno());
        map.put("apellido_materno__c", p.getApellidoMaterno());
        map.put("uuid__c", p.getUuid());
        map.put("id_fiscal__c", p.getIdFiscal());
        map.put("giro__c", p.getGiro());
        map.put("subgiro__c", p.getSubgiro());
        map.put("descripcion__c", p.getDescripcion());
        map.put("fecha_de_nacimiento__c", p.getFechaNacimiento());
        map.put("lista_de_precios__c", p.getListaPrecios());
        map.put("record_type_id", p.getRecordTypeId());
        map.put("pais__c", p.getPaisIso());
        map.put("creado_en_nitro_app__c", p.isCreadoEnNitroApp());
        map.put("usar_direccion_facturacion__c", p.isUsarDireccionFacturacion());
        map.put("latitud__c", p.getLatitud());
        map.put("longitud__c", p.getLongitud());

        if (p.getDocumentoIdentidad() != null) {
            map.put("documento_identificacion__c", p.getDocumentoIdentidad().tipo());
            map.put("numero_documento__c", p.getDocumentoIdentidad().numero());
        }

        if (p.getContacto() != null) {
            map.put("telefono__c", p.getContacto().telefono());
            map.put("telefono_contacto__c", p.getContacto().telefonoContacto());
            map.put("correo_electronico__c", p.getContacto().email());
            map.put("correo_persona_contacto__c", p.getContacto().emailContacto());
        }

        if (p.getDireccionFacturacion() != null) {
            var dir = p.getDireccionFacturacion();
            map.put("billing_street__c", dir.calle());
            map.put("billing_numero__c", dir.numero());
            map.put("billing_interior__c", dir.interior());
            map.put("billing_lote__c", dir.lote());
            map.put("billing_manzana__c", dir.manzana());
            map.put("billing_city__c", dir.ciudad());
            map.put("billing_state__c", dir.estado());
            map.put("billing_country__c", dir.pais());
            map.put("billing_postal_code__c", dir.codigoPostal());
            map.put("billing_clasificacion__c", dir.clasificacion());
            map.put("billing_detalle_clasificacion__c", dir.detalleClasificacion());
            map.put("billing_tipo_via__c", dir.tipoVia());
            map.put("billing_detalle_via__c", dir.detalleVia());
            map.put("billing_referencia__c", dir.referencia());
        }

        if (p.getDireccionEnvio() != null) {
            var dir = p.getDireccionEnvio();
            map.put("shipping_street__c", dir.calle());
            map.put("shipping_numero__c", dir.numero());
            map.put("shipping_interior__c", dir.interior());
            map.put("shipping_lote__c", dir.lote());
            map.put("shipping_manzana__c", dir.manzana());
            map.put("shipping_city__c", dir.ciudad());
            map.put("shipping_state__c", dir.estado());
            map.put("shipping_country__c", dir.pais());
            map.put("shipping_postal_code__c", dir.codigoPostal());
            map.put("shipping_clasificacion__c", dir.clasificacion());
            map.put("shipping_detalle_clasificacion__c", dir.detalleClasificacion());
            map.put("shipping_tipo_via__c", dir.tipoVia());
            map.put("shipping_detalle_via__c", dir.detalleVia());
            map.put("shipping_referencia__c", dir.referencia());
        }

        if (p.getModulo() != null) {
            map.put("modulo", mapModulo(p.getModulo()));
        }
        if (p.getSolicitud() != null) {
            map.put("solicitud", mapSolicitud(p.getSolicitud()));
        }

        return map;
    }

    private Map<String, Object> mapModulo(Modulo m) {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", m.uuid());
        map.put("dias_visita", m.diasVisita());
        map.put("id_modulo_asignado", m.idModuloAsignado());
        map.put("periodo_visita", m.periodoVisita());
        return map;
    }

    private Map<String, Object> mapSolicitud(Solicitud s) {
        Map<String, Object> map = new HashMap<>();
        map.put("uuid", s.uuid());
        map.put("estado", s.estado());
        map.put("tipo", s.tipo());
        map.put("record_type_id", s.recordTypeId());
        map.put("enviar_para_aprobacion", s.enviarParaAprobacion());
        return map;
    }
}