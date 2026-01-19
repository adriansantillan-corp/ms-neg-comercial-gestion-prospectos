package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("public.solicitud")
public class SolicitudEntity {

    @Id
    @Column("id")
    private String id;

    @Column("prospecto__c")
    private String prospectoId;

    @Column("uuid__c")
    private String uuid;

    @Column("estado__c")
    private String estado;

    @Column("tipo__c")
    private String tipo;

    @Column("record_type_id")
    private String recordTypeId;

    @Column("enviar_para_aprobacion__c")
    private Boolean enviarParaAprobacion;
}