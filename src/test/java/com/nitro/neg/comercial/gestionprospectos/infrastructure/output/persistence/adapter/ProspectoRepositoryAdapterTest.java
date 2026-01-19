package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.adapter;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Foto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Modulo;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Solicitud;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.DocumentoIdentidad;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.ModuloEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.ProspectoEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.SolicitudEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.mapper.ProspectoPersistenceMapper;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProspectoRepositoryAdapterTest {

    @Mock private R2dbcProspectoRepository prospectoRepo;
    @Mock private R2dbcModuloRepository moduloRepo;
    @Mock private R2dbcSolicitudRepository solicitudRepo;
    @Mock private R2dbcFotoRepository fotoRepo;
    @Mock private ProspectoPersistenceMapper mapper;

    @InjectMocks
    private ProspectoRepositoryAdapter adapter;

    @Test
    void save_DeberiaGuardarPadreEHijos() {

        Prospecto prospecto = new Prospecto.Builder()
                .nombreRazonSocial("Test")
                .documentoIdentidad(new DocumentoIdentidad("RUC", "201"))
                .modulo(new Modulo("id", "uuid", "LUN", "mod1", "S"))
                .solicitud(new Solicitud("id", "uuid", "PEND", "A", "rt", true, true))

                .agregarFoto(new Foto("foto-id", "http://url", "path", "archivo.jpg"))
                .build();

        ProspectoEntity entity = ProspectoEntity.builder().id("p-1").build();

        when(mapper.toEntity(any())).thenReturn(entity);
        when(prospectoRepo.save(any())).thenReturn(Mono.just(entity));

        when(moduloRepo.save(any())).thenReturn(Mono.just(new ModuloEntity()));
        when(solicitudRepo.save(any())).thenReturn(Mono.just(new SolicitudEntity()));

        when(fotoRepo.saveAll(anyList())).thenReturn(Flux.empty());

        StepVerifier.create(adapter.save(prospecto))
                .expectNext(prospecto)
                .verifyComplete();

        verify(prospectoRepo).save(any());
        verify(moduloRepo).save(any());
        verify(solicitudRepo).save(any());
        verify(fotoRepo).saveAll(anyList());
    }
}