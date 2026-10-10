package com.albion.api.domain.port.out;

import com.albion.api.domain.model.ItemFabricado;

import java.util.List;
import java.util.Optional;

/**
 * Driven Port (Outbound) para persistência de itens fabricados (desacoplada de JPA/H2).
 */
public interface ItemFabricadoRepositoryPort {
    ItemFabricado salvar(ItemFabricado item);
    List<ItemFabricado> listarTodos();
    List<ItemFabricado> buscarPorNome(String termo);
    Optional<ItemFabricado> buscarPorId(Long id);
    boolean deletar(Long id);
    boolean existePorNome(String nome);
    long contar();
}
