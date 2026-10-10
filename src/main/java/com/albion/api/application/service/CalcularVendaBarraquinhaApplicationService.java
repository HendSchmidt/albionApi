package com.albion.api.application.service;

import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.VendaBarraquinha;
import com.albion.api.domain.port.in.CalcularVendaBarraquinhaUseCase;
import com.albion.api.domain.service.NutricaoBarraquinhaDomainService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Caso de uso orquestrando o cálculo de venda para barraquinha vs mercado.
 */
@Service
public class CalcularVendaBarraquinhaApplicationService implements CalcularVendaBarraquinhaUseCase {

    private final NutricaoBarraquinhaDomainService domainService;

    public CalcularVendaBarraquinhaApplicationService() {
        this.domainService = new NutricaoBarraquinhaDomainService();
    }

    public CalcularVendaBarraquinhaApplicationService(NutricaoBarraquinhaDomainService domainService) {
        this.domainService = domainService;
    }

    @Override
    public VendaBarraquinha calcular(String nomeComida,
                                    BigDecimal nutricaoPorUnidade,
                                    boolean comidaFavorita,
                                    BigDecimal valorPorCemNutricao,
                                    int quantidadeProducao,
                                    BigDecimal taxaDeRetorno,
                                    BigDecimal precoMercadoUnitario,
                                    boolean contaPremium,
                                    boolean ordemDeVenda,
                                    List<Ingrediente> ingredientes) {
        return domainService.calcular(
                nomeComida,
                nutricaoPorUnidade,
                comidaFavorita,
                valorPorCemNutricao,
                quantidadeProducao,
                taxaDeRetorno,
                precoMercadoUnitario,
                contaPremium,
                ordemDeVenda,
                ingredientes
        );
    }
}
