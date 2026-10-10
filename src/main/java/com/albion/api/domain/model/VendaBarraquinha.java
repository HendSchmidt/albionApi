package com.albion.api.domain.model;

import java.math.BigDecimal;

/**
 * Modelo de domínio com o resultado consolidado da comparação entre venda para barraquinha e mercado.
 */
public record VendaBarraquinha(
        String nomeComida,
        BigDecimal nutricaoEfetiva,
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
        BigDecimal diferenca
) {}
