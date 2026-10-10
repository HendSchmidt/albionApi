package com.albion.api.domain.service;

import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.VendaBarraquinha;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NutricaoBarraquinhaDomainServiceTest {

    private final NutricaoBarraquinhaDomainService domainService = new NutricaoBarraquinhaDomainService();

    @Test
    @DisplayName("Deve calcular corretamente comparacao da Sopa de Repolho T5")
    void deveCalcularSopaDeRepolhoT5() {
        VendaBarraquinha resultado = domainService.calcular(
                "Sopa de Repolho T5",
                new BigDecimal("432"),
                false,
                new BigDecimal("300"),
                10,
                new BigDecimal("15"),
                new BigDecimal("1400"),
                true,
                true,
                List.of(new Ingrediente("Repolho", 144, new BigDecimal("70.00")))
        );

        assertNotNull(resultado);
        assertEquals(new BigDecimal("1296.00"), resultado.valorPagoPorUnidadeBarraquinha());
        assertEquals(new BigDecimal("1309.00"), resultado.precoLiquidoMercadoUnitario());
        assertEquals("MERCADO", resultado.melhorOpcao());
    }

    @Test
    @DisplayName("Deve indicar BARRAQUINHA quando ela paga mais que o mercado liquido")
    void deveIndicarBarraquinhaQuandoMaisRentavel() {
        VendaBarraquinha resultado = domainService.calcular(
                "Sopa de Repolho T5",
                new BigDecimal("432"),
                false,
                new BigDecimal("350"),
                10,
                new BigDecimal("15"),
                new BigDecimal("1400"),
                true,
                true,
                List.of(new Ingrediente("Repolho", 144, new BigDecimal("70.00")))
        );

        assertNotNull(resultado);
        assertEquals(new BigDecimal("1512.00"), resultado.valorPagoPorUnidadeBarraquinha());
        assertEquals("BARRAQUINHA", resultado.melhorOpcao());
    }

    @Test
    @DisplayName("Deve dobrar nutricao quando for comida favorita da bancada")
    void deveDobrarNutricaoComComidaFavorita() {
        VendaBarraquinha resultado = domainService.calcular(
                "Sopa de Cenoura T1",
                new BigDecimal("48"),
                true,
                new BigDecimal("200"),
                10,
                new BigDecimal("15"),
                new BigDecimal("120"),
                true,
                true,
                List.of(new Ingrediente("Cenoura", 16, new BigDecimal("25.00")))
        );

        assertNotNull(resultado);
        assertEquals(new BigDecimal("96"), resultado.nutricaoEfetiva());
        assertEquals(new BigDecimal("192.00"), resultado.valorPagoPorUnidadeBarraquinha());
    }
}
