package com.albion.api.domain.model;

/**
 * Representa as categorias de fabricação do Albion Online
 * e seus respectivos rendimentos padrão por clique de fabricação.
 */
public enum CategoriaProducao {
    CULINARIA(10),
    ALQUIMIA(5),
    EQUIPAMENTOS(1),
    REFINO(1);

    private final int rendimentoPadrao;

    CategoriaProducao(int rendimentoPadrao) {
        this.rendimentoPadrao = rendimentoPadrao;
    }

    public int getRendimentoPadrao() {
        return rendimentoPadrao;
    }

    public static CategoriaProducao deString(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return CULINARIA;
        }
        try {
            return CategoriaProducao.valueOf(valor.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return CULINARIA;
        }
    }
}
