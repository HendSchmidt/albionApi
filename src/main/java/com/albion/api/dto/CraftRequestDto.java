package com.albion.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record CraftRequestDto(
		List<RecursoRequestDto> recurso,
		int quantidadeParaProducao,
		int taxaDeRetorno,
		BigDecimal precoDeVenda,
		boolean contaPremium,
		// Funcionalidades avançadas da economia do Albion Online:
		BigDecimal taxaEstacaoPorCemNutricao, // Taxa cobrada pelo dono da barraca na cidade (por 100 de nutrição)
		BigDecimal itemValue,                 // Item Value do item para cálculo da nutrição consumida
		Integer quantidadeDiarios,            // Quantidade de diários de artesão preenchidos no lote
		BigDecimal precoDiarioVazio,          // Preço de compra de cada diário vazio no mercado
		BigDecimal precoDiarioCheio,          // Preço de venda de cada diário cheio no mercado
		BigDecimal valorVendaDiario,          // Mantido para compatibilidade retroativa (caso precoDiarioCheio não venha)
		Boolean ordemDeVenda,                 // true = Ordem de Venda (adiciona taxa de 2.5%), false = Venda Instantânea
		Boolean usarFoco,                     // Indica se foi utilizado Foco de Produção
		Integer custoFocoTotal                // Quantidade total de pontos de foco gastos no lote
) {
	// Construtor compatível com a versão original
	public CraftRequestDto(
			List<RecursoRequestDto> recurso,
			int quantidadeParaProducao,
			int taxaDeRetorno,
			BigDecimal precoDeVenda,
			boolean contaPremium
	) {
		this(recurso, quantidadeParaProducao, taxaDeRetorno, precoDeVenda, contaPremium,
				null, null, null, null, null, null, true, false, null);
	}
}
