package com.albion.api.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;

public record FoodNutritionSaleResponseDto(
        String nomeComida,
        BigDecimal nutricaoEfetivaPorUnidade,
        BigDecimal valorPagoPorUnidadeBarraquinha,
        BigDecimal receitaTotalBarraquinha,
        BigDecimal custoProducaoTotal,
        BigDecimal custoProducaoPorUnidade,
        BigDecimal lucroTotalBarraquinha,
        BigDecimal lucroUnitarioBarraquinha,
        BigDecimal precoMercadoUnitario,
        BigDecimal taxaMercadoPercentual,
        BigDecimal precoLiquidoMercadoUnitario,
        BigDecimal receitaLiquidaTotalMercado,
        BigDecimal lucroTotalMercado,
        BigDecimal lucroUnitarioMercado,
        String melhorOpcao,
        String recomendacao,
        BigDecimal diferencaBarraquinhaVsMercado
) {}
