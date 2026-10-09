package com.albion.api.service;

import com.albion.api.dto.CraftRequestDto;
import com.albion.api.dto.CraftResponseDto;
import com.albion.api.dto.RecursoRequestDto;
import com.albion.api.dto.RecursoResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculaViabilidadeDeProdcao {

	private static final Logger log = LoggerFactory.getLogger(CalculaViabilidadeDeProdcao.class);

	// Taxas do mercado:
	// Com Conta Premium: 6% de taxa (0.06).
	// Sem Conta Premium: 12% de taxa (0.12), pois a Conta Premium equivale a 6% de desconto.
	private static final BigDecimal TAXA_MERCADO_COM_PREMIUM = new BigDecimal("0.06");
	private static final BigDecimal TAXA_MERCADO_SEM_PREMIUM = new BigDecimal("0.12");

	// Taxa de montagem de ordem no mercado do Albion (Setup Fee): 2.5% (0.025)
	private static final BigDecimal TAXA_MONTAGEM_ORDEM = new BigDecimal("0.025");

	// Fator oficial de consumo de nutrição por Item Value no Albion: 0.1125
	private static final BigDecimal FATOR_NUTRICAO = new BigDecimal("0.1125");

	private static final BigDecimal CEM = new BigDecimal("100");

	/**
	 * Calcula o custo por recurso, taxas de estação, ciclo completo dos diários de artesão
	 * (compra do vazio vs venda do cheio com taxas), taxas de mercado e Silver per Focus (SPF).
	 *
	 * @param request DTO com todos os parâmetros de craft do Albion Online.
	 * @return CraftResponseDto com detalhamento econômico completo.
	 */
	public CraftResponseDto calcular(CraftRequestDto request) {
		log.info("[Classe: {}] [Metodo: {}] [Entrada: {}]", 
				"CalculaViabilidadeDeProdcao", 
				"calcular", 
				request);

		if (request == null || request.recurso() == null) {
			IllegalArgumentException ex = new IllegalArgumentException("O payload de fabricação e a lista de recursos não podem ser nulos.");
			log.error("[Classe: {}] [Metodo: {}] [Erro: {}]", 
					"CalculaViabilidadeDeProdcao", 
					"calcular", 
					ex.getMessage());
			throw ex;
		}

		int quantidadeProducao = Math.max(1, request.quantidadeParaProducao());
		int taxaRetorno = Math.max(0, request.taxaDeRetorno());

		// 1. Fator de retorno de materiais (RRR)
		BigDecimal fatorRetorno = BigDecimal.valueOf(taxaRetorno)
				.divide(CEM, 4, RoundingMode.HALF_UP);
		BigDecimal fatorConsumo = BigDecimal.ONE.subtract(fatorRetorno);

		List<RecursoResponseDto> custosPorRecurso = new ArrayList<>();
		BigDecimal custoInsumos = BigDecimal.ZERO;

		// Cálculo do custo de cada recurso com a taxa de retorno
		for (RecursoRequestDto recurso : request.recurso()) {
			if (recurso == null) continue;
			BigDecimal qtdTotal = BigDecimal.valueOf((long) recurso.quantidade() * quantidadeProducao);
			BigDecimal qtdConsumidaEfetiva = qtdTotal.multiply(fatorConsumo);
			BigDecimal valorUnitario = recurso.valor() != null ? recurso.valor() : BigDecimal.ZERO;
			BigDecimal custoRecurso = qtdConsumidaEfetiva
					.multiply(valorUnitario)
					.setScale(2, RoundingMode.HALF_UP);

			custosPorRecurso.add(new RecursoResponseDto(recurso.nome(), custoRecurso));
			custoInsumos = custoInsumos.add(custoRecurso);
		}

		// 2. Cálculo da Taxa da Estação de Fabricação (Station / Nutrition Fee)
		BigDecimal custoTaxaEstacao = BigDecimal.ZERO;
		if (request.taxaEstacaoPorCemNutricao() != null && request.taxaEstacaoPorCemNutricao().compareTo(BigDecimal.ZERO) > 0) {
			BigDecimal iv = request.itemValue() != null && request.itemValue().compareTo(BigDecimal.ZERO) > 0
					? request.itemValue()
					: custoInsumos.divide(BigDecimal.valueOf(quantidadeProducao), 2, RoundingMode.HALF_UP);

			// Nutrição consumida = Item Value * 0.1125 * Quantidade
			BigDecimal nutricaoConsumida = iv
					.multiply(FATOR_NUTRICAO)
					.multiply(BigDecimal.valueOf(quantidadeProducao));

			// Custo = (Nutrição / 100) * Taxa por 100 de nutrição
			custoTaxaEstacao = nutricaoConsumida
					.divide(CEM, 4, RoundingMode.HALF_UP)
					.multiply(request.taxaEstacaoPorCemNutricao())
					.setScale(2, RoundingMode.HALF_UP);
		}

		// 3. Taxas de Mercado (Itens Fabricados):
		BigDecimal precoVenda = request.precoDeVenda() != null ? request.precoDeVenda() : BigDecimal.ZERO;
		BigDecimal receitaBrutaItens = precoVenda
				.multiply(BigDecimal.valueOf(quantidadeProducao))
				.setScale(2, RoundingMode.HALF_UP);

		// Taxa de Venda: 6% com Premium / 12% sem Premium
		BigDecimal aliquotaVenda = request.contaPremium()
				? TAXA_MERCADO_COM_PREMIUM
				: TAXA_MERCADO_SEM_PREMIUM;

		BigDecimal taxaVendaItens = receitaBrutaItens
				.multiply(aliquotaVenda)
				.setScale(2, RoundingMode.HALF_UP);

		// Taxa de Montagem de Ordem (Setup Fee): 2.5% apenas se for vendido via Ordem de Venda
		boolean ehOrdemDeVenda = request.ordemDeVenda() == null || request.ordemDeVenda();
		BigDecimal taxaMontagemOrdemAliquota = ehOrdemDeVenda ? TAXA_MONTAGEM_ORDEM : BigDecimal.ZERO;
		BigDecimal taxaMontagemItens = receitaBrutaItens
				.multiply(taxaMontagemOrdemAliquota)
				.setScale(2, RoundingMode.HALF_UP);

		BigDecimal receitaLiquidaItens = receitaBrutaItens
				.subtract(taxaVendaItens)
				.subtract(taxaMontagemItens)
				.setScale(2, RoundingMode.HALF_UP);

		// 4. Operação Completa com Diários de Artesão (Crafting Journals):
		BigDecimal custoDiariosVazios = BigDecimal.ZERO;
		BigDecimal receitaBrutaDiarios = BigDecimal.ZERO;
		BigDecimal taxaVendaDiarios = BigDecimal.ZERO;
		BigDecimal taxaMontagemDiarios = BigDecimal.ZERO;
		BigDecimal receitaLiquidaDiarios = BigDecimal.ZERO;
		BigDecimal lucroLiquidoDiarios = BigDecimal.ZERO;

		int qtdDiarios = request.quantidadeDiarios() != null ? Math.max(0, request.quantidadeDiarios()) : 0;
		if (qtdDiarios > 0) {
			BigDecimal precoCheio = request.precoDiarioCheio() != null && request.precoDiarioCheio().compareTo(BigDecimal.ZERO) > 0
					? request.precoDiarioCheio()
					: (request.valorVendaDiario() != null ? request.valorVendaDiario() : BigDecimal.ZERO);
			receitaBrutaDiarios = precoCheio.multiply(BigDecimal.valueOf(qtdDiarios)).setScale(2, RoundingMode.HALF_UP);

			BigDecimal precoVazio = request.precoDiarioVazio() != null ? request.precoDiarioVazio() : BigDecimal.ZERO;
			custoDiariosVazios = precoVazio.multiply(BigDecimal.valueOf(qtdDiarios)).setScale(2, RoundingMode.HALF_UP);

			taxaVendaDiarios = receitaBrutaDiarios.multiply(aliquotaVenda).setScale(2, RoundingMode.HALF_UP);
			taxaMontagemDiarios = receitaBrutaDiarios.multiply(taxaMontagemOrdemAliquota).setScale(2, RoundingMode.HALF_UP);
			receitaLiquidaDiarios = receitaBrutaDiarios.subtract(taxaVendaDiarios).subtract(taxaMontagemDiarios).setScale(2, RoundingMode.HALF_UP);
			lucroLiquidoDiarios = receitaLiquidaDiarios.subtract(custoDiariosVazios).setScale(2, RoundingMode.HALF_UP);
		}

		// 5. Consolidação de Custos, Taxas e Receitas:
		BigDecimal custoTotalProducao = custoInsumos
				.add(custoTaxaEstacao)
				.add(custoDiariosVazios)
				.setScale(2, RoundingMode.HALF_UP);

		BigDecimal taxaVendaTotal = taxaVendaItens.add(taxaVendaDiarios).setScale(2, RoundingMode.HALF_UP);
		BigDecimal taxaMontagemTotal = taxaMontagemItens.add(taxaMontagemDiarios).setScale(2, RoundingMode.HALF_UP);
		BigDecimal receitaLiquidaTotal = receitaLiquidaItens.add(receitaLiquidaDiarios).setScale(2, RoundingMode.HALF_UP);

		// 6. Lucro Líquido Final = Receita Líquida Total - Custo Total da Produção
		BigDecimal lucro = receitaLiquidaTotal.subtract(custoTotalProducao).setScale(2, RoundingMode.HALF_UP);

		// 7. Métrica de Prata por Ponto de Foco (Silver per Focus - SPF)
		BigDecimal prataPorFoco = BigDecimal.ZERO;
		if (Boolean.TRUE.equals(request.usarFoco()) && request.custoFocoTotal() != null && request.custoFocoTotal() > 0) {
			prataPorFoco = lucro
					.divide(BigDecimal.valueOf(request.custoFocoTotal()), 2, RoundingMode.HALF_UP);
		}

		CraftResponseDto response = new CraftResponseDto(
				custoTotalProducao,
				custosPorRecurso,
				lucro,
				custoTaxaEstacao,
				receitaLiquidaDiarios,
				custoDiariosVazios,
				lucroLiquidoDiarios,
				taxaMontagemTotal,
				taxaVendaTotal,
				receitaLiquidaTotal,
				prataPorFoco
		);

		log.info("[Classe: {}] [Metodo: {}] [Saida: {}]", 
				"CalculaViabilidadeDeProdcao", 
				"calcular", 
				response);

		return response;
	}
}
