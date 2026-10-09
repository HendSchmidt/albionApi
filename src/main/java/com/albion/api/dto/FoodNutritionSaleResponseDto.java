package com.albion.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record FoodNutritionSaleResponseDto(
    String nomeComida,
    BigDecimal nutricaoEfetivaPorUnidade, // Dobrada se comida favorita
    BigDecimal valorPagoPorUnidadeBarraquinha, // (Nutrição / 100) * X
    BigDecimal receitaTotalBarraquinha,       // ValorUnitario * Quantidade
    BigDecimal custoProducaoTotal,           // Custo corrigido com TRR dos ingredientes
    BigDecimal custoProducaoPorUnidade,      // Custo total / Quantidade
    BigDecimal lucroTotalBarraquinha,        // Receita Barraquinha - Custo Produção Total
    BigDecimal lucroUnitarioBarraquinha,     // Valor pago barraquinha - Custo por unidade
    BigDecimal precoMercadoUnitario,         // Preço no mercado
    BigDecimal taxaMercadoPercentual,        // 6.5% com premium ou 10.5% sem premium
    BigDecimal precoLiquidoMercadoUnitario,  // Preço mercado * (1 - taxa)
    BigDecimal receitaLiquidaTotalMercado,   // PrecoLiquidoMercadoUnitario * Quantidade
    BigDecimal lucroTotalMercado,            // Receita Liquida Mercado - Custo Produção Total
    BigDecimal lucroUnitarioMercado,         // Lucro unitario no mercado
    String melhorOpcao,                      // "BARRAQUINHA", "MERCADO" ou "PREJUIZO"
    String recomendacao,                     // Descrição e justificativa analítica detalhada
    BigDecimal diferencaBarraquinhaVsMercado // Vantagem em prata da melhor opção
) {}
