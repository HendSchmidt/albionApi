package com.albion.api.infrastructure.adapter.in.web.dto;

import java.math.BigDecimal;
import java.util.List;

public record CraftRequestDto(
        List<RecursoRequestDto> recurso,
        int quantidadeParaProducao,
        int taxaDeRetorno,
        BigDecimal precoDeVenda,
        boolean contaPremium,
        BigDecimal taxaEstacaoPorCemNutricao,
        BigDecimal itemValue,
        Integer quantidadeDiarios,
        BigDecimal precoDiarioVazio,
        BigDecimal precoDiarioCheio,
        BigDecimal valorVendaDiario,
        Boolean ordemDeVenda,
        Boolean usarFoco,
        Integer custoFocoTotal,
        Integer rendimentoPorClique,
        String categoriaProducao,
        String nomeItem
) {
    public CraftRequestDto(
            List<RecursoRequestDto> recurso,
            int quantidadeParaProducao,
            int taxaDeRetorno,
            BigDecimal precoDeVenda,
            boolean contaPremium
    ) {
        this(recurso, quantidadeParaProducao, taxaDeRetorno, precoDeVenda, contaPremium,
                null, null, null, null, null, null, true, false, null, 1, "EQUIPAMENTOS", "Receita Customizada");
    }

    public CraftRequestDto(
            List<RecursoRequestDto> recurso,
            int quantidadeParaProducao,
            int taxaDeRetorno,
            BigDecimal precoDeVenda,
            boolean contaPremium,
            BigDecimal taxaEstacaoPorCemNutricao,
            BigDecimal itemValue,
            Integer quantidadeDiarios,
            BigDecimal precoDiarioVazio,
            BigDecimal precoDiarioCheio,
            BigDecimal valorVendaDiario,
            Boolean ordemDeVenda,
            Boolean usarFoco,
            Integer custoFocoTotal
    ) {
        this(recurso, quantidadeParaProducao, taxaDeRetorno, precoDeVenda, contaPremium,
                taxaEstacaoPorCemNutricao, itemValue, quantidadeDiarios, precoDiarioVazio,
                precoDiarioCheio, valorVendaDiario, ordemDeVenda, usarFoco, custoFocoTotal, 1, "EQUIPAMENTOS", "Receita Customizada");
    }

    public CraftRequestDto(
            List<RecursoRequestDto> recurso,
            int quantidadeParaProducao,
            int taxaDeRetorno,
            BigDecimal precoDeVenda,
            boolean contaPremium,
            BigDecimal taxaEstacaoPorCemNutricao,
            BigDecimal itemValue,
            Integer quantidadeDiarios,
            BigDecimal precoDiarioVazio,
            BigDecimal precoDiarioCheio,
            BigDecimal valorVendaDiario,
            Boolean ordemDeVenda,
            Boolean usarFoco,
            Integer custoFocoTotal,
            Integer rendimentoPorClique,
            String categoriaProducao
    ) {
        this(recurso, quantidadeParaProducao, taxaDeRetorno, precoDeVenda, contaPremium,
                taxaEstacaoPorCemNutricao, itemValue, quantidadeDiarios, precoDiarioVazio,
                precoDiarioCheio, valorVendaDiario, ordemDeVenda, usarFoco, custoFocoTotal,
                rendimentoPorClique, categoriaProducao, "Receita Customizada");
    }

    public int quantidade() {
        return quantidadeParaProducao;
    }

    public int taxaRetorno() {
        return taxaDeRetorno;
    }

    public BigDecimal valorVenda() {
        return precoDeVenda;
    }

    public BigDecimal valorCompraDiarioVazio() {
        return precoDiarioVazio;
    }

    public BigDecimal valorVendaDiarioCheio() {
        return precoDiarioCheio != null ? precoDiarioCheio : valorVendaDiario;
    }

    public boolean vendaInstantanea() {
        return Boolean.FALSE.equals(ordemDeVenda);
    }

    public boolean usoFoco() {
        return Boolean.TRUE.equals(usarFoco);
    }

    public Integer pontosFoco() {
        return custoFocoTotal;
    }
}
