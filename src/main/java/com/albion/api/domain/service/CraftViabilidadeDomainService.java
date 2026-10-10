package com.albion.api.domain.service;

import com.albion.api.domain.model.CategoriaProducao;
import com.albion.api.domain.model.CustoRecurso;
import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.domain.model.ViabilidadeCraft;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Domain Service responsável pelo cálculo matemático de viabilidade de produção do Albion Online.
 * Contém as regras de negócio puras, desacopladas de frameworks.
 */
public class CraftViabilidadeDomainService {

    private static final Logger log = LoggerFactory.getLogger(CraftViabilidadeDomainService.class);

    private static final BigDecimal CEM = new BigDecimal("100");
    private static final BigDecimal FATOR_NUTRICAO = new BigDecimal("0.1125");
    private static final BigDecimal TAXA_MERCADO_COM_PREMIUM = new BigDecimal("0.06");
    private static final BigDecimal TAXA_MERCADO_SEM_PREMIUM = new BigDecimal("0.12");
    private static final BigDecimal TAXA_MONTAGEM_ORDEM = new BigDecimal("0.025");

    public ViabilidadeCraft calcular(ItemFabricado item) {
        log.info("[DomainService: CraftViabilidadeDomainService] [Calculando item: {}]", item != null ? item.getNomeItem() : "null");
        if (item == null) {
            throw new IllegalArgumentException("O item fabricado não pode ser nulo.");
        }

        int quantidadeCliques = Math.max(1, item.getQuantidadeCliques());
        int rendimento = item.getRendimentoPorClique() > 0 ? item.getRendimentoPorClique() : 1;
        int totalItensProduzidos = quantidadeCliques * rendimento;

        // Fator de Retorno de Recursos (RRR)
        BigDecimal taxaRetornoPercent = item.getTaxaRetorno() != null ? item.getTaxaRetorno() : BigDecimal.ZERO;
        BigDecimal fatorRetorno = taxaRetornoPercent.divide(CEM, 4, RoundingMode.HALF_UP);
        BigDecimal fatorConsumo = BigDecimal.ONE.subtract(fatorRetorno);
        if (fatorConsumo.compareTo(BigDecimal.ZERO) < 0) {
            fatorConsumo = BigDecimal.ZERO;
        }

        List<CustoRecurso> custosPorRecurso = new ArrayList<>();
        BigDecimal custoInsumos = BigDecimal.ZERO;

        for (Ingrediente ingrediente : item.getIngredientes()) {
            if (ingrediente == null) continue;
            BigDecimal qtdTotal = BigDecimal.valueOf((long) ingrediente.getQuantidade() * quantidadeCliques);
            BigDecimal qtdConsumidaEfetiva = qtdTotal.multiply(fatorConsumo);
            BigDecimal valorUnitario = ingrediente.getValorUnitario() != null ? ingrediente.getValorUnitario() : BigDecimal.ZERO;
            BigDecimal custoRecurso = qtdConsumidaEfetiva.multiply(valorUnitario).setScale(2, RoundingMode.HALF_UP);

            custosPorRecurso.add(new CustoRecurso(ingrediente.getNome(), custoRecurso));
            custoInsumos = custoInsumos.add(custoRecurso);
        }

        // Taxa da Estação
        BigDecimal custoTaxaEstacao = BigDecimal.ZERO;
        if (item.getTaxaEstacaoPorCemNutricao() != null && item.getTaxaEstacaoPorCemNutricao().compareTo(BigDecimal.ZERO) > 0) {
            BigDecimal iv = item.getItemValue() != null && item.getItemValue().compareTo(BigDecimal.ZERO) > 0
                    ? item.getItemValue()
                    : custoInsumos.divide(BigDecimal.valueOf(quantidadeCliques), 2, RoundingMode.HALF_UP);

            BigDecimal nutricaoConsumida = iv.multiply(FATOR_NUTRICAO).multiply(BigDecimal.valueOf(quantidadeCliques));
            custoTaxaEstacao = nutricaoConsumida.divide(CEM, 4, RoundingMode.HALF_UP)
                    .multiply(item.getTaxaEstacaoPorCemNutricao())
                    .setScale(2, RoundingMode.HALF_UP);
        }

        // Taxas de Mercado dos Itens Finais
        BigDecimal precoVendaUnitario = item.getPrecoVendaUnitario() != null ? item.getPrecoVendaUnitario() : BigDecimal.ZERO;
        BigDecimal receitaBrutaItens = precoVendaUnitario.multiply(BigDecimal.valueOf(totalItensProduzidos)).setScale(2, RoundingMode.HALF_UP);

        BigDecimal aliquotaVenda = item.isContaPremium() ? TAXA_MERCADO_COM_PREMIUM : TAXA_MERCADO_SEM_PREMIUM;
        BigDecimal taxaVendaItens = receitaBrutaItens.multiply(aliquotaVenda).setScale(2, RoundingMode.HALF_UP);

        boolean ehOrdemDeVenda = !item.isVendaInstantanea();
        BigDecimal taxaMontagemOrdemAliquota = ehOrdemDeVenda ? TAXA_MONTAGEM_ORDEM : BigDecimal.ZERO;
        BigDecimal taxaMontagemItens = receitaBrutaItens.multiply(taxaMontagemOrdemAliquota).setScale(2, RoundingMode.HALF_UP);

        BigDecimal receitaLiquidaItens = receitaBrutaItens.subtract(taxaVendaItens).subtract(taxaMontagemItens).setScale(2, RoundingMode.HALF_UP);

        // Diários de Artesão
        BigDecimal custoDiariosVazios = BigDecimal.ZERO;
        BigDecimal receitaBrutaDiarios = BigDecimal.ZERO;
        BigDecimal taxaVendaDiarios = BigDecimal.ZERO;
        BigDecimal taxaMontagemDiarios = BigDecimal.ZERO;
        BigDecimal receitaLiquidaDiarios = BigDecimal.ZERO;
        BigDecimal lucroLiquidoDiarios = BigDecimal.ZERO;

        int qtdDiarios = item.getQuantidadeDiarios() != null ? Math.max(0, item.getQuantidadeDiarios()) : 0;
        if (qtdDiarios > 0) {
            BigDecimal precoCheio = item.getValorVendaDiarioCheio() != null ? item.getValorVendaDiarioCheio() : BigDecimal.ZERO;
            receitaBrutaDiarios = precoCheio.multiply(BigDecimal.valueOf(qtdDiarios)).setScale(2, RoundingMode.HALF_UP);
            BigDecimal precoVazio = item.getValorCompraDiarioVazio() != null ? item.getValorCompraDiarioVazio() : BigDecimal.ZERO;
            custoDiariosVazios = precoVazio.multiply(BigDecimal.valueOf(qtdDiarios)).setScale(2, RoundingMode.HALF_UP);
            taxaVendaDiarios = receitaBrutaDiarios.multiply(aliquotaVenda).setScale(2, RoundingMode.HALF_UP);
            taxaMontagemDiarios = receitaBrutaDiarios.multiply(taxaMontagemOrdemAliquota).setScale(2, RoundingMode.HALF_UP);
            receitaLiquidaDiarios = receitaBrutaDiarios.subtract(taxaVendaDiarios).subtract(taxaMontagemDiarios).setScale(2, RoundingMode.HALF_UP);
            lucroLiquidoDiarios = receitaLiquidaDiarios.subtract(custoDiariosVazios).setScale(2, RoundingMode.HALF_UP);
        }

        // Consolidação
        BigDecimal custoTotalProducao = custoInsumos.add(custoTaxaEstacao).add(custoDiariosVazios).setScale(2, RoundingMode.HALF_UP);
        BigDecimal taxaVendaTotal = taxaVendaItens.add(taxaVendaDiarios).setScale(2, RoundingMode.HALF_UP);
        BigDecimal taxaMontagemTotal = taxaMontagemItens.add(taxaMontagemDiarios).setScale(2, RoundingMode.HALF_UP);
        BigDecimal receitaLiquidaTotal = receitaLiquidaItens.add(receitaLiquidaDiarios).setScale(2, RoundingMode.HALF_UP);

        BigDecimal lucro = receitaLiquidaTotal.subtract(custoTotalProducao).setScale(2, RoundingMode.HALF_UP);

        BigDecimal custoUnitarioItemFinal = totalItensProduzidos > 0
                ? custoTotalProducao.divide(BigDecimal.valueOf(totalItensProduzidos), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;
        BigDecimal lucroUnitarioItemFinal = totalItensProduzidos > 0
                ? lucro.divide(BigDecimal.valueOf(totalItensProduzidos), 2, RoundingMode.HALF_UP)
                : BigDecimal.ZERO;

        BigDecimal prataPorFoco = BigDecimal.ZERO;
        if (item.isUsoFoco() && item.getPontosFoco() != null && item.getPontosFoco() > 0) {
            prataPorFoco = lucro.divide(BigDecimal.valueOf(item.getPontosFoco()), 2, RoundingMode.HALF_UP);
        }

        String categoriaNome = item.getCategoriaProducao() != null ? item.getCategoriaProducao().name() : "CULINARIA";

        return new ViabilidadeCraft(
                custoTotalProducao,
                custosPorRecurso,
                lucro,
                custoTaxaEstacao,
                receitaLiquidaDiarios,
                custoDiariosVazios,
                lucroLiquidoDiarios,
                taxaMontagemTotal,
                taxaVendaTotal,
                receitaLiquidaTotal,
                prataPorFoco,
                totalItensProduzidos,
                rendimento,
                custoUnitarioItemFinal,
                lucroUnitarioItemFinal,
                categoriaNome
        );
    }
}
