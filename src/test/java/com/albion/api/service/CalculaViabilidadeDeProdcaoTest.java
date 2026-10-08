package com.albion.api.service;

import com.albion.api.dto.CraftRequestDto;
import com.albion.api.dto.CraftResponseDto;
import com.albion.api.dto.RecursoRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculaViabilidadeDeProdcaoTest {

	private CalculaViabilidadeDeProdcao service;

	@BeforeEach
	void setUp() {
		service = new CalculaViabilidadeDeProdcao();
	}

	@Test
	@DisplayName("Deve calcular com taxa da estação, diários de artesão e venda instantânea")
	void deveCalcularComTaxaEstacaoEDiarios() {
		CraftRequestDto request = new CraftRequestDto(
				List.of(new RecursoRequestDto("Barra de Ferro T4", 16, new BigDecimal("200.00"))),
				5,                                    // 5 itens
				25,                                   // 25% de retorno
				new BigDecimal("6000.00"),            // Preço de venda = 6.000 (Bruto = 30.000)
				true,                                 // Conta Premium = true (6% taxa de venda)
				new BigDecimal("500.00"),             // Taxa da estação por 100 de nutrição = 500
				new BigDecimal("800.00"),             // Item Value = 800
				2,                                    // 2 diários cheios
				new BigDecimal("3500.00"),            // Cada diário vende por 3.500 (Total diários = 7.000)
				false,                                // Venda Instantânea (sem taxa de montagem de 2.5%)
				true,                                 // Usou foco
				1000                                  // 1000 pontos de foco
		);

		CraftResponseDto response = service.calcular(request);

		assertNotNull(response);
		// Insumos: 16 * 5 = 80. Consumo efetivo = 80 * 0.75 = 60 barras * 200 = 12.000
		// Nutrição: 800 * 0.1125 * 5 = 450. Taxa da estação: (450 / 100) * 500 = 2.250
		// Custo Total: 12.000 + 2.250 = 14.250
		assertEquals(new BigDecimal("14250.00"), response.custoTotalDaProdcao());
		assertEquals(new BigDecimal("2250.00"), response.custoTaxaEstacao());
		assertEquals(new BigDecimal("7000.00"), response.receitaDiarios());
		assertEquals(new BigDecimal("0.00"), response.taxaMontagemOrdem());
		// Taxa venda: 30.000 * 6% = 1.800
		assertEquals(new BigDecimal("1800.00"), response.taxaVendaMercado());
		// Receita líquida: (30.000 - 1.800) + 7.000 = 35.200
		assertEquals(new BigDecimal("35200.00"), response.receitaLiquidaTotal());
		// Lucro: 35.200 - 14.250 = 20.950
		assertEquals(new BigDecimal("20950.00"), response.lucro());
		// SPF: 20.950 / 1000 = 20.95
		assertEquals(new BigDecimal("20.95"), response.prataPorFoco());
	}
}
