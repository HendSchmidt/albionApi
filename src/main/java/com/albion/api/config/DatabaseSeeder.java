package com.albion.api.config;

import com.albion.api.entity.IngredienteItemEntity;
import com.albion.api.entity.ItemFabricadoEntity;
import com.albion.api.repository.ItemFabricadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DatabaseSeeder.class);
    private final ItemFabricadoRepository repository;

    public DatabaseSeeder(ItemFabricadoRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... args) {
        log.info("[CLASSE: DatabaseSeeder] [METODO: run] - Verificando cadastro de receitas padrao no H2");
        cadastrarReceitasPadrao();
        log.info("[CLASSE: DatabaseSeeder] [METODO: run] - Carga de receitas concluida. Total no H2: {}", repository.count());
    }

    private void cadastrarReceitasPadrao() {
        // 1. Sopas (Regeneração de Vida fora de combate)
        cadastrarSeNaoExistir(
                "Sopa de Cenoura (T1)",
                "CULINARIA",
                10,
                List.of(new IngredienteItemEntity("Cenoura", 48, BigDecimal.ZERO))
        );
        cadastrarSeNaoExistir(
                "Sopa de Trigo (T3)",
                "CULINARIA",
                10,
                List.of(new IngredienteItemEntity("Trigo", 48, BigDecimal.ZERO))
        );
        cadastrarSeNaoExistir(
                "Sopa de Repolho (T5)",
                "CULINARIA",
                10,
                List.of(new IngredienteItemEntity("Repolho", 48, BigDecimal.ZERO))
        );

        // 2. Saladas (Aumento de Velocidade e Qualidade de Craft)
        cadastrarSeNaoExistir(
                "Salada de Nabo (T2)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Nabo", 8, BigDecimal.ZERO),
                        new IngredienteItemEntity("Cenoura", 8, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Salada de Batata (T4)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Batata", 8, BigDecimal.ZERO),
                        new IngredienteItemEntity("Trigo", 8, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Salada de Feijão (T6)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Feijão", 8, BigDecimal.ZERO),
                        new IngredienteItemEntity("Repolho", 8, BigDecimal.ZERO)
                )
        );

        // 3. Tortas (Aumento de Carga Máxima e Rendimento de Coleta)
        cadastrarSeNaoExistir(
                "Torta de Frango (T3)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Carne de Frango", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Farinha", 12, BigDecimal.ZERO),
                        new IngredienteItemEntity("Leite de Cabra", 6, BigDecimal.ZERO),
                        new IngredienteItemEntity("Cenoura", 6, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Torta de Ganso (T5)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Carne de Ganso", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Farinha", 12, BigDecimal.ZERO),
                        new IngredienteItemEntity("Leite de Cabra", 6, BigDecimal.ZERO),
                        new IngredienteItemEntity("Repolho", 6, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Torta de Porco (T7)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Carne de Porco", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Farinha", 12, BigDecimal.ZERO),
                        new IngredienteItemEntity("Leite de Ovelha", 6, BigDecimal.ZERO),
                        new IngredienteItemEntity("Abóbora", 6, BigDecimal.ZERO)
                )
        );

        // 4. Guisados / Stews (Aumento de Dano de Ataque)
        cadastrarSeNaoExistir(
                "Guisado de Cabrito (T4)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Carne de Cabrito", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Batata", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Pão", 4, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Guisado de Carneiro (T6)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Carne de Carneiro", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Feijão", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Pão", 4, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Guisado de Carne de Panela (T8)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Carne de Boi", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Abóbora", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Pão", 4, BigDecimal.ZERO)
                )
        );

        // 5. Sanduíches (Aumento de Vida Máxima)
        cadastrarSeNaoExistir(
                "Sanduíche de Cabrito (T4)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Carne de Cabrito", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Pão", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Manteiga", 4, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Sanduíche de Carneiro (T6)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Carne de Carneiro", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Pão", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Manteiga", 4, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Sanduíche de Carne de Panela (T8)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Carne de Boi", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Pão", 24, BigDecimal.ZERO),
                        new IngredienteItemEntity("Manteiga", 4, BigDecimal.ZERO)
                )
        );

        // 6. Omeletes (Redução de Cooldown e Conjuração)
        cadastrarSeNaoExistir(
                "Omelete de Frango (T3)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Carne de Frango", 12, BigDecimal.ZERO),
                        new IngredienteItemEntity("Ovo de Galinha", 12, BigDecimal.ZERO),
                        new IngredienteItemEntity("Leite de Cabra", 6, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Omelete de Ganso (T5)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Carne de Ganso", 12, BigDecimal.ZERO),
                        new IngredienteItemEntity("Ovo de Ganso", 12, BigDecimal.ZERO),
                        new IngredienteItemEntity("Leite de Cabra", 6, BigDecimal.ZERO)
                )
        );
        cadastrarSeNaoExistir(
                "Omelete de Porco (T7)",
                "CULINARIA",
                10,
                List.of(
                        new IngredienteItemEntity("Carne de Porco", 12, BigDecimal.ZERO),
                        new IngredienteItemEntity("Ovo de Ganso", 12, BigDecimal.ZERO),
                        new IngredienteItemEntity("Leite de Ovelha", 6, BigDecimal.ZERO)
                )
        );
    }

    private void cadastrarSeNaoExistir(String nomeItem, String categoria, int rendimento, List<IngredienteItemEntity> ingredientes) {
        if (repository.existsByNomeItemIgnoreCase(nomeItem)) {
            return;
        }

        ItemFabricadoEntity entity = new ItemFabricadoEntity();
        entity.setNomeItem(nomeItem);
        entity.setCategoriaProducao(categoria);
        entity.setRendimentoPorClique(rendimento);
        entity.setQuantidadeCliques(1);
        entity.setTaxaRetorno(BigDecimal.valueOf(15.00));
        entity.setPrecoVendaUnitario(BigDecimal.ZERO);
        entity.setContaPremium(true);
        entity.setVendaInstantanea(false);
        entity.setUsoFoco(false);
        entity.setCustoTotalEstimado(BigDecimal.ZERO);
        entity.setLucroEstimado(BigDecimal.ZERO);

        for (IngredienteItemEntity ing : ingredientes) {
            entity.adicionarIngrediente(ing);
        }

        repository.save(entity);
        log.info("[CLASSE: DatabaseSeeder] Receita padrão cadastrada no H2: {}", nomeItem);
    }
}
