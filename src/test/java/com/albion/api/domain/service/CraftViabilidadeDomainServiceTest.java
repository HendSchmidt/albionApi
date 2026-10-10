package com.albion.api.domain.service;

import com.albion.api.domain.model.CategoriaProducao;
import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.domain.model.ViabilidadeCraft;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CraftViabilidadeDomainServiceTest {

    private CraftViabilidadeDomainService domainService;

    @BeforeEach
    void setUp() {
        domainService = new CraftViabilidadeDomainService();
    }

    @Test
    @DisplayName("Deve calcular operacao de Culinaria com rendimento de 10 unidades por clique no dominio puro")
    void deveCalcularCulinariaComLoteDeDezUnidadesPorClique() {
        ItemFabricado item = new ItemFabricado(
                1L,
                "Guisado de Carne T8",
                CategoriaProducao.CULINARIA,
                10,
                1,
                BigDecimal.valueOf(15),
                new BigDecimal("1000.00"),
                true,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                0,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                true, // vendaInstantanea = true -> ordemDeVenda = false
                false,
                0,
                null,
                null,
                null,
                List.of(
                        new Ingrediente("Carne Crua T8", 8, new BigDecimal("1000.00")),
                        new Ingrediente("Pao", 4, new BigDecimal("500.00"))
                )
        );

        ViabilidadeCraft resultado = domainService.calcular(item);

        assertNotNull(resultado);
        assertEquals(10, resultado.totalItensProduzidos());
        assertEquals(10, resultado.rendimentoPorClique());
        assertEquals(new BigDecimal("8500.00"), resultado.custoTotalProducao());
        assertEquals(new BigDecimal("850.00"), resultado.custoUnitarioItemFinal());
        assertEquals(new BigDecimal("9400.00"), resultado.receitaLiquidaTotal());
        assertEquals(new BigDecimal("900.00"), resultado.lucro());
        assertEquals(new BigDecimal("90.00"), resultado.lucroUnitarioItemFinal());
    }

    @Test
    @DisplayName("Deve calcular operacao completa com diarios e estacao de fabricacao")
    void deveCalcularOperacaoComDiariosCompletos() {
        ItemFabricado item = new ItemFabricado(
                2L,
                "Espada Larga T4",
                CategoriaProducao.EQUIPAMENTOS,
                1,
                5,
                BigDecimal.valueOf(25),
                new BigDecimal("6000.00"),
                true,
                new BigDecimal("500.00"),
                new BigDecimal("800.00"),
                2,
                new BigDecimal("1200.00"),
                new BigDecimal("5000.00"),
                true,
                true,
                1000,
                null,
                null,
                null,
                List.of(new Ingrediente("Barra de Ferro T4", 16, new BigDecimal("200.00")))
        );

        ViabilidadeCraft resultado = domainService.calcular(item);

        assertNotNull(resultado);
        assertEquals(new BigDecimal("2400.00"), resultado.custoDiariosVazios());
        assertEquals(new BigDecimal("9400.00"), resultado.receitaLiquidaDiarios());
        assertEquals(new BigDecimal("7000.00"), resultado.lucroLiquidoDiarios());
        assertEquals(new BigDecimal("16650.00"), resultado.custoTotalProducao());
        assertEquals(new BigDecimal("37600.00"), resultado.receitaLiquidaTotal());
        assertEquals(new BigDecimal("20950.00"), resultado.lucro());
    }

    @Test
    @DisplayName("Deve lancar excecao quando o item for nulo")
    void deveLancarExcecaoQuandoItemForNulo() {
        assertThrows(IllegalArgumentException.class, () -> domainService.calcular(null));
    }
}
