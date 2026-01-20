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
@Table("public.modulo_de_prospecto")
public class ModuloEntity implements Persistable<String> {

    @Id
    @Column("id")
    private String id;

    @Column("prospecto__c") private String prospectoId;
    @Column("uuid__c") private String uuid;
    @Column("dias_visita__c") private String diasVisita;
    @Column("periodo_de_visita__c") private String periodoVisita;
    @Column("modulo__c") private String idModuloAsignado;
    @Column("created_date") private LocalDateTime createdDate;
    @Column("is_deleted") private Boolean isDeleted;

    @Transient
    @Builder.Default
    private boolean isNew = false;

    @Override
    public boolean isNew() { return this.isNew; }
}