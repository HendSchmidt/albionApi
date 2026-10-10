package com.albion.api.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

public record RecursoRequestDto(
        String nome,
        int quantidade,
        BigDecimal valor
) {}
