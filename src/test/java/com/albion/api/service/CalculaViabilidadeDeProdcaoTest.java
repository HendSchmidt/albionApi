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
	@DisplayName("Deve calcular viabilidade com taxa de retorno de 20% e taxa de 6% quando possui Conta Premium")
	void deveCalcularComRetornoEContaPremium() {
		// Cenário:
		// 1 item, 10 barras de ferro (valor unitário 100.00).
		// Taxa de Retorno = 20% -> Consumo efetivo = 8 barras = 800.00 de custo.
		// Preço de venda = 1000.00.
		// Com Conta Premium -> taxa de mercado de 6% = 60.00 de taxa.
		// Receita líquida = 1000.00 - 60.00 = 940.00.
		// Lucro líquido = 940.00 - 800.00 = 140.00.
		CraftRequestDto request = new CraftRequestDto(
				List.of(new RecursoRequestDto("Barra de Ferro", 10, new BigDecimal("100.00"))),
				1,
				20,
				new BigDecimal("1000.00"),
				true
		);

		CraftResponseDto response = service.calcular(request);

		assertNotNull(response);
		assertEquals(new BigDecimal("800.00"), response.custoTotalDaProdcao());
		assertEquals(1, response.custoPorRecurso().size());
		assertEquals("Barra de Ferro", response.custoPorRecurso().get(0).nome());
		assertEquals(new BigDecimal("800.00"), response.custoPorRecurso().get(0).valor());
		assertEquals(new BigDecimal("140.00"), response.lucro());
	}

	@Test
	@DisplayName("Deve aplicar 12% de taxa de mercado quando NÃO possui Conta Premium (sem o desconto de 6%)")
	void deveCalcularSemContaPremiumComTaxaDeMercado() {
		// Cenário:
		// Custo = 800.00.
		// Preço de venda = 1000.00.
		// Sem Premium -> Taxa de mercado de 12% = 120.00.
		// Receita líquida = 1000.00 - 120.00 = 880.00.
		// Lucro líquido = 880.00 - 800.00 = 80.00.
		CraftRequestDto request = new CraftRequestDto(
				List.of(new RecursoRequestDto("Barra de Ferro", 10, new BigDecimal("100.00"))),
				1,
				20,
				new BigDecimal("1000.00"),
				false
		);

		CraftResponseDto response = service.calcular(request);

		assertNotNull(response);
		assertEquals(new BigDecimal("800.00"), response.custoTotalDaProdcao());
		assertEquals(new BigDecimal("80.00"), response.lucro());
	}
}
