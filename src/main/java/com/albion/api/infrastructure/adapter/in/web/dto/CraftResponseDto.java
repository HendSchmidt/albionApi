package com.albion.api.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * DTO de resposta para o cálculo de viabilidade de fabricação.
 * Suporta tanto 'custosPorRecurso' quanto 'custoPorRecurso' para compatibilidade total.
 */
public record CraftResponseDto(
        BigDecimal custoTotalDaProdcao,
        List<RecursoResponseDto> custosPorRecurso,
        List<RecursoResponseDto> custoPorRecurso,
        BigDecimal lucro,
        BigDecimal custoTaxaEstacao,
        BigDecimal receitaDiarios,
        BigDecimal custoDiariosVazios,
        BigDecimal lucroLiquidoDiarios,
        BigDecimal taxaMontagemOrdem,
        BigDecimal taxaVendaMercado,
        BigDecimal receitaLiquidaTotal,
        BigDecimal prataPorPontoDeFoco,
        Integer totalItensProduzidos,
        Integer rendimentoPorClique,
        BigDecimal custoUnitarioItemFinal,
        BigDecimal lucroUnitarioItemFinal,
        String categoriaProducao
) {
    public CraftResponseDto(
            BigDecimal custoTotalDaProdcao,
            List<RecursoResponseDto> custosPorRecurso,
            BigDecimal lucro,
            BigDecimal custoTaxaEstacao,
            BigDecimal receitaDiarios,
            BigDecimal custoDiariosVazios,
            BigDecimal lucroLiquidoDiarios,
            BigDecimal taxaMontagemOrdem,
            BigDecimal taxaVendaMercado,
            BigDecimal receitaLiquidaTotal,
            BigDecimal prataPorPontoDeFoco,
            Integer totalItensProduzidos,
            Integer rendimentoPorClique,
            BigDecimal custoUnitarioItemFinal,
            BigDecimal lucroUnitarioItemFinal,
            String categoriaProducao
    ) {
        this(
                custoTotalDaProdcao,
                custosPorRecurso,
                custosPorRecurso,
                lucro,
                custoTaxaEstacao,
                receitaDiarios,
                custoDiariosVazios,
                lucroLiquidoDiarios,
                taxaMontagemOrdem,
                taxaVendaMercado,
                receitaLiquidaTotal,
                prataPorPontoDeFoco,
                totalItensProduzidos,
                rendimentoPorClique,
                custoUnitarioItemFinal,
                lucroUnitarioItemFinal,
                categoriaProducao
        );
    }

    public CraftResponseDto(
            BigDecimal custoTotalDaProdcao,
            List<RecursoResponseDto> custosPorRecurso,
            BigDecimal lucro
    ) {
        this(custoTotalDaProdcao, custosPorRecurso, custosPorRecurso, lucro,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                null, 1, null, null, "EQUIPAMENTOS");
    }
}
