package com.albion.api.domain.model;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Value Object representando um ingrediente/insumo de fabricação.
 */
public class Ingrediente {
    private final String nome;
    private final int quantidade;
    private final BigDecimal valorUnitario;

    public Ingrediente(String nome, int quantidade, BigDecimal valorUnitario) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("O nome do ingrediente não pode ser nulo ou vazio.");
        }
        if (quantidade < 0) {
            throw new IllegalArgumentException("A quantidade do ingrediente não pode ser negativa.");
        }
        this.nome = nome.trim();
        this.quantidade = quantidade;
        this.valorUnitario = valorUnitario != null ? valorUnitario : BigDecimal.ZERO;
    }

    public String getNome() {
        return nome;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public BigDecimal getValorUnitario() {
        return valorUnitario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingrediente that = (Ingrediente) o;
        return quantidade == that.quantidade &&
                Objects.equals(nome, that.nome) &&
                Objects.equals(valorUnitario, that.valorUnitario);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nome, quantidade, valorUnitario);
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "nome='" + nome + '"' +
                ", quantidade=" + quantidade +
                ", valorUnitario=" + valorUnitario +
                '}';
    }
}
