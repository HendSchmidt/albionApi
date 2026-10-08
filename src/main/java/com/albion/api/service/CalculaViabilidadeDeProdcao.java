package com.albion.api.service;

import com.albion.api.dto.CraftRequestDto;
import com.albion.api.dto.CraftResponseDto;
import com.albion.api.dto.RecursoRequestDto;
import com.albion.api.dto.RecursoResponseDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class CalculaViabilidadeDeProdcao {

	// Taxas do mercado:
	// Com Conta Premium: 6% de taxa (0.06).
	// Sem Conta Premium: 12% de taxa (0.12), pois a Conta Premium equivale a 6% de desconto no mercado.
	private static final BigDecimal TAXA_MERCADO_COM_PREMIUM = new BigDecimal("0.06");
	private static final BigDecimal TAXA_MERCADO_SEM_PREMIUM = new BigDecimal("0.12");
	private static final BigDecimal CEM = new BigDecimal("100");

	/**
	 * Calcula o custo por recurso com a taxa de retorno, o custo total de fabricação
	 * e o lucro líquido após o desconto de taxa de mercado com base na conta premium.
	 *
	 * @param request DTO com recursos, quantidade para produção, taxa de retorno, preço de venda e status premium.
	 * @return CraftResponseDto com custo total, detalhamento por recurso e lucro líquido.
	 */
	public CraftResponseDto calcular(CraftRequestDto request) {
		if (request == null || request.recurso() == null) {
			throw new IllegalArgumentException("O payload de fabricação e a lista de recursos não podem ser nulos.");
		}

		int quantidadeProducao = Math.max(1, request.quantidadeParaProducao());
		int taxaRetorno = Math.max(0, request.taxaDeRetorno());

		// Fator de retorno: taxaRetorno / 100 (ex: 20% -> 0.2000)
		BigDecimal fatorRetorno = BigDecimal.valueOf(taxaRetorno)
				.divide(CEM, 4, RoundingMode.HALF_UP);

		// Fator de consumo efetivo: 1 - fatorRetorno (ex: 1 - 0.20 = 0.80)
		BigDecimal fatorConsumo = BigDecimal.ONE.subtract(fatorRetorno);

		List<RecursoResponseDto> custosPorRecurso = new ArrayList<>();
		BigDecimal custoTotalProducao = BigDecimal.ZERO;

		// 1. Calcula o custo de cada recurso considerando o retorno do jogo
		for (RecursoRequestDto recurso : request.recurso()) {
			if (recurso == null) continue;

			// Quantidade bruta total necessária para iniciar o lote
			BigDecimal qtdTotal = BigDecimal.valueOf((long) recurso.quantidade() * quantidadeProducao);

			// Quantidade líquida gasta (após a devolução pela taxa de retorno)
			BigDecimal qtdConsumidaEfetiva = qtdTotal.multiply(fatorConsumo);

			// Custo do recurso = quantidade líquida * valor unitário
			BigDecimal valorUnitario = recurso.valor() != null ? recurso.valor() : BigDecimal.ZERO;
			BigDecimal custoRecurso = qtdConsumidaEfetiva
					.multiply(valorUnitario)
					.setScale(2, RoundingMode.HALF_UP);

			custosPorRecurso.add(new RecursoResponseDto(recurso.nome(), custoRecurso));
			custoTotalProducao = custoTotalProducao.add(custoRecurso);
		}

		// 2. Cálculo da Receita Bruta da Venda
		BigDecimal precoVenda = request.precoDeVenda() != null ? request.precoDeVenda() : BigDecimal.ZERO;
		BigDecimal receitaBruta = precoVenda
				.multiply(BigDecimal.valueOf(quantidadeProducao))
				.setScale(2, RoundingMode.HALF_UP);

		// 3. Regra da Conta Premium e Taxa de Mercado:
		// Com Conta Premium: taxa é de 6% (0.06).
		// Sem Conta Premium: taxa é de 12% (0.12) (6% a mais sem o desconto premium).
		BigDecimal taxaMercado = request.contaPremium()
				? TAXA_MERCADO_COM_PREMIUM
				: TAXA_MERCADO_SEM_PREMIUM;

		BigDecimal valorTaxaMercado = receitaBruta
				.multiply(taxaMercado)
				.setScale(2, RoundingMode.HALF_UP);

		BigDecimal receitaLiquida = receitaBruta.subtract(valorTaxaMercado);

		// 4. Lucro Líquido = Receita Líquida - Custo Total da Produção
		BigDecimal lucro = receitaLiquida
				.subtract(custoTotalProducao)
				.setScale(2, RoundingMode.HALF_UP);

		return new CraftResponseDto(
				custoTotalProducao,
				custosPorRecurso,
				lucro
		);
	}
}
