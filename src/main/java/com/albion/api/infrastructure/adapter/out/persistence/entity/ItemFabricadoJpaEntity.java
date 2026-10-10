package com.albion.api.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tb_item_fabricado")
public class ItemFabricadoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String nomeItem;

    @Column(length = 20)
    private String categoriaProducao;

    private Integer rendimentoPorClique;
    private int quantidadeCliques;

    @Column(precision = 10, scale = 2)
    private BigDecimal taxaRetorno;

    @Column(precision = 19, scale = 2)
    private BigDecimal precoVendaUnitario;

    private boolean contaPremium;

    @Column(precision = 19, scale = 2)
    private BigDecimal taxaEstacaoPorCemNutricao;

    @Column(precision = 19, scale = 2)
    private BigDecimal itemValue;

    private Integer quantidadeDiarios;

    @Column(precision = 19, scale = 2)
    private BigDecimal valorCompraDiarioVazio;

    @Column(precision = 19, scale = 2)
    private BigDecimal valorVendaDiarioCheio;

    private boolean vendaInstantanea;
    private boolean usoFoco;
    private Integer pontosFoco;

    @Column(precision = 19, scale = 2)
    private BigDecimal custoTotalEstimado;

    @Column(precision = 19, scale = 2)
    private BigDecimal lucroEstimado;

    private LocalDateTime dataCriacao;

    @OneToMany(mappedBy = "itemFabricado", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<IngredienteItemJpaEntity> ingredientes = new ArrayList<>();

    public ItemFabricadoJpaEntity() {}

    @PrePersist
    public void prePersist() {
        if (this.dataCriacao == null) {
            this.dataCriacao = LocalDateTime.now();
        }
    }

    public void adicionarIngrediente(IngredienteItemJpaEntity ingrediente) {
        ingredientes.add(ingrediente);
        ingrediente.setItemFabricado(this);
    }

    public void removerIngrediente(IngredienteItemJpaEntity ingrediente) {
        ingredientes.remove(ingrediente);
        ingrediente.setItemFabricado(null);
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNomeItem() { return nomeItem; }
    public void setNomeItem(String nomeItem) { this.nomeItem = nomeItem; }

    public String getCategoriaProducao() { return categoriaProducao; }
    public void setCategoriaProducao(String categoriaProducao) { this.categoriaProducao = categoriaProducao; }

    public Integer getRendimentoPorClique() { return rendimentoPorClique; }
    public void setRendimentoPorClique(Integer rendimentoPorClique) { this.rendimentoPorClique = rendimentoPorClique; }

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

    public List<IngredienteItemJpaEntity> getIngredientes() { return ingredientes; }
    public void setIngredientes(List<IngredienteItemJpaEntity> ingredientes) { this.ingredientes = ingredientes; }
}
