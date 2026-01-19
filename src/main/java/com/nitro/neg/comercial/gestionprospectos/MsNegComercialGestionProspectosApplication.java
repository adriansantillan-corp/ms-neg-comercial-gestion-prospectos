package com.nitro.neg.comercial.gestionprospectos;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class MsNegComercialGestionProspectosApplication {

    private final Environment environment;

    public static void main(String[] args) {
        SpringApplication.run(MsNegComercialGestionProspectosApplication.class, args);
    }

    @EventListener
    public void handleContextRefresh(ContextRefreshedEvent event) {
        String[] activeProfiles = environment.getActiveProfiles();

        log.info("""
            
            ═══════════════════════════════════════════════════════════
                    MS-NEG-COMERCIAL-GESTION-PROSPECTOS INICIADO
            ═══════════════════════════════════════════════════════════
            """);

        log.info("Perfiles activos: {}",
                activeProfiles.length > 0 ? String.join(", ", activeProfiles) : "default");

        if (activeProfiles.length == 0) {
            log.warn("⚠️ Ningún perfil activo - usando perfil 'default'");
            log.warn("⚠️ Es recomendable especificar un perfil explícito (local, docker, prod)");
            return;
        }

        if (environment.acceptsProfiles(Profiles.of("local"))) {
            logLocalEnvironment();
        } else if (environment.acceptsProfiles(Profiles.of("docker"))) {
            logDockerEnvironment();
        } else if (environment.acceptsProfiles(Profiles.of("prod"))) {
            logProdEnvironment();
        } else if (environment.acceptsProfiles(Profiles.of("test"))) {
            logTestEnvironment();
        }

        log.info("""
            ═══════════════════════════════════════════════════════════
                    APLICACIÓN LISTA PARA RECIBIR REQUESTS
            ═══════════════════════════════════════════════════════════
            """);
    }

    private void logLocalEnvironment() {
        log.warn("""
            
            ┌────────────────────────────────────────────────────────┐
            │      ENTORNO 'local' ACTIVO                            │
            ├────────────────────────────────────────────────────────┤
            │      DB: PostgreSQL Local (R2DBC + JDBC)               │
            │ 🛠️    Flyway: Ejecutándose al inicio                    │
            │ ☁️    AWS S3: Perfil 'default' (~/.aws/credentials)     │
            │      Swagger UI: http://localhost:8080/swagger-ui.html │
            │      Health: http://localhost:8080/actuator/health     │
            │                                                        │
            │      SOLO PARA DESARROLLO - NO USAR EN PRODUCCIÓN      │
            └────────────────────────────────────────────────────────┘
            """);
    }

    private void logDockerEnvironment() {
        log.info("""
            
            ┌────────────────────────────────────────────────────────┐
            │   ENTORNO 'docker' ACTIVO                              │
            ├────────────────────────────────────────────────────────┤
            │      DB: Contenedor Postgres                           │
            │      Flyway: Desactivado (Usar contenedor init)        │
            │      Swagger UI: Accesible                             │
            │ ☁️    AWS S3: Bucket DEV                                │
            └────────────────────────────────────────────────────────┘
            """);
    }

    private void logProdEnvironment() {
        log.info("""
            
            ┌────────────────────────────────────────────────────────┐
            │      ENTORNO 'prod' ACTIVO                             │
            ├────────────────────────────────────────────────────────┤
            │      DB: Aurora PostgreSQL (R2DBC Pool Optimizado)     │
            │ 🛡️    Seguridad: IAM Roles (ECS Task Role)              │
            │      Swagger UI: NO ACCESIBLE                          │
            │ ☁️    AWS S3: Bucket PROD                               │
            │      Integración: Salesforce PROD                      │
            └────────────────────────────────────────────────────────┘
            """);
    }

    private void logTestEnvironment() {
        log.info("""
            
            ┌────────────────────────────────────────────────────────┐
            │      ENTORNO 'test' ACTIVO                             │
            ├────────────────────────────────────────────────────────┤
            │      DB: Testcontainers (Ephemeral)                    │
            │      AWS: Mocked / Localstack                          │
            └────────────────────────────────────────────────────────┘
            """);
    }
}