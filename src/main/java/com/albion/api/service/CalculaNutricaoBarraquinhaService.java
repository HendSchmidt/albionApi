package com.albion.api.service;

import com.albion.api.dto.FoodNutritionSaleRequestDto;
import com.albion.api.dto.FoodNutritionSaleResponseDto;
import com.albion.api.dto.RecursoRequestDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class CalculaNutricaoBarraquinhaService {

    private static final Logger log = LoggerFactory.getLogger(CalculaNutricaoBarraquinhaService.class);

    private static final BigDecimal CEM = new BigDecimal("100");
    private static final BigDecimal DOIS = new BigDecimal("2");
    private static final BigDecimal UM = BigDecimal.ONE;

    public FoodNutritionSaleResponseDto calcular(FoodNutritionSaleRequestDto request) {
        log.info("[CLASSE: CalculaNutricaoBarraquinhaService] [METODO: calcular] [ENTRADA: {}]", request);

        if (request == null) {
            log.warn("[CLASSE: CalculaNutricaoBarraquinhaService] [METODO: calcular] Request nulo recebido.");
            return null;
        }

        // 1. Nutrição efetiva por unidade (dobra se for comida favorita da bancada)
        BigDecimal nutricaoBase = request.nutricaoPorUnidade() != null ? request.nutricaoPorUnidade() : BigDecimal.ZERO;
        BigDecimal nutricaoEfetiva = request.comidaFavorita() ? nutricaoBase.multiply(DOIS) : nutricaoBase;

        // 2. Valor pago por unidade na barraquinha: (Nutrição / 100) * X
        BigDecimal valorPorCem = request.valorPorCemNutricao() != null ? request.valorPorCemNutricao() : BigDecimal.ZERO;
        BigDecimal fatorNutricao = nutricaoEfetiva.divide(CEM, 4, RoundingMode.HALF_UP);
        BigDecimal valorPagoPorUnidadeBarraquinha = fatorNutricao.multiply(valorPorCem).setScale(2, RoundingMode.HALF_UP);

        int quantidadeProducao = (request.quantidadeProducao() != null && request.quantidadeProducao() > 0)
                ? request.quantidadeProducao()
                : 1;
        BigDecimal qtdTotalBd = BigDecimal.valueOf(quantidadeProducao);

        // Receita Total Barraquinha
        BigDecimal receitaTotalBarraquinha = valorPagoPorUnidadeBarraquinha.multiply(qtdTotalBd).setScale(2, RoundingMode.HALF_UP);

        // 3. Custo de produção considerando TRR dos ingredientes
        BigDecimal taxaRetorno = (request.taxaDeRetorno() != null ? request.taxaDeRetorno() : BigDecimal.ZERO)
                .divide(CEM, 4, RoundingMode.HALF_UP);
        BigDecimal fatorCustoInsumo = UM.subtract(taxaRetorno);
        if (fatorCustoInsumo.compareTo(BigDecimal.ZERO) < 0) {
            fatorCustoInsumo = BigDecimal.ZERO;
        }

        BigDecimal custoInsumosTotal = BigDecimal.ZERO;
        List<RecursoRequestDto> ingredientes = request.ingredientes();
        if (ingredientes != null && !ingredientes.isEmpty()) {
            for (RecursoRequestDto ing : ingredientes) {
                if (ing != null && ing.quantidade() > 0 && ing.valor() != null) {
                    BigDecimal preco = ing.valor();
                    BigDecimal qtd = BigDecimal.valueOf(ing.quantidade());
                    BigDecimal custoItem = preco.multiply(qtd).multiply(fatorCustoInsumo);
                    custoInsumosTotal = custoInsumosTotal.add(custoItem);
                }
            }
        }

        BigDecimal custoProducaoTotal = custoInsumosTotal.setScale(2, RoundingMode.HALF_UP);
        BigDecimal custoProducaoPorUnidade = custoProducaoTotal.divide(qtdTotalBd, 2, RoundingMode.HALF_UP);

        // Lucros da Barraquinha
        BigDecimal lucroTotalBarraquinha = receitaTotalBarraquinha.subtract(custoProducaoTotal).setScale(2, RoundingMode.HALF_UP);
        BigDecimal lucroUnitarioBarraquinha = valorPagoPorUnidadeBarraquinha.subtract(custoProducaoPorUnidade).setScale(2, RoundingMode.HALF_UP);

        // 4. Preço e Lucro do Mercado
        BigDecimal precoMercadoUnitario = request.precoMercadoUnitario() != null ? request.precoMercadoUnitario() : BigDecimal.ZERO;
        
        // Taxas do mercado: 
        // Com Premium: 6.5% (2.5% montagem ordem de venda + 4% taxa venda) se ordemDeVenda=true, ou 4% venda direta
        // Sem Premium: 10.5% (2.5% montagem + 8% taxa venda) se ordemDeVenda=true, ou 8% venda direta
        BigDecimal taxaMercadoPercentual;
        if (request.contaPremium()) {
            taxaMercadoPercentual = request.ordemDeVenda() ? new BigDecimal("6.5") : new BigDecimal("4.0");
        } else {
            taxaMercadoPercentual = request.ordemDeVenda() ? new BigDecimal("10.5") : new BigDecimal("8.0");
        }

        BigDecimal aliquotaTaxaMercado = taxaMercadoPercentual.divide(CEM, 4, RoundingMode.HALF_UP);
        BigDecimal precoLiquidoMercadoUnitario = precoMercadoUnitario.multiply(UM.subtract(aliquotaTaxaMercado)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal receitaLiquidaTotalMercado = precoLiquidoMercadoUnitario.multiply(qtdTotalBd).setScale(2, RoundingMode.HALF_UP);

        BigDecimal lucroTotalMercado = receitaLiquidaTotalMercado.subtract(custoProducaoTotal).setScale(2, RoundingMode.HALF_UP);
        BigDecimal lucroUnitarioMercado = precoLiquidoMercadoUnitario.subtract(custoProducaoPorUnidade).setScale(2, RoundingMode.HALF_UP);

        // 5. Comparação e Tomada de Decisão
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

        FoodNutritionSaleResponseDto response = new FoodNutritionSaleResponseDto(
                request.nomeComida(),
                nutricaoEfetiva,
                valorPagoPorUnidadeBarraquinha,
                receitaTotalBarraquinha,
                custoProducaoTotal,
                custoProducaoPorUnidade,
                lucroTotalBarraquinha,
                lucroUnitarioBarraquinha,
                precoMercadoUnitario,
                taxaMercadoPercentual,
                precoLiquidoMercadoUnitario,
                receitaLiquidaTotalMercado,
                lucroTotalMercado,
                lucroUnitarioMercado,
                melhorOpcao,
                recomendacao,
                diferenca
        );

        log.info("[CLASSE: CalculaNutricaoBarraquinhaService] [METODO: calcular] [SAIDA: {}]", response);
        return response;
    }
}
