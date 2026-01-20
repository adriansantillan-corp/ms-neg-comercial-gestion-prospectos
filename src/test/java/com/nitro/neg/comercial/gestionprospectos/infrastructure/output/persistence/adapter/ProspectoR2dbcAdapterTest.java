package com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.adapter;

import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Foto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Modulo;
import com.nitro.neg.comercial.gestionprospectos.domain.model.entity.Solicitud;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.DocumentoIdentidad; // Importar
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.ProspectoId;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.mapper.ProspectoPersistenceMapper;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.FotoEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.ModuloEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.ProspectoEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.entity.SolicitudEntity;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository.IFotoRepository;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository.IModuloRepository;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository.IProspectoRepository;
import com.nitro.neg.comercial.gestionprospectos.infrastructure.output.persistence.repository.ISolicitudRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProspectoR2dbcAdapterTest {

    @Mock private IProspectoRepository prospectoRepo;
    @Mock private IModuloRepository moduloRepo;
    @Mock private ISolicitudRepository solicitudRepo;
    @Mock private IFotoRepository fotoRepo;
    @Mock private ProspectoPersistenceMapper mapper;

    @InjectMocks
    private ProspectoR2dbcAdapter adapter;

    @Test
    void save_DeberiaGuardarPadreYHijos() {
        Prospecto prospectoDomain = mockProspectoDominio(true, true, true);
        ProspectoEntity prospectoEntity = ProspectoEntity.builder().id("p-1").build();

        when(mapper.toEntity(any(Prospecto.class))).thenReturn(prospectoEntity);
        when(mapper.toModuloEntity(any(), anyString(), anyBoolean())).thenReturn(ModuloEntity.builder().id("m-1").build());
        when(mapper.toSolicitudEntity(any(), anyString(), anyBoolean())).thenReturn(SolicitudEntity.builder().id("s-1").build());
        when(mapper.toFotoEntity(any(), anyString(), anyBoolean())).thenReturn(FotoEntity.builder().id("f-1").build());

        when(prospectoRepo.save(any(ProspectoEntity.class))).thenReturn(Mono.just(prospectoEntity));
        when(moduloRepo.save(any(ModuloEntity.class))).thenReturn(Mono.just(ModuloEntity.builder().build()));
        when(solicitudRepo.save(any(SolicitudEntity.class))).thenReturn(Mono.just(SolicitudEntity.builder().build()));
        when(fotoRepo.save(any(FotoEntity.class))).thenReturn(Mono.just(FotoEntity.builder().build()));

        StepVerifier.create(adapter.save(prospectoDomain))
                .expectNext(prospectoDomain)
                .verifyComplete();

        verify(prospectoRepo).save(any(ProspectoEntity.class));
        verify(moduloRepo).save(any(ModuloEntity.class));
        verify(solicitudRepo).save(any(SolicitudEntity.class));
        verify(fotoRepo, times(1)).save(any(FotoEntity.class));
    }

    @Test
    void save_DeberiaGuardarSoloPadre_CuandoNoTieneHijos() {
        Prospecto prospectoDomain = mockProspectoDominio(false, false, false);
        ProspectoEntity prospectoEntity = ProspectoEntity.builder().id("p-1").build();

        when(mapper.toEntity(any(Prospecto.class))).thenReturn(prospectoEntity);
        when(prospectoRepo.save(any(ProspectoEntity.class))).thenReturn(Mono.just(prospectoEntity));

        StepVerifier.create(adapter.save(prospectoDomain))
                .expectNext(prospectoDomain)
                .verifyComplete();

        verify(prospectoRepo).save(any());
        verifyNoInteractions(moduloRepo, solicitudRepo, fotoRepo);
    }

    @Test
    void findById_DeberiaBuscarYRetornarDominio() {
        ProspectoId id = new ProspectoId("p-1");
        ProspectoEntity entity = ProspectoEntity.builder().id("p-1").build();
        Prospecto domain = mockProspectoDominio(false, false, false);

        when(prospectoRepo.findById("p-1")).thenReturn(Mono.just(entity));
        when(mapper.toDomain(eq(entity), any(), any(), anyList())).thenReturn(domain);

        StepVerifier.create(adapter.findById(id))
                .expectNext(domain)
                .verifyComplete();
    }

    private Prospecto mockProspectoDominio(boolean conModulo, boolean conSolicitud, boolean conFotos) {
        Prospecto.Builder builder = new Prospecto.Builder()
                .id(new ProspectoId("1"))
                .uuid(UUID.randomUUID().toString())
                .nombreRazonSocial("Test Company")
                .documentoIdentidad(new DocumentoIdentidad("RUC", "20555555551"));

        if (conModulo) {
            builder.modulo(new Modulo("m-1", "uuid-m", "LUN", "MOD1", "SEM"));
        }
        if (conSolicitud) {
            builder.solicitud(new Solicitud("s-1", "uuid-s", "PEND", "ALTA", "RT", true, true));
        }
        if (conFotos) {
            builder.fotos(List.of(new Foto("f-1", "url", "path", "name")));
        } else {
            builder.fotos(Collections.emptyList());
        }

        return builder.build();
    }
}