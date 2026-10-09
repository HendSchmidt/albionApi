package com.albion.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record FoodNutritionSaleRequestDto(
    String nomeComida,
    String tier,
    BigDecimal nutricaoPorUnidade,
    boolean comidaFavorita,
    BigDecimal valorPorCemNutricao, // X configurado pelo dono da barraquinha
    Integer quantidadeProducao,     // Qtd total de unidades (ex: 10 por clique * cliques)
    BigDecimal taxaDeRetorno,       // TRR em % (ex: 15 ou 25 ou 48)
    BigDecimal precoMercadoUnitario,// Preço de venda unitário no mercado da cidade
    boolean contaPremium,           // Se tem conta premium (6.5% vs 10.5% ou 12%)
    boolean ordemDeVenda,           // true = Ordem de venda (inclui taxa 2.5%), false = Venda direta
    List<RecursoRequestDto> ingredientes // Lista de ingredientes para calcular custo real
) {}
