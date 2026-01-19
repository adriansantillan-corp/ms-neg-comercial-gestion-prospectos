package com.nitro.neg.comercial.gestionprospectos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles; // Importar esto

@Import(TestcontainersConfiguration.class)
@SpringBootTest
@ActiveProfiles("test")
class MsNegComercialGestionProspectosApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring (Beans, Config, DB) levante sin errores
    }

}