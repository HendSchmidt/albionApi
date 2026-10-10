package com.albion.api.infrastructure.adapter.out.persistence.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tb_ingrediente_item")
public class IngredienteItemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nome;

    private int quantidade;

    @Column(precision = 19, scale = 2)
    private BigDecimal valor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_fabricado_id", nullable = false)
    private ItemFabricadoJpaEntity itemFabricado;

    public IngredienteItemJpaEntity() {}

    public IngredienteItemJpaEntity(String nome, int quantidade, BigDecimal valor) {
        this.nome = nome;
        this.quantidade = quantidade;
        this.valor = valor;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public int getQuantidade() { return quantidade; }
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public ItemFabricadoJpaEntity getItemFabricado() { return itemFabricado; }
    public void setItemFabricado(ItemFabricadoJpaEntity itemFabricado) { this.itemFabricado = itemFabricado; }
}
