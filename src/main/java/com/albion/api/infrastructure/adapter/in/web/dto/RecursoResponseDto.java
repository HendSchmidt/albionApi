package com.albion.api.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

public record RecursoResponseDto(
        String nome,
        BigDecimal custo
) {}
