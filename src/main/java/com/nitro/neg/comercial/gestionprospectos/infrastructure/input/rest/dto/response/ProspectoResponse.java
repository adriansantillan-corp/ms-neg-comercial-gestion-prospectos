package com.nitro.neg.comercial.gestionprospectos.infrastructure.input.rest.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProspectoResponse {
    private String id;
    private String uuid;
    private String mensaje;
}