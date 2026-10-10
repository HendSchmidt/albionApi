package com.albion.api.infrastructure.adapter.out.persistence.seeder;

import com.albion.api.domain.model.CategoriaProducao;
import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.domain.port.out.ItemFabricadoRepositoryPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

/**
 * Seeder de banco de dados (Infrastructure Outbound Persistence Seeder).
 * 
 * Camada Arquitetural: Infrastructure -> Adapter Out -> Persistence -> Seeder
 * 
 * Responsabilidade:
 * - Provisionar o banco relacional H2 com as 18 receitas oficiais de culinária
 *   na inicialização da aplicação (caso ainda não existam).
 * - Utiliza a porta de persistência {@link ItemFabricadoRepositoryPort} (DIP - SOLID).
 */
@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);

    private final ItemFabricadoRepositoryPort repositoryPort;

    public DatabaseSeeder(ItemFabricadoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
    }

    @Override
    public void run(String... args) {
        log.info("[Infrastructure: Persistence Seeder] Verificando receitas culinárias padrão no banco H2...");
        cadastrarReceitasPadrao();
        log.info("[Infrastructure: Persistence Seeder] Carga concluída. Total de receitas no H2: {}", repositoryPort.contar());
    }

    private void cadastrarReceitasPadrao() {
        // --------------------------------------------------------------------
        // 1. Sopas (Regeneração de Vida fora de combate)
        // --------------------------------------------------------------------
        cadastrarSeNaoExistir(
                "Sopa de Cenoura (T1)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(new Ingrediente("Cenoura", 48, BigDecimal.ZERO))
        );
        cadastrarSeNaoExistir(
                "Sopa de Trigo (T3)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(new Ingrediente("Trigo", 48, BigDecimal.ZERO))
        );
        cadastrarSeNaoExistir(
                "Sopa de Repolho (T5)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(new Ingrediente("Repolho", 48, BigDecimal.ZERO))
        );

        // --------------------------------------------------------------------
        // 2. Saladas (Aumento de Velocidade e Qualidade de Craft)
        // --------------------------------------------------------------------
        cadastrarSeNaoExistir(
                "Salada de Nabo (T2)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Nabo", 8, BigDecimal.ZERO),
                        new Ingrediente("Cenoura", 8, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Salada de Batata (T4)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Batata", 8, BigDecimal.ZERO),
                        new Ingrediente("Trigo", 8, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Salada de Feijão (T6)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Feijão", 8, BigDecimal.ZERO),
                        new Ingrediente("Repolho", 8, BigDecimal.ZERO)
                )
        );

        // --------------------------------------------------------------------
        // 3. Tortas (Aumento de Carga Máxima e Rendimento de Coleta)
        // --------------------------------------------------------------------
        cadastrarSeNaoExistir(
                "Torta de Frango (T3)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Carne de Frango", 24, BigDecimal.ZERO),
                        new Ingrediente("Farinha", 12, BigDecimal.ZERO),
                        new Ingrediente("Leite de Cabra", 6, BigDecimal.ZERO),
                        new Ingrediente("Cenoura", 6, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Torta de Ganso (T5)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Carne de Ganso", 24, BigDecimal.ZERO),
                        new Ingrediente("Farinha", 12, BigDecimal.ZERO),
                        new Ingrediente("Leite de Cabra", 6, BigDecimal.ZERO),
                        new Ingrediente("Repolho", 6, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Torta de Porco (T7)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Carne de Porco", 24, BigDecimal.ZERO),
                        new Ingrediente("Farinha", 12, BigDecimal.ZERO),
                        new Ingrediente("Leite de Ovelha", 6, BigDecimal.ZERO),
                        new Ingrediente("Abóbora", 6, BigDecimal.ZERO)
                )
        );

        // --------------------------------------------------------------------
        // 4. Guisados / Stews (Aumento de Dano de Ataque)
        // --------------------------------------------------------------------
        cadastrarSeNaoExistir(
                "Guisado de Cabrito (T4)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Carne de Cabrito", 24, BigDecimal.ZERO),
                        new Ingrediente("Batata", 24, BigDecimal.ZERO),
                        new Ingrediente("Pão", 4, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Guisado de Carneiro (T6)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Carne de Carneiro", 24, BigDecimal.ZERO),
                        new Ingrediente("Feijão", 24, BigDecimal.ZERO),
                        new Ingrediente("Pão", 4, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Guisado de Carne de Panela (T8)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Carne de Boi", 24, BigDecimal.ZERO),
                        new Ingrediente("Abóbora", 24, BigDecimal.ZERO),
                        new Ingrediente("Pão", 4, BigDecimal.ZERO)
                )
        );

        // --------------------------------------------------------------------
        // 5. Sanduíches (Aumento de Vida Máxima)
        // --------------------------------------------------------------------
        cadastrarSeNaoExistir(
                "Sanduíche de Cabrito (T4)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Carne de Cabrito", 24, BigDecimal.ZERO),
                        new Ingrediente("Pão", 24, BigDecimal.ZERO),
                        new Ingrediente("Manteiga", 4, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Sanduíche de Carneiro (T6)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Carne de Carneiro", 24, BigDecimal.ZERO),
                        new Ingrediente("Pão", 24, BigDecimal.ZERO),
                        new Ingrediente("Manteiga", 4, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Sanduíche de Carne de Panela (T8)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Carne de Boi", 24, BigDecimal.ZERO),
                        new Ingrediente("Pão", 24, BigDecimal.ZERO),
                        new Ingrediente("Manteiga", 4, BigDecimal.ZERO)
                )
        );

        // --------------------------------------------------------------------
        // 6. Omeletes (Redução de Cooldown e Conjuração)
        // --------------------------------------------------------------------
        cadastrarSeNaoExistir(
                "Omelete de Frango (T3)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Carne de Frango", 12, BigDecimal.ZERO),
                        new Ingrediente("Ovo de Galinha", 12, BigDecimal.ZERO),
                        new Ingrediente("Leite de Cabra", 6, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Omelete de Ganso (T5)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Carne de Ganso", 12, BigDecimal.ZERO),
                        new Ingrediente("Ovo de Ganso", 12, BigDecimal.ZERO),
                        new Ingrediente("Leite de Cabra", 6, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Omelete de Porco (T7)",
                CategoriaProducao.CULINARIA,
                10,
                List.of(
                        new Ingrediente("Carne de Porco", 12, BigDecimal.ZERO),
                        new Ingrediente("Ovo de Ganso", 12, BigDecimal.ZERO),
                        new Ingrediente("Leite de Ovelha", 6, BigDecimal.ZERO)
                )
        );
    }

    private void cadastrarSeNaoExistir(String nomeItem, CategoriaProducao categoria, int rendimento, List<Ingrediente> ingredientes) {
        if (repositoryPort.existePorNome(nomeItem)) {
            return;
        }

        ItemFabricado item = new ItemFabricado(
                null,
                nomeItem,
                categoria,
                rendimento,
                1,
                BigDecimal.valueOf(15.00),
                BigDecimal.ZERO,
                true,
                null,
                null,
                null,
                null,
                null,
                false,
                false,
                null,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                null,
                ingredientes
        );

        repositoryPort.salvar(item);
        log.info("[Infrastructure: Persistence Seeder] Receita padrão cadastrada no H2: {}", nomeItem);
    }
}
