package com.albion.api.service;

import com.albion.api.dto.CraftRequestDto;
import com.albion.api.dto.CraftResponseDto;
import com.albion.api.dto.RecursoRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CalculaViabilidadeDeProdcaoTest {

	private CalculaViabilidadeDeProdcao service;

	@BeforeEach
	void setUp() {
		service = new CalculaViabilidadeDeProdcao();
	}

	@Test
	@DisplayName("Deve calcular operacao de Culinaria com rendimento de 10 unidades por clique")
	void deveCalcularCulinariaComLoteDeDezUnidadesPorClique() {
		// 1 clique de Guisado de Carne gerando 10 unidades
		CraftRequestDto request = new CraftRequestDto(
				List.of(
						new RecursoRequestDto("Carne Crua T8", 8, new BigDecimal("1000.00")),
						new RecursoRequestDto("Pao", 4, new BigDecimal("500.00"))
				),
				1,                                    // 1 clique
				15,                                   // 15% TRR -> Custo corrigido: 10.000 * 0.85 = 8.500
				new BigDecimal("1000.00"),            // Preço de venda = 1.000 por guisado no mercado
				true,                                 // Conta Premium (6%)
				BigDecimal.ZERO,                      // Sem taxa de estação
				BigDecimal.ZERO,
				0,
				BigDecimal.ZERO,
				BigDecimal.ZERO,
				null,
				false,                                // Venda direta (0% setup fee, apenas 6% mercado)
				false,
				0,
				10,                                   // 1 clique = 10 guisados produzidos!
				"CULINARIA"
		);

		CraftResponseDto response = service.calcular(request);
		assertNotNull(response);

		// Total de itens: 1 clique * 10 = 10 guisados
		assertEquals(10, response.totalItensProduzidos());
		assertEquals(10, response.rendimentoPorClique());

		// Custo total da produção: (8*1000 + 4*500) * 0.85 = 10.000 * 0.85 = 8.500
		assertEquals(new BigDecimal("8500.00"), response.custoTotalDaProdcao());

		// Custo por guisado individual: 8.500 / 10 = 850
		assertEquals(new BigDecimal("850.00"), response.custoUnitarioItemFinal());

		// Receita Bruta = 10 guisados * 1.000 = 10.000. Taxa 6% = 600. Receita Líquida = 9.400
		assertEquals(new BigDecimal("9400.00"), response.receitaLiquidaTotal());

		// Lucro: 9.400 - 8.500 = +900 de prata
		assertEquals(new BigDecimal("900.00"), response.lucro());

		// Lucro unitário: 900 / 10 = +90 por guisado
		assertEquals(new BigDecimal("90.00"), response.lucroUnitarioItemFinal());
	}

	@Test
	@DisplayName("Deve calcular operação completa com compra de diário vazio e venda do diário cheio")
	void deveCalcularOperacaoComDiariosCompletos() {
		CraftRequestDto request = new CraftRequestDto(
				List.of(new RecursoRequestDto("Barra de Ferro T4", 16, new BigDecimal("200.00"))),
				5,                                    // 5 itens
				25,                                   // 25% de retorno -> 60 barras gastas = 12.000
				new BigDecimal("6000.00"),            // Preço de venda = 6.000 (Bruto = 30.000)
				true,                                 // Conta Premium = true (6% taxa de venda)
				new BigDecimal("500.00"),             // Taxa da estação por 100 de nutrição = 500
				new BigDecimal("800.00"),             // Item Value = 800 -> Nutrição = 450 -> Taxa = 2.250
				2,                                    // 2 diários
				new BigDecimal("1200.00"),            // Compra do diário vazio = 1.200 cada (Total = 2.400)
				new BigDecimal("5000.00"),            // Venda do diário cheio = 5.000 cada (Total bruto = 10.000)
				null,                                 // valorVendaDiario
				false,                                // Venda Instantânea (sem taxa de montagem de 2.5%)
				true,                                 // Usou foco
				1000                                  // 1000 pontos de foco
		);

		CraftResponseDto response = service.calcular(request);
		assertNotNull(response);

		// Custo de compra dos vazios: 2 * 1200 = 2.400
		assertEquals(new BigDecimal("2400.00"), response.custoDiariosVazios());

		// Venda cheios: 2 * 5000 = 10.000. Taxa 6% = 600. Receita líquida diários = 9.400
		assertEquals(new BigDecimal("9400.00"), response.receitaDiarios());

		// Lucro limpo dos diários: 9.400 - 2.400 = 7.000
		assertEquals(new BigDecimal("7000.00"), response.lucroLiquidoDiarios());

		// Custo total: 12.000 (insumos) + 2.250 (estação) + 2.400 (diários vazios) = 16.650
		assertEquals(new BigDecimal("16650.00"), response.custoTotalDaProdcao());

		// Itens: Venda 30.000 - 6% (1.800) = 28.200
		// Receita líquida total: 28.200 (itens) + 9.400 (diários) = 37.600
		assertEquals(new BigDecimal("37600.00"), response.receitaLiquidaTotal());

		// Lucro: 37.600 - 16.650 = 20.950
		assertEquals(new BigDecimal("20950.00"), response.lucro());
	}

	@Test
	@DisplayName("Deve lançar exceção quando o request for nulo")
	void deveLancarExcecaoQuandoRequestForNulo() {
		assertThrows(IllegalArgumentException.class, () -> service.calcular(null));
	}
}
