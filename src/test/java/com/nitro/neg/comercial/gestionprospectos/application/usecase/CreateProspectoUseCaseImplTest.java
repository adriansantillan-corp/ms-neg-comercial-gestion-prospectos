package com.nitro.neg.comercial.gestionprospectos.application.usecase;

import com.nitro.neg.comercial.gestionprospectos.domain.exception.DomainException;
import com.nitro.neg.comercial.gestionprospectos.domain.model.aggregate.Prospecto;
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.DocumentoIdentidad; // Importar
import com.nitro.neg.comercial.gestionprospectos.domain.model.vo.ProspectoId;
import com.nitro.neg.comercial.gestionprospectos.domain.port.output.ProspectoRepositoryPort;
import com.nitro.neg.comercial.gestionprospectos.domain.port.output.SalesforceIntegrationPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProspectoUseCaseImplTest {

    @Mock
    private ProspectoRepositoryPort repositoryPort;

    @Mock
    private SalesforceIntegrationPort salesforcePort;

    @InjectMocks
    private CreateProspectoUseCaseImpl useCase;

    @Test
    void ejecutar_DeberiaGuardarSincronizarYActualizar_CuandoTodoEsExitoso() {
        Prospecto input = mockProspecto("p-1", null);
        Prospecto guardadoInicial = mockProspecto("p-1", null);
        String sfId = "SF-NEW-ID";
        Prospecto guardadoFinal = mockProspecto("p-1", sfId);

        when(repositoryPort.save(any(Prospecto.class))).thenReturn(Mono.just(guardadoInicial));
        when(salesforcePort.enviarProspecto(any(Prospecto.class))).thenReturn(Mono.just(sfId));
        when(repositoryPort.save(argThat(p -> sfId.equals(p.getSalesforceId())))).thenReturn(Mono.just(guardadoFinal));

        StepVerifier.create(useCase.ejecutar(input))
                .expectNextMatches(p -> sfId.equals(p.getSalesforceId()))
                .verifyComplete();

        verify(repositoryPort, times(2)).save(any(Prospecto.class));
        verify(salesforcePort).enviarProspecto(any(Prospecto.class));
    }

    @Test
    void ejecutar_DeberiaRetornarProspectoLocal_CuandoSalesforceFalla() {
        Prospecto input = mockProspecto("p-1", null);

        when(repositoryPort.save(any(Prospecto.class))).thenReturn(Mono.just(input));
        when(salesforcePort.enviarProspecto(any(Prospecto.class)))
                .thenReturn(Mono.error(new RuntimeException("Salesforce down")));

        StepVerifier.create(useCase.ejecutar(input))
                .expectNextMatches(p -> p.getSalesforceId() == null)
                .verifyComplete();

        verify(repositoryPort, times(1)).save(any(Prospecto.class));
        verify(salesforcePort).enviarProspecto(any(Prospecto.class));
    }

    @Test
    void ejecutar_DeberiaLanzarDomainException_CuandoFallaBaseDeDatos() {
        Prospecto input = mockProspecto("p-1", null);
        when(repositoryPort.save(any(Prospecto.class)))
                .thenReturn(Mono.error(new RuntimeException("DB Error")));

        StepVerifier.create(useCase.ejecutar(input))
                .expectError(DomainException.class)
                .verify();

        verify(salesforcePort, never()).enviarProspecto(any());
    }

    private Prospecto mockProspecto(String id, String sfId) {
        return new Prospecto.Builder()
                .id(new ProspectoId(id))
                .uuid(UUID.randomUUID().toString())
                .salesforceId(sfId)
                .nombreRazonSocial("Test Company")
                .documentoIdentidad(new DocumentoIdentidad("RUC", "20555555551"))
                .build();
    }
}