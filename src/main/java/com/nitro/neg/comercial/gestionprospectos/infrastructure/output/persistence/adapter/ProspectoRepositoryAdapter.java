package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.adapter;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Foto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Modulo;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Solicitud;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.ProspectoId;
import com.nitro.neg.comercial.gestionprospectos.domain.port.output.ProspectoRepositoryPort;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.*;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.mapper.ProspectoPersistenceMapper;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProspectoRepositoryAdapter implements ProspectoRepositoryPort {

    private final R2dbcProspectoRepository prospectoRepo;
    private final R2dbcModuloRepository moduloRepo;
    private final R2dbcSolicitudRepository solicitudRepo;
    private final R2dbcFotoRepository fotoRepo;
    private final ProspectoPersistenceMapper mapper;

    @Override
    @Transactional
    public Mono<Prospecto> save(Prospecto prospecto) {
        ProspectoEntity entity = mapper.toEntity(prospecto);

        return prospectoRepo.save(entity)
                .flatMap(savedEntity ->
                        Mono.when(
                                saveModulo(prospecto.getModulo(), savedEntity.getId()),
                                saveSolicitud(prospecto.getSolicitud(), savedEntity.getId()),
                                saveFotos(prospecto.getFotos(), savedEntity.getId())
                        ).thenReturn(prospecto)
                );
    }

    @Override
    public Mono<Prospecto> findById(ProspectoId id) {
        String idStr = id.value();

        return prospectoRepo.findById(idStr)
                .flatMap(entity -> Mono.zip(
                        moduloRepo.findByProspectoId(idStr).defaultIfEmpty(new ModuloEntity()),
                        solicitudRepo.findByProspectoId(idStr).defaultIfEmpty(new SolicitudEntity()),
                        fotoRepo.findByProspectoId(idStr).collectList()
                ).map(tuple -> mapper.toDomain(entity, tuple.getT1(), tuple.getT2(), tuple.getT3())));
    }

    private Mono<ModuloEntity> saveModulo(Modulo m, String prospectoId) {
        if (m == null) return Mono.empty(); // Mono.when maneja esto correctamente

        String id = m.id() != null ? m.id() : UUID.randomUUID().toString();

        ModuloEntity e = ModuloEntity.builder()
                .id(id)
                .prospectoId(prospectoId)
                .uuid(m.uuid())
                .diasVisita(m.diasVisita())
                .periodoVisita(m.periodoVisita())
                .idModuloAsignado(m.idModuloAsignado())
                .build();

        return moduloRepo.save(e);
    }

    private Mono<SolicitudEntity> saveSolicitud(Solicitud s, String prospectoId) {
        if (s == null) return Mono.empty();

        String id = s.id() != null ? s.id() : UUID.randomUUID().toString();

        SolicitudEntity e = SolicitudEntity.builder()
                .id(id)
                .prospectoId(prospectoId)
                .uuid(s.uuid())
                .estado(s.estado())
                .tipo(s.tipo())
                .recordTypeId(s.recordTypeId())
                .enviarParaAprobacion(s.enviarParaAprobacion())
                .build();

        return solicitudRepo.save(e);
    }

    private Mono<List<FotoEntity>> saveFotos(List<Foto> fotos, String prospectoId) {
        if (fotos == null || fotos.isEmpty()) return Mono.empty();

        List<FotoEntity> entities = fotos.stream().map(f -> FotoEntity.builder()
                .id(f.id() != null ? f.id() : UUID.randomUUID().toString())
                .prospectoId(prospectoId)
                .url(f.url())
                .path(f.path())
                .nombreArchivo(f.nombreArchivo())
                .build()).collect(Collectors.toList());

        return fotoRepo.saveAll(entities).collectList();
    }
}