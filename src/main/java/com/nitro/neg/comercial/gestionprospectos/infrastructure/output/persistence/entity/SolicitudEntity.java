package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.domain.Persistable;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("public.solicitud")
public class SolicitudEntity implements Persistable<String> {

    @Id
    @Column("id")
    private String id;

    @Column("prospecto__c") private String prospectoId;
    @Column("uuid__c") private String uuid;
    @Column("estado__c") private String estado;
    @Column("tipo__c") private String tipo;
    @Column("record_type_id") private String recordTypeId;
    @Column("enviar_para_aprobacion__c") private Boolean enviarParaAprobacion;
    @Column("created_date") private LocalDateTime createdDate;

    @Transient
    @Builder.Default
    private boolean isNew = false;

    @Override
    public boolean isNew() { return this.isNew; }
}