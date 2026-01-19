package com.nitro.neg.comercial.gestionprospectos.infrastructure.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.HttpStatusServerEntryPoint;
import org.springframework.security.web.server.authorization.HttpStatusServerAccessDeniedHandler;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfiguration {

    private static final String PROSPECTOS_PATH = "/api/v1/prospectos/**";

    private static final String ROLE_VENDEDOR = "VENDEDOR";
    private static final String ROLE_ADMIN = "ADMIN";
    private static final String ROLE_SUPERVISOR = "SUPERVISOR";
    private static final String ROLE_JEFE_VENTAS = "JEFE_VENTAS";

    @Value("${app.security.oauth2.jwk-set-uri:mock://default-jwk-uri}")
    private String jwkSetUri;

    @Value("${app.security.enabled:true}")
    private boolean securityEnabled;

    private final Environment environment;

    public SecurityConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Profile("!test")
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        if (!securityEnabled) {
            return http
                    .csrf(ServerHttpSecurity.CsrfSpec::disable)
                    .authorizeExchange(exchanges -> exchanges.anyExchange().permitAll())
                    .build();
        }

        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .cors(corsSpec -> corsSpec.configurationSource(request -> {
                    var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                    corsConfig.setAllowedOrigins(List.of("*")); // Ajustar en Prod
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("*"));
                    corsConfig.setMaxAge(3600L);
                    return corsConfig;
                }))
                .authorizeExchange(exchanges -> exchanges
                        // Endpoints públicos de Infraestructura
                        .pathMatchers("/actuator/**", "/health").permitAll()
                        .pathMatchers("/v3/api-docs/**").permitAll()
                        .pathMatchers("/api/v1/api-docs/**", "/api/v1/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .pathMatchers("/webjars/**").permitAll()

                        .pathMatchers(HttpMethod.POST, PROSPECTOS_PATH).hasAnyRole(ROLE_VENDEDOR, ROLE_ADMIN, ROLE_SUPERVISOR)
                        .pathMatchers(HttpMethod.GET, PROSPECTOS_PATH).hasAnyRole(ROLE_VENDEDOR, ROLE_ADMIN, ROLE_SUPERVISOR, ROLE_JEFE_VENTAS)
                        .pathMatchers(HttpMethod.PUT, PROSPECTOS_PATH).hasAnyRole(ROLE_VENDEDOR, ROLE_ADMIN)

                        .anyExchange().authenticated()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtSpec -> jwtSpec.jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler(new HttpStatusServerAccessDeniedHandler(HttpStatus.FORBIDDEN))
                )
                .exceptionHandling(exceptionHandling -> exceptionHandling
                        .authenticationEntryPoint(new HttpStatusServerEntryPoint(HttpStatus.UNAUTHORIZED))
                        .accessDeniedHandler(new HttpStatusServerAccessDeniedHandler(HttpStatus.FORBIDDEN))
                )
                .build();
    }

    @Bean
    @Profile("!test")
    public ReactiveJwtDecoder jwtDecoder() {
        if (!securityEnabled) {
            return token -> Mono.just(Jwt.withTokenValue("mock-token")
                    .header("alg", "none")
                    .claim("sub", "mock-user")
                    .claim("cognito:groups", List.of(ROLE_VENDEDOR, ROLE_ADMIN))
                    .build());
        }

        if (jwkSetUri == null || jwkSetUri.trim().isEmpty() || jwkSetUri.startsWith("mock://")) {
            return token -> Mono.just(Jwt.withTokenValue("secure-mock-token")
                    .header("alg", "none")
                    .claim("sub", "secure-user")
                    .claim("cognito:groups", List.of(ROLE_VENDEDOR, ROLE_ADMIN))
                    .build());
        }

        return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
    }

    private ReactiveJwtAuthenticationConverterAdapter jwtAuthenticationConverter() {
        JwtAuthenticationConverter delegate = new JwtAuthenticationConverter();

        delegate.setJwtGrantedAuthoritiesConverter(jwt -> {
            Collection<String> roles = extractRolesFromJwt(jwt);
            return roles.stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    .map(GrantedAuthority.class::cast)
                    .toList();
        });

        return new ReactiveJwtAuthenticationConverterAdapter(delegate);
    }

    private Collection<String> extractRolesFromJwt(Jwt jwt) {
        Collection<String> cognitoGroups = jwt.getClaimAsStringList("cognito:groups");
        if (cognitoGroups != null && !cognitoGroups.isEmpty()) {
            return cognitoGroups;
        }

        Collection<String> roles = jwt.getClaimAsStringList("roles");
        if (roles != null && !roles.isEmpty()) {
            return roles;
        }

        Collection<String> authorities = jwt.getClaimAsStringList("authorities");
        if (authorities != null && !authorities.isEmpty()) {
            return authorities;
        }

        return Collections.emptyList();
    }

    /**
     * Utilidad para obtener el ID del usuario actual desde el contexto reactivo.
     */
    public static Mono<String> getCurrentUserId(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return Mono.empty();
        }
        if (authentication.getPrincipal() instanceof Jwt jwt) {
            return Mono.just(jwt.getSubject());
        }
        return Mono.just(authentication.getName());
    }
}