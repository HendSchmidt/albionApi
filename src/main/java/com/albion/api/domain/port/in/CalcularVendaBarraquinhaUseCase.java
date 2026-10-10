package com.albion.api.domain.port.in;

import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.VendaBarraquinha;

import java.math.BigDecimal;
import java.util.List;

/**
 * Driver Port (Inbound) para execução do caso de uso de venda de comida para barraquinhas.
 */
public interface CalcularVendaBarraquinhaUseCase {
    VendaBarraquinha calcular(String nomeComida,
                             BigDecimal nutricaoPorUnidade,
                             boolean comidaFavorita,
                             BigDecimal valorPorCemNutricao,
                             int quantidadeProducao,
                             BigDecimal taxaDeRetorno,
                             BigDecimal precoMercadoUnitario,
                             boolean contaPremium,
                             boolean ordemDeVenda,
                             List<Ingrediente> ingredientes);
}
