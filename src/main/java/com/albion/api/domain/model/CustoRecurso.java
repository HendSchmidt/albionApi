package com.albion.api.domain.model;

import java.math.BigDecimal;

/**
 * Value Object com o cálculo econômico de custo de um recurso específico.
 */
public record CustoRecurso(
        String nome,
        BigDecimal custoTotal
) {}
