package com.albion.api.infrastructure.adapter.in.web.mapper;

import com.albion.api.domain.model.CategoriaProducao;
import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.infrastructure.adapter.in.web.dto.CraftRequestDto;
import com.albion.api.infrastructure.adapter.in.web.dto.ItemSalvoResponseDto;
import com.albion.api.infrastructure.adapter.in.web.dto.RecursoRequestDto;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Component
public class ItemFabricadoDtoMapper {

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

        String nome = (dto.nomeItem() != null && !dto.nomeItem().trim().isEmpty())
                ? dto.nomeItem().trim()
                : "Receita Customizada";

        CategoriaProducao cat = CategoriaProducao.deString(dto.categoriaProducao());
        int rendimento = dto.rendimentoPorClique() != null && dto.rendimentoPorClique() > 0
                ? dto.rendimentoPorClique()
                : 1;

        return new ItemFabricado(
                null,
                nome,
                cat,
                rendimento,
                dto.quantidade(),
                BigDecimal.valueOf(dto.taxaRetorno()),
                dto.valorVenda(),
                dto.contaPremium(),
                dto.taxaEstacaoPorCemNutricao(),
                dto.itemValue(),
                dto.quantidadeDiarios(),
                dto.valorCompraDiarioVazio(),
                dto.valorVendaDiarioCheio(),
                dto.vendaInstantanea(),
                dto.usoFoco(),
                dto.pontosFoco(),
                null,
                null,
                null,
                ingredientes
        );
    }

    public ItemSalvoResponseDto paraDto(ItemFabricado domain) {
        if (domain == null) return null;

        List<RecursoRequestDto> recursos = domain.getIngredientes() != null
                ? domain.getIngredientes().stream()
                .map(ing -> new RecursoRequestDto(ing.getNome(), ing.getQuantidade(), ing.getValorUnitario()))
                .toList()
                : List.of();

        return new ItemSalvoResponseDto(
                domain.getId(),
                domain.getNomeItem(),
                domain.getCategoriaProducao() != null ? domain.getCategoriaProducao().name() : "CULINARIA",
                domain.getRendimentoPorClique(),
                domain.getQuantidadeCliques(),
                domain.getTaxaRetorno(),
                domain.getPrecoVendaUnitario(),
                domain.isContaPremium(),
                domain.getTaxaEstacaoPorCemNutricao(),
                domain.getItemValue(),
                domain.getQuantidadeDiarios(),
                domain.getValorCompraDiarioVazio(),
                domain.getValorVendaDiarioCheio(),
                domain.isVendaInstantanea(),
                domain.isUsoFoco(),
                domain.getPontosFoco(),
                domain.getCustoTotalEstimado(),
                domain.getLucroEstimado(),
                domain.getDataCriacao(),
                recursos
        );
    }
}
