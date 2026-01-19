package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table("public.prospecto_foto")
public class FotoEntity {

    @Id
    @Column("id")
    private String id;

    @Column("prospecto_id")
    private String prospectoId;

    @Column("url_s3")
    private String url;

    @Column("path_s3")
    private String path;

    @Column("nombre_archivo")
    private String nombreArchivo;

    @Column("created_date")
    private LocalDateTime createdDate;
}