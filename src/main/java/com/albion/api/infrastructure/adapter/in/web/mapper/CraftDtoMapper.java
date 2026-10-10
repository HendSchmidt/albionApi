package com.albion.api.infrastructure.adapter.in.web.mapper;

import com.albion.api.domain.model.CategoriaProducao;
import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.domain.model.ViabilidadeCraft;
import com.albion.api.infrastructure.adapter.in.web.dto.CraftRequestDto;
import com.albion.api.infrastructure.adapter.in.web.dto.CraftResponseDto;
import com.albion.api.infrastructure.adapter.in.web.dto.RecursoRequestDto;
import com.albion.api.infrastructure.adapter.in.web.dto.RecursoResponseDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class CraftDtoMapper {

    public ItemFabricado paraDominio(CraftRequestDto dto) {
        if (dto == null) return null;

        List<Ingrediente> ingredientes = new ArrayList<>();
        if (dto.recurso() != null) {
            for (RecursoRequestDto r : dto.recurso()) {
                if (r != null && r.nome() != null && !r.nome().trim().isEmpty()) {
                    ingredientes.add(new Ingrediente(
                            r.nome(),
                            r.quantidade(),
                            r.valor() != null ? r.valor() : BigDecimal.ZERO
                    ));
                }
            }
        }

        int rendimento = dto.rendimentoPorClique() != null && dto.rendimentoPorClique() > 0
                ? dto.rendimentoPorClique()
                : 1;

        CategoriaProducao cat = CategoriaProducao.deString(dto.categoriaProducao());
        if (dto.categoriaProducao() == null || dto.categoriaProducao().isBlank()) {
            cat = rendimento == 10 ? CategoriaProducao.CULINARIA : (rendimento == 5 ? CategoriaProducao.ALQUIMIA : CategoriaProducao.EQUIPAMENTOS);
        }

        BigDecimal precoCheio = dto.precoDiarioCheio() != null ? dto.precoDiarioCheio() : dto.valorVendaDiario();

        return new ItemFabricado(
                null,
                dto.nomeItem(),
                cat,
                rendimento,
                dto.quantidadeParaProducao(),
                BigDecimal.valueOf(dto.taxaDeRetorno()),
                dto.precoDeVenda(),
                dto.contaPremium(),
                dto.taxaEstacaoPorCemNutricao(),
                dto.itemValue(),
                dto.quantidadeDiarios(),
                dto.precoDiarioVazio(),
                precoCheio,
                Boolean.FALSE.equals(dto.ordemDeVenda()),
                Boolean.TRUE.equals(dto.usarFoco()),
                dto.custoFocoTotal(),
                null,
                null,
                null,
                ingredientes
        );
    }

    public CraftResponseDto paraDto(ViabilidadeCraft domain) {
        if (domain == null) return null;

        List<RecursoResponseDto> recursos = domain.custosPorRecurso() != null
                ? domain.custosPorRecurso().stream()
                .map(c -> new RecursoResponseDto(c.nome(), c.custoTotal()))
                .toList()
                : List.of();

        return new CraftResponseDto(
                domain.custoTotalProducao(),
                recursos,
                domain.lucro(),
                domain.custoTaxaEstacao(),
                domain.receitaLiquidaDiarios(),
                domain.custoDiariosVazios(),
                domain.lucroLiquidoDiarios(),
                domain.taxaMontagemTotal(),
                domain.taxaVendaTotal(),
                domain.receitaLiquidaTotal(),
                domain.prataPorFoco(),
                domain.totalItensProduzidos(),
                domain.rendimentoPorClique(),
                domain.custoUnitarioItemFinal(),
                domain.lucroUnitarioItemFinal(),
                domain.categoriaProducao()
        );
    }
}
