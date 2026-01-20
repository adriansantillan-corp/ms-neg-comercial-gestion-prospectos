package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.adapter;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.ProspectoId;
import com.nitro.neg.comercial.gestionprospectos.domain.port.output.ProspectoRepositoryPort;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.mapper.ProspectoPersistenceMapper;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository.IFotoRepository; // Nuevo
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository.IModuloRepository;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository.IProspectoRepository;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository.ISolicitudRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProspectoR2dbcAdapter implements ProspectoRepositoryPort {

    private final IProspectoRepository prospectoRepo;
    private final IModuloRepository moduloRepo;
    private final ISolicitudRepository solicitudRepo;
    private final IFotoRepository fotoRepo;
    private final ProspectoPersistenceMapper mapper;

    @Override
    @Transactional
    public Mono<Prospecto> save(Prospecto prospecto) {

        return prospectoRepo.save(mapper.toEntity(prospecto))
                .flatMap(savedProspecto -> {
                    String prospectoId = savedProspecto.getId();
                    boolean esNuevo = prospecto.getSalesforceId() == null;

                    Mono<Void> saveModulo = Mono.empty();
                    if (prospecto.getModulo() != null) {
                        var moduloEntity = mapper.toModuloEntity(prospecto.getModulo(), prospectoId, esNuevo);
                        saveModulo = moduloRepo.save(moduloEntity).then();
                    }

                    Mono<Void> saveSolicitud = Mono.empty();
                    if (prospecto.getSolicitud() != null) {
                        var solicitudEntity = mapper.toSolicitudEntity(prospecto.getSolicitud(), prospectoId, esNuevo);
                        saveSolicitud = solicitudRepo.save(solicitudEntity).then();
                    }

                    Mono<Void> saveFotos = Mono.empty();
                    if (prospecto.getFotos() != null && !prospecto.getFotos().isEmpty()) {
                        saveFotos = Flux.fromIterable(prospecto.getFotos())
                                .map(foto -> mapper.toFotoEntity(foto, prospectoId, esNuevo))
                                .flatMap(fotoRepo::save)
                                .then();
                    }

                    return Mono.when(saveModulo, saveSolicitud, saveFotos)
                            .thenReturn(prospecto);
                });
    }

    @Override
    public Mono<Prospecto> findById(ProspectoId id) {
        return prospectoRepo.findById(id.value())
                .map(entity -> mapper.toDomain(entity, null, null, Collections.emptyList()));
    }
}