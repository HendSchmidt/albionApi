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
	@DisplayName("Deve calcular viabilidade com taxa de retorno de 20% e Conta Premium (desconto de 6% no mercado)")
	void deveCalcularComRetornoEContaPremium() {
		CraftRequestDto request = new CraftRequestDto(
				List.of(new RecursoRequestDto("Barra de Ferro", 10, new BigDecimal("100.00"))),
				1,
				20,
				new BigDecimal("1200.00"),
				true
		);

		CraftResponseDto response = service.calcular(request);

		assertNotNull(response);
		assertEquals(new BigDecimal("800.00"), response.custoTotalDaProdcao());
		assertEquals(1, response.custoPorRecurso().size());
		assertEquals("Barra de Ferro", response.custoPorRecurso().get(0).nome());
		assertEquals(new BigDecimal("800.00"), response.custoPorRecurso().get(0).valor());
		assertEquals(new BigDecimal("400.00"), response.lucro());
	}

	@Test
	@DisplayName("Deve aplicar 6% de taxa de mercado quando Conta Premium for falsa")
	void deveCalcularSemContaPremiumComTaxaDeMercado() {
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
		assertEquals(new BigDecimal("140.00"), response.lucro());
	}
}
