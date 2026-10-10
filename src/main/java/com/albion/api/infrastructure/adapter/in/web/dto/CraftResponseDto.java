package com.albion.api.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record CraftResponseDto(
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
    public CraftResponseDto(
            BigDecimal custoTotalDaProdcao,
            List<RecursoResponseDto> custosPorRecurso,
            BigDecimal lucro
    ) {
        this(custoTotalDaProdcao, custosPorRecurso, lucro,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                null, 1, null, null, "EQUIPAMENTOS");
    }
}
