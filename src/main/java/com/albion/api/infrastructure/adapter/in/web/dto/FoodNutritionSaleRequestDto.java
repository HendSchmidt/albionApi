package com.albion.api.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record FoodNutritionSaleRequestDto(
        String nomeComida,
        String tier,
        BigDecimal nutricaoPorUnidade,
        boolean comidaFavorita,
        BigDecimal valorPorCemNutricao,
        int quantidadeProducao,
        BigDecimal taxaDeRetorno,
        BigDecimal precoMercadoUnitario,
        boolean contaPremium,
        boolean ordemDeVenda,
        List<RecursoRequestDto> ingredientes
) {}
