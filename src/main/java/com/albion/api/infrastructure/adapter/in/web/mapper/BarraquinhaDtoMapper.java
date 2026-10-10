package com.albion.api.infrastructure.adapter.in.web.mapper;

import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.VendaBarraquinha;
import com.albion.api.infrastructure.adapter.in.web.dto.FoodNutritionSaleResponseDto;
import com.albion.api.infrastructure.adapter.in.web.dto.RecursoRequestDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class BarraquinhaDtoMapper {

    public List<Ingrediente> paraIngredientes(List<RecursoRequestDto> recursos) {
        if (recursos == null) return List.of();
        List<Ingrediente> resultado = new ArrayList<>();
        for (RecursoRequestDto r : recursos) {
            if (r != null && r.nome() != null && !r.nome().trim().isEmpty()) {
                resultado.add(new Ingrediente(
                        r.nome(),
                        r.quantidade(),
                        r.valor() != null ? r.valor() : BigDecimal.ZERO
                ));
            }
        }
        return resultado;
    }

    public FoodNutritionSaleResponseDto paraDto(VendaBarraquinha domain) {
        if (domain == null) return null;

        return new FoodNutritionSaleResponseDto(
                domain.nomeComida(),
                domain.nutricaoEfetiva(),
                domain.valorPagoPorUnidadeBarraquinha(),
                domain.receitaTotalBarraquinha(),
                domain.custoProducaoTotal(),
                domain.custoProducaoPorUnidade(),
                domain.lucroTotalBarraquinha(),
                domain.lucroUnitarioBarraquinha(),
                domain.precoMercadoUnitario(),
                domain.taxaMercadoPercentual(),
                domain.precoLiquidoMercadoUnitario(),
                domain.receitaLiquidaTotalMercado(),
                domain.lucroTotalMercado(),
                domain.lucroUnitarioMercado(),
                domain.melhorOpcao(),
                domain.recomendacao(),
                domain.diferenca()
        );
    }
}
