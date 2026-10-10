package com.albion.api.infrastructure.adapter.out.persistence.mapper;

import com.albion.api.domain.model.CategoriaProducao;
import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.infrastructure.adapter.out.persistence.entity.IngredienteItemJpaEntity;
import com.albion.api.infrastructure.adapter.out.persistence.entity.ItemFabricadoJpaEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ItemFabricadoEntityMapper {

    public ItemFabricado paraDominio(ItemFabricadoJpaEntity entity) {
        if (entity == null) return null;

        List<Ingrediente> ingredientes = entity.getIngredientes() != null
                ? entity.getIngredientes().stream()
                .map(ing -> new Ingrediente(ing.getNome(), ing.getQuantidade(), ing.getValor()))
                .toList()
                : List.of();

        return new ItemFabricado(
                entity.getId(),
                entity.getNomeItem(),
                CategoriaProducao.deString(entity.getCategoriaProducao()),
                entity.getRendimentoPorClique() != null ? entity.getRendimentoPorClique() : 1,
                entity.getQuantidadeCliques(),
                entity.getTaxaRetorno(),
                entity.getPrecoVendaUnitario(),
                entity.isContaPremium(),
                entity.getTaxaEstacaoPorCemNutricao(),
                entity.getItemValue(),
                entity.getQuantidadeDiarios(),
                entity.getValorCompraDiarioVazio(),
                entity.getValorVendaDiarioCheio(),
                entity.isVendaInstantanea(),
                entity.isUsoFoco(),
                entity.getPontosFoco(),
                entity.getCustoTotalEstimado(),
                entity.getLucroEstimado(),
                entity.getDataCriacao(),
                ingredientes
        );
    }

    public ItemFabricadoJpaEntity paraJpaEntity(ItemFabricado dominio) {
        if (dominio == null) return null;

        ItemFabricadoJpaEntity entity = new ItemFabricadoJpaEntity();
        entity.setId(dominio.getId());
        entity.setNomeItem(dominio.getNomeItem());
        entity.setCategoriaProducao(dominio.getCategoriaProducao() != null ? dominio.getCategoriaProducao().name() : "CULINARIA");
        entity.setRendimentoPorClique(dominio.getRendimentoPorClique());
        entity.setQuantidadeCliques(dominio.getQuantidadeCliques());
        entity.setTaxaRetorno(dominio.getTaxaRetorno());
        entity.setPrecoVendaUnitario(dominio.getPrecoVendaUnitario());
        entity.setContaPremium(dominio.isContaPremium());
        entity.setTaxaEstacaoPorCemNutricao(dominio.getTaxaEstacaoPorCemNutricao());
        entity.setItemValue(dominio.getItemValue());
        entity.setQuantidadeDiarios(dominio.getQuantidadeDiarios());
        entity.setValorCompraDiarioVazio(dominio.getValorCompraDiarioVazio());
        entity.setValorVendaDiarioCheio(dominio.getValorVendaDiarioCheio());
        entity.setVendaInstantanea(dominio.isVendaInstantanea());
        entity.setUsoFoco(dominio.isUsoFoco());
        entity.setPontosFoco(dominio.getPontosFoco());
        entity.setCustoTotalEstimado(dominio.getCustoTotalEstimado());
        entity.setLucroEstimado(dominio.getLucroEstimado());
        entity.setDataCriacao(dominio.getDataCriacao());

        if (dominio.getIngredientes() != null) {
            for (Ingrediente ing : dominio.getIngredientes()) {
                IngredienteItemJpaEntity jpaIng = new IngredienteItemJpaEntity(
                        ing.getNome(),
                        ing.getQuantidade(),
                        ing.getValorUnitario()
                );
                entity.adicionarIngrediente(jpaIng);
            }
        }

        return entity;
    }
}
