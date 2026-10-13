package com.albion.api.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

/**
 * DTO para retorno do custo calculado de cada insumo.
 * Contém tanto o campo 'valor' quanto 'custo' para compatibilidade total com o front-end.
 */
public record RecursoResponseDto(
        String nome,
        BigDecimal valor,
        BigDecimal custo
) {
    public RecursoResponseDto(String nome, BigDecimal custo) {
        this(nome, custo, custo);
    }
}
