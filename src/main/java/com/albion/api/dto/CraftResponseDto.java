package com.albion.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record CraftResponseDto(
		BigDecimal custoTotalDaProdcao,
		List<RecursoResponseDto> custoPorRecurso,
		BigDecimal lucro,
		// Detalhamento econômico avançado:
		BigDecimal custoTaxaEstacao,      // Valor pago ao dono da estação/barraca de fabricação
		BigDecimal receitaDiarios,         // Receita líquida obtida com a venda dos diários preenchidos
		BigDecimal custoDiariosVazios,     // Custo total pago na compra dos diários vazios
		BigDecimal lucroLiquidoDiarios,    // Lucro limpo obtido exclusivamente na operação dos diários
		BigDecimal taxaMontagemOrdem,      // Taxa de 2.5% de setup fee quando vendido via Ordem de Venda
		BigDecimal taxaVendaMercado,       // Taxa de venda do mercado (6% com premium / 12% sem premium)
		BigDecimal receitaLiquidaTotal,    // Receita líquida total incluindo itens e diários após taxas
		BigDecimal prataPorFoco            // Métrica Silver per Focus (SPF - prata gerada por ponto de foco)
) {
	// Construtor compatível com a versão original
	public CraftResponseDto(
			BigDecimal custoTotalDaProdcao,
			List<RecursoResponseDto> custoPorRecurso,
			BigDecimal lucro
	) {
		this(custoTotalDaProdcao, custoPorRecurso, lucro,
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO);
	}
}
