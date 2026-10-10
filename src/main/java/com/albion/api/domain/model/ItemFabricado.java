package com.albion.api.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Aggregate Root que representa uma receita de item fabricado no Albion Online.
 */
public class ItemFabricado {
    private Long id;
    private String nomeItem;
    private CategoriaProducao categoriaProducao;
    private int rendimentoPorClique;
    private int quantidadeCliques;
    private BigDecimal taxaRetorno;
    private BigDecimal precoVendaUnitario;
    private boolean contaPremium;
    private BigDecimal taxaEstacaoPorCemNutricao;
    private BigDecimal itemValue;
    private Integer quantidadeDiarios;
    private BigDecimal valorCompraDiarioVazio;
    private BigDecimal valorVendaDiarioCheio;
    private boolean vendaInstantanea;
    private boolean usoFoco;
    private Integer pontosFoco;
    private BigDecimal custoTotalEstimado;
    private BigDecimal lucroEstimado;
    private LocalDateTime dataCriacao;
    private final List<Ingrediente> ingredientes = new ArrayList<>();

    public ItemFabricado() {
        this.categoriaProducao = CategoriaProducao.CULINARIA;
        this.rendimentoPorClique = 10;
        this.quantidadeCliques = 1;
        this.taxaRetorno = BigDecimal.ZERO;
        this.precoVendaUnitario = BigDecimal.ZERO;
        this.contaPremium = true;
        this.dataCriacao = LocalDateTime.now();
    }

    public ItemFabricado(Long id, String nomeItem, CategoriaProducao categoriaProducao,
                         int rendimentoPorClique, int quantidadeCliques, BigDecimal taxaRetorno,
                         BigDecimal precoVendaUnitario, boolean contaPremium,
                         BigDecimal taxaEstacaoPorCemNutricao, BigDecimal itemValue,
                         Integer quantidadeDiarios, BigDecimal valorCompraDiarioVazio,
                         BigDecimal valorVendaDiarioCheio, boolean vendaInstantanea,
                         boolean usoFoco, Integer pontosFoco, BigDecimal custoTotalEstimado,
                         BigDecimal lucroEstimado, LocalDateTime dataCriacao,
                         List<Ingrediente> ingredientes) {
        this.id = id;
        this.nomeItem = (nomeItem != null && !nomeItem.trim().isEmpty()) ? nomeItem.trim() : "Receita Customizada";
        this.categoriaProducao = categoriaProducao != null ? categoriaProducao : CategoriaProducao.CULINARIA;
        this.rendimentoPorClique = rendimentoPorClique > 0 ? rendimentoPorClique : 1;
        this.quantidadeCliques = Math.max(1, quantidadeCliques);
        this.taxaRetorno = taxaRetorno != null ? taxaRetorno : BigDecimal.ZERO;
        this.precoVendaUnitario = precoVendaUnitario != null ? precoVendaUnitario : BigDecimal.ZERO;
        this.contaPremium = contaPremium;
        this.taxaEstacaoPorCemNutricao = taxaEstacaoPorCemNutricao;
        this.itemValue = itemValue;
        this.quantidadeDiarios = quantidadeDiarios;
        this.valorCompraDiarioVazio = valorCompraDiarioVazio;
        this.valorVendaDiarioCheio = valorVendaDiarioCheio;
        this.vendaInstantanea = vendaInstantanea;
        this.usoFoco = usoFoco;
        this.pontosFoco = pontosFoco;
        this.custoTotalEstimado = custoTotalEstimado;
        this.lucroEstimado = lucroEstimado;
        this.dataCriacao = dataCriacao != null ? dataCriacao : LocalDateTime.now();
        if (ingredientes != null) {
            this.ingredientes.addAll(ingredientes);
        }
    }

    public void adicionarIngrediente(Ingrediente ingrediente) {
        if (ingrediente != null) {
            this.ingredientes.add(ingrediente);
        }
    }

    public void atualizarEstimativas(BigDecimal custo, BigDecimal lucro) {
        this.custoTotalEstimado = custo;
        this.lucroEstimado = lucro;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeItem() { return nomeItem; }
    public void setNomeItem(String nomeItem) { this.nomeItem = nomeItem; }

    public CategoriaProducao getCategoriaProducao() { return categoriaProducao; }
    public void setCategoriaProducao(CategoriaProducao categoriaProducao) { this.categoriaProducao = categoriaProducao; }

    public int getRendimentoPorClique() { return rendimentoPorClique; }
    public void setRendimentoPorClique(int rendimentoPorClique) { this.rendimentoPorClique = rendimentoPorClique; }

    public int getQuantidadeCliques() { return quantidadeCliques; }
    public void setQuantidadeCliques(int quantidadeCliques) { this.quantidadeCliques = quantidadeCliques; }

    public BigDecimal getTaxaRetorno() { return taxaRetorno; }
    public void setTaxaRetorno(BigDecimal taxaRetorno) { this.taxaRetorno = taxaRetorno; }

    public BigDecimal getPrecoVendaUnitario() { return precoVendaUnitario; }
    public void setPrecoVendaUnitario(BigDecimal precoVendaUnitario) { this.precoVendaUnitario = precoVendaUnitario; }

    public boolean isContaPremium() { return contaPremium; }
    public void setContaPremium(boolean contaPremium) { this.contaPremium = contaPremium; }

    public BigDecimal getTaxaEstacaoPorCemNutricao() { return taxaEstacaoPorCemNutricao; }
    public void setTaxaEstacaoPorCemNutricao(BigDecimal taxaEstacaoPorCemNutricao) { this.taxaEstacaoPorCemNutricao = taxaEstacaoPorCemNutricao; }

    public BigDecimal getItemValue() { return itemValue; }
    public void setItemValue(BigDecimal itemValue) { this.itemValue = itemValue; }

    public Integer getQuantidadeDiarios() { return quantidadeDiarios; }
    public void setQuantidadeDiarios(Integer quantidadeDiarios) { this.quantidadeDiarios = quantidadeDiarios; }

    public BigDecimal getValorCompraDiarioVazio() { return valorCompraDiarioVazio; }
    public void setValorCompraDiarioVazio(BigDecimal valorCompraDiarioVazio) { this.valorCompraDiarioVazio = valorCompraDiarioVazio; }

    public BigDecimal getValorVendaDiarioCheio() { return valorVendaDiarioCheio; }
    public void setValorVendaDiarioCheio(BigDecimal valorVendaDiarioCheio) { this.valorVendaDiarioCheio = valorVendaDiarioCheio; }

    public boolean isVendaInstantanea() { return vendaInstantanea; }
    public void setVendaInstantanea(boolean vendaInstantanea) { this.vendaInstantanea = vendaInstantanea; }

    public boolean isUsoFoco() { return usoFoco; }
    public void setUsoFoco(boolean usoFoco) { this.usoFoco = usoFoco; }

    public Integer getPontosFoco() { return pontosFoco; }
    public void setPontosFoco(Integer pontosFoco) { this.pontosFoco = pontosFoco; }

    public BigDecimal getCustoTotalEstimado() { return custoTotalEstimado; }
    public void setCustoTotalEstimado(BigDecimal custoTotalEstimado) { this.custoTotalEstimado = custoTotalEstimado; }

    public BigDecimal getLucroEstimado() { return lucroEstimado; }
    public void setLucroEstimado(BigDecimal lucroEstimado) { this.lucroEstimado = lucroEstimado; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public void setDataCriacao(LocalDateTime dataCriacao) { this.dataCriacao = dataCriacao; }

    public List<Ingrediente> getIngredientes() {
        return Collections.unmodifiableList(ingredientes);
    }
}
