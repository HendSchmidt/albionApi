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
		BigDecimal prataPorFoco,           // Métrica Silver per Focus (SPF - prata gerada por ponto de foco)
		// Detalhes de Lote de Fabricação:
		Integer totalItensProduzidos,      // Cliques * rendimentoPorClique (ex: 1 clique de guisado = 10 itens)
		Integer rendimentoPorClique,       // Multiplicador por clique (ex: 10 para comida, 5 para poções, 1 para refino/equipamento)
		BigDecimal custoUnitarioItemFinal, // Custo corrigido por unidade final fabricada (ex: custo do clique / 10)
		BigDecimal lucroUnitarioItemFinal, // Lucro líquido por unidade final vendida
		String categoriaProducao           // Categoria informada
) {
	// Construtor compatível com a versão original
	public CraftResponseDto(
			BigDecimal custoTotalDaProdcao,
			List<RecursoResponseDto> custoPorRecurso,
			BigDecimal lucro
	) {
		this(custoTotalDaProdcao, custoPorRecurso, lucro,
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
				BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
				1, 1, custoTotalDaProdcao, lucro, "EQUIPAMENTOS");
	}

	// Construtor intermediário compatível
	public CraftResponseDto(
			BigDecimal custoTotalDaProdcao,
			List<RecursoResponseDto> custoPorRecurso,
			BigDecimal lucro,
			BigDecimal custoTaxaEstacao,
			BigDecimal receitaDiarios,
			BigDecimal custoDiariosVazios,
			BigDecimal lucroLiquidoDiarios,
			BigDecimal taxaMontagemOrdem,
			BigDecimal taxaVendaMercado,
			BigDecimal receitaLiquidaTotal,
			BigDecimal prataPorFoco
	) {
		this(custoTotalDaProdcao, custoPorRecurso, lucro,
				custoTaxaEstacao, receitaDiarios, custoDiariosVazios, lucroLiquidoDiarios,
				taxaMontagemOrdem, taxaVendaMercado, receitaLiquidaTotal, prataPorFoco,
				1, 1, custoTotalDaProdcao, lucro, "EQUIPAMENTOS");
	}
}
