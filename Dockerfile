# syntax=docker/dockerfile:1.4

# =================================================================
# ETAPA 1: CACHE DE DEPENDENCIAS
# =================================================================
FROM gradle:8-jdk17-jammy AS dependencies

ENV GRADLE_USER_HOME=/home/gradle/.gradle
WORKDIR /home/gradle/src

# Copiar archivos de configuración
COPY --chown=gradle:gradle build.gradle settings.gradle gradle.properties* ./
COPY --chown=gradle:gradle gradle/ gradle/

USER gradle

# Descargar dependencias
RUN --mount=type=cache,target=/home/gradle/.gradle \
    ./gradlew dependencies --no-daemon --refresh-dependencies || true

# =================================================================
# ETAPA 2: BUILD
# =================================================================
FROM gradle:8-jdk17-jammy AS builder

# Reutilizar caché de la etapa anterior
COPY --from=dependencies --chown=gradle:gradle /home/gradle/.gradle /home/gradle/.gradle

WORKDIR /home/gradle/src

# Copiar código fuente
COPY --chown=gradle:gradle . .

# 1. /home/gradle/.gradle (Caché global copiado)
# 2. /home/gradle/src     (Código fuente, para poder crear .gradle local)
USER root
RUN chown -R gradle:gradle /home/gradle
USER gradle

# Compilar
RUN ./gradlew build --no-daemon -x test --stacktrace && \
    ls -lh build/libs/

# =================================================================
# ETAPA 3: RUNTIME
# =================================================================
FROM eclipse-temurin:17-jre-jammy

RUN addgroup --system --gid 1001 appuser && \
    adduser --system --uid 1001 --gid 1001 appuser && \
    mkdir /app && chown appuser:appuser /app && \
    apt-get update && \
    apt-get install -y --no-install-recommends wget && \
    rm -rf /var/lib/apt/lists/*

USER appuser:appuser
WORKDIR /app

# Copiar JAR
COPY --from=builder --chown=appuser:appuser /home/gradle/src/build/libs/*.jar app.jar

ENV APP_PORT=8080 \
    SPRING_PROFILES_ACTIVE=prod \
    TZ=America/Lima \
    AWS_REGION=us-east-1

EXPOSE ${APP_PORT}

HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:${APP_PORT}/actuator/health || exit 1

ENTRYPOINT ["java", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseG1GC", \
    "-XX:+ExitOnOutOfMemoryError", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-Dreactor.netty.ioWorkerCount=4", \
    "-jar", "app.jar"]