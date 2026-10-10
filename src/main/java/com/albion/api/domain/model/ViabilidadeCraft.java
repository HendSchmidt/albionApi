package com.albion.api.domain.model;

import java.math.BigDecimal;
import java.util.List;

/**
 * Modelo de domínio com o resultado consolidado do cálculo de viabilidade de craft.
 */
public record ViabilidadeCraft(
        BigDecimal custoTotalProducao,
        List<CustoRecurso> custosPorRecurso,
        BigDecimal lucro,
        BigDecimal custoTaxaEstacao,
        BigDecimal receitaLiquidaDiarios,
        BigDecimal custoDiariosVazios,
        BigDecimal lucroLiquidoDiarios,
        BigDecimal taxaMontagemTotal,
        BigDecimal taxaVendaTotal,
        BigDecimal receitaLiquidaTotal,
        BigDecimal prataPorFoco,
        int totalItensProduzidos,
        int rendimentoPorClique,
        BigDecimal custoUnitarioItemFinal,
        BigDecimal lucroUnitarioItemFinal,
        String categoriaProducao
) {}
