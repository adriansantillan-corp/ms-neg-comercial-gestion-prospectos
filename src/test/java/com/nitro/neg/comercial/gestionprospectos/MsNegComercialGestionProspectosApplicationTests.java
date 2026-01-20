package com.nitro.neg.comercial.gestionprospectos;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "spring.autoconfigure.exclude=" +
                        "io.awspring.cloud.autoconfigure.s3.S3AutoConfiguration," +
                        "io.awspring.cloud.autoconfigure.core.AwsAutoConfiguration," +
                        "io.awspring.cloud.autoconfigure.core.CredentialsProviderAutoConfiguration," +
                        "io.awspring.cloud.autoconfigure.core.RegionProviderAutoConfiguration," +
                        "io.awspring.cloud.autoconfigure.config.secretsmanager.SecretsManagerAutoConfiguration," +
                        "io.awspring.cloud.autoconfigure.config.parameterstore.ParameterStoreAutoConfiguration"
        }
)
@Import(TestcontainersConfiguration.class)
@ActiveProfiles("test")
class MsNegComercialGestionProspectosApplicationTests {

    @Test
    void contextLoads() {
        // Verifica que el contexto de Spring se cargue correctamente
        // con PostgreSQL real (Testcontainers), sin AWS, sin Salesforce real
    }

}