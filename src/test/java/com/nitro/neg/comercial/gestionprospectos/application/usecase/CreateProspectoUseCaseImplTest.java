package com.nitro.neg.comercial.gestionprospectos.application.usecase;

import com.nitro.neg.comercial.gestionprospectos.domain.exception.DomainException;
import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.DocumentoIdentidad;
import com.nitro.neg.comercial.gestionprospectos.domain.port.output.ProspectoRepositoryPort;
import com.nitro.neg.comercial.gestionprospectos.domain.port.output.SalesforceIntegrationPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProspectoUseCaseImplTest {

    @Mock
    private ProspectoRepositoryPort repositoryPort;
    @Mock
    private SalesforceIntegrationPort salesforcePort;

    @InjectMocks
    private CreateProspectoUseCaseImpl useCase;

    private Prospecto prospectoValido;

    @BeforeEach
    void setUp() {
        prospectoValido = new Prospecto.Builder()
                .nombreRazonSocial("Empresa X")
                .documentoIdentidad(new DocumentoIdentidad("RUC", "20100100100"))
                .build();
    }

    @Test
    void ejecutar_FlujoExitoso_DeberiaGuardarYSyncronizar() {
        when(repositoryPort.save(any(Prospecto.class))).thenReturn(Mono.just(prospectoValido));
        when(salesforcePort.enviarProspecto(any(Prospecto.class))).thenReturn(Mono.just("SF-ID-001"));

        StepVerifier.create(useCase.ejecutar(prospectoValido))
                .expectNextMatches(p -> "SF-ID-001".equals(p.getSalesforceId()))
                .verifyComplete();

        verify(repositoryPort, times(2)).save(any(Prospecto.class));
    }

    @Test
    void ejecutar_CuandoFallaSalesforce_DeberiaRetornarLocalSinError() {
        when(repositoryPort.save(any(Prospecto.class))).thenReturn(Mono.just(prospectoValido));
        when(salesforcePort.enviarProspecto(any(Prospecto.class)))
                .thenReturn(Mono.error(new RuntimeException("API Down")));

        StepVerifier.create(useCase.ejecutar(prospectoValido))
                .expectNextMatches(p -> p.getSalesforceId() == null)
                .verifyComplete();

        verify(repositoryPort, times(1)).save(any(Prospecto.class));
    }

    @Test
    void ejecutar_CuandoFallaBaseDeDatos_DeberiaLanzarDomainException() {
        when(repositoryPort.save(any())).thenReturn(Mono.error(new RuntimeException("DB Error")));

        StepVerifier.create(useCase.ejecutar(prospectoValido))
                .expectError(DomainException.class)
                .verify();
    }
}