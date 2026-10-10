package com.albion.api.domain.service;

import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.VendaBarraquinha;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

/**
 * Domain Service que avalia a tomada de decisão entre vender comida diretamente
 * para barraquinha (nutrição) versus vender no mercado público do Albion Online.
 */
public class NutricaoBarraquinhaDomainService {

    private static final Logger log = LoggerFactory.getLogger(NutricaoBarraquinhaDomainService.class);

    private static final BigDecimal CEM = new BigDecimal("100");
    private static final BigDecimal DOIS = new BigDecimal("2");
    private static final BigDecimal UM = BigDecimal.ONE;

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
        log.info("[DomainService: NutricaoBarraquinhaDomainService] [Calculando: {}]", nomeComida);

        BigDecimal nutricaoBase = nutricaoPorUnidade != null ? nutricaoPorUnidade : BigDecimal.ZERO;
        BigDecimal nutricaoEfetiva = comidaFavorita ? nutricaoBase.multiply(DOIS) : nutricaoBase;

        BigDecimal valorPorCem = valorPorCemNutricao != null ? valorPorCemNutricao : BigDecimal.ZERO;
        BigDecimal fatorNutricao = nutricaoEfetiva.divide(CEM, 4, RoundingMode.HALF_UP);
        BigDecimal valorPagoPorUnidadeBarraquinha = fatorNutricao.multiply(valorPorCem).setScale(2, RoundingMode.HALF_UP);

        int qtdTotal = Math.max(1, quantidadeProducao);
        BigDecimal qtdTotalBd = BigDecimal.valueOf(qtdTotal);

        BigDecimal receitaTotalBarraquinha = valorPagoPorUnidadeBarraquinha.multiply(qtdTotalBd).setScale(2, RoundingMode.HALF_UP);

        BigDecimal trr = (taxaDeRetorno != null ? taxaDeRetorno : BigDecimal.ZERO).divide(CEM, 4, RoundingMode.HALF_UP);
        BigDecimal fatorCustoInsumo = UM.subtract(trr);
        if (fatorCustoInsumo.compareTo(BigDecimal.ZERO) < 0) {
            fatorCustoInsumo = BigDecimal.ZERO;
        }

        BigDecimal custoInsumosTotal = BigDecimal.ZERO;
        if (ingredientes != null) {
            for (Ingrediente ing : ingredientes) {
                if (ing != null && ing.getQuantidade() > 0 && ing.getValorUnitario() != null) {
                    BigDecimal preco = ing.getValorUnitario();
                    BigDecimal qtd = BigDecimal.valueOf(ing.getQuantidade());
                    BigDecimal custoItem = preco.multiply(qtd).multiply(fatorCustoInsumo);
                    custoInsumosTotal = custoInsumosTotal.add(custoItem);
                }
            }
        }

        BigDecimal custoProducaoTotal = custoInsumosTotal.setScale(2, RoundingMode.HALF_UP);
        BigDecimal custoProducaoPorUnidade = custoProducaoTotal.divide(qtdTotalBd, 2, RoundingMode.HALF_UP);

        BigDecimal lucroTotalBarraquinha = receitaTotalBarraquinha.subtract(custoProducaoTotal).setScale(2, RoundingMode.HALF_UP);
        BigDecimal lucroUnitarioBarraquinha = valorPagoPorUnidadeBarraquinha.subtract(custoProducaoPorUnidade).setScale(2, RoundingMode.HALF_UP);

        BigDecimal precoMercado = precoMercadoUnitario != null ? precoMercadoUnitario : BigDecimal.ZERO;
        BigDecimal taxaMercadoPercentual;
        if (contaPremium) {
            taxaMercadoPercentual = ordemDeVenda ? new BigDecimal("6.5") : new BigDecimal("4.0");
        } else {
            taxaMercadoPercentual = ordemDeVenda ? new BigDecimal("10.5") : new BigDecimal("8.0");
        }

        BigDecimal aliquotaTaxaMercado = taxaMercadoPercentual.divide(CEM, 4, RoundingMode.HALF_UP);
        BigDecimal precoLiquidoMercadoUnitario = precoMercado.multiply(UM.subtract(aliquotaTaxaMercado)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal receitaLiquidaTotalMercado = precoLiquidoMercadoUnitario.multiply(qtdTotalBd).setScale(2, RoundingMode.HALF_UP);
        BigDecimal lucroTotalMercado = receitaLiquidaTotalMercado.subtract(custoProducaoTotal).setScale(2, RoundingMode.HALF_UP);
        BigDecimal lucroUnitarioMercado = precoLiquidoMercadoUnitario.subtract(custoProducaoPorUnidade).setScale(2, RoundingMode.HALF_UP);

        String melhorOpcao;
        String recomendacao;
        BigDecimal diferenca;

        if (lucroTotalBarraquinha.compareTo(BigDecimal.ZERO) < 0 && lucroTotalMercado.compareTo(BigDecimal.ZERO) < 0) {
            melhorOpcao = "PREJUIZO";
            diferenca = BigDecimal.ZERO;
            recomendacao = "Ambas as opções dão prejuízo com estes custos de produção. Recomenda-se comprar insumos via ordem de compra ou usar foco.";
        } else if (valorPagoPorUnidadeBarraquinha.compareTo(precoLiquidoMercadoUnitario) >= 0) {
            melhorOpcao = "BARRAQUINHA";
            diferenca = valorPagoPorUnidadeBarraquinha.subtract(precoLiquidoMercadoUnitario).multiply(qtdTotalBd).setScale(2, RoundingMode.HALF_UP);
            recomendacao = String.format(
                    "Vale mais a pena vender na BARRAQUINHA! Você recebe %s de prata por unidade imediatamente sem taxas de mercado (lucro de %s pratas por unidade). Vantagem de %s pratas sobre o mercado.",
                    valorPagoPorUnidadeBarraquinha, lucroUnitarioBarraquinha, diferenca
            );
        } else {
            melhorOpcao = "MERCADO";
            diferenca = precoLiquidoMercadoUnitario.subtract(valorPagoPorUnidadeBarraquinha).multiply(qtdTotalBd).setScale(2, RoundingMode.HALF_UP);
            recomendacao = String.format(
                    "Vale mais a pena vender no MERCADO! O preço líquido após taxas de %s%% é de %s de prata por unidade (lucro de %s pratas por unidade). Vantagem de %s pratas sobre a barraquinha.",
                    taxaMercadoPercentual, precoLiquidoMercadoUnitario, lucroUnitarioMercado, diferenca
            );
        }

        return new VendaBarraquinha(
                nomeComida,
                nutricaoEfetiva,
                valorPagoPorUnidadeBarraquinha,
                receitaTotalBarraquinha,
                custoProducaoTotal,
                custoProducaoPorUnidade,
                lucroTotalBarraquinha,
                lucroUnitarioBarraquinha,
                precoMercado,
                taxaMercadoPercentual,
                precoLiquidoMercadoUnitario,
                receitaLiquidaTotalMercado,
                lucroTotalMercado,
                lucroUnitarioMercado,
                melhorOpcao,
                recomendacao,
                diferenca
        );
    }
}
