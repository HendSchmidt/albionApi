package com.albion.api.service;

import com.albion.api.dto.FoodNutritionSaleRequestDto;
import com.albion.api.dto.FoodNutritionSaleResponseDto;
import com.albion.api.dto.RecursoRequestDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CalculaNutricaoBarraquinhaServiceTest {

    private final CalculaNutricaoBarraquinhaService service = new CalculaNutricaoBarraquinhaService();

    @Test
    @DisplayName("Deve calcular corretamente o exemplo da Sopa de Repolho T5")
    void deveCalcularSopaDeRepolhoT5() {
        // Exemplo da solicitação:
        // Nutrição = 432
        // X = 300 por 100 de nutrição -> Valor = (432 / 100) * 300 = 1296 prata por unidade
        // Mercado = 1400 -> taxa 6.5% -> 1400 * 0.935 = 1309 líquido
        // Como 1309 > 1296 -> Mercado é melhor!
        FoodNutritionSaleRequestDto request = new FoodNutritionSaleRequestDto(
                "Sopa de Repolho T5",
                "T5",
                new BigDecimal("432"),
                false,
                new BigDecimal("300"),
                10,
                new BigDecimal("15"),
                new BigDecimal("1400"),
                true,
                true,
                List.of(new RecursoRequestDto("Repolho", 144, new BigDecimal("70.00")))
        );

        FoodNutritionSaleResponseDto response = service.calcular(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("1296.00"), response.valorPagoPorUnidadeBarraquinha());
        assertEquals(new BigDecimal("1309.00"), response.precoLiquidoMercadoUnitario());
        assertEquals("MERCADO", response.melhorOpcao());
    }

    @Test
    @DisplayName("Deve indicar BARRAQUINHA quando ela paga mais que o mercado liquido")
    void deveIndicarBarraquinhaQuandoMaisRentavel() {
        // Nutrição = 432, X = 350 -> (4.32) * 350 = 1512 por unidade
        // Mercado = 1400 -> 1400 * 0.935 = 1309
        FoodNutritionSaleRequestDto request = new FoodNutritionSaleRequestDto(
                "Sopa de Repolho T5",
                "T5",
                new BigDecimal("432"),
                false,
                new BigDecimal("350"),
                10,
                new BigDecimal("15"),
                new BigDecimal("1400"),
                true,
                true,
                List.of(new RecursoRequestDto("Repolho", 144, new BigDecimal("70.00")))
        );

        FoodNutritionSaleResponseDto response = service.calcular(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("1512.00"), response.valorPagoPorUnidadeBarraquinha());
        assertEquals("BARRAQUINHA", response.melhorOpcao());
    }

    @Test
    @DisplayName("Deve dobrar nutricao quando for comida favorita da bancada")
    void deveDobrarNutricaoComComidaFavorita() {
        FoodNutritionSaleRequestDto request = new FoodNutritionSaleRequestDto(
                "Sopa de Cenoura T1",
                "T1",
                new BigDecimal("48"),
                true, // comida favorita = 96
                new BigDecimal("200"),
                10,
                new BigDecimal("15"),
                new BigDecimal("120"),
                true,
                true,
                List.of(new RecursoRequestDto("Cenoura", 16, new BigDecimal("25.00")))
        );

        FoodNutritionSaleResponseDto response = service.calcular(request);

        assertNotNull(response);
        assertEquals(new BigDecimal("96"), response.nutricaoEfetivaPorUnidade());
        assertEquals(new BigDecimal("192.00"), response.valorPagoPorUnidadeBarraquinha());
    }
}
