package com.albion.api.domain.port.in;

import com.albion.api.domain.model.ItemFabricado;

import java.util.List;
import java.util.Optional;

/**
 * Driver Port (Inbound) para consulta e busca de receitas de itens fabricados.
 */
public interface BuscarItemFabricadoUseCase {
    List<ItemFabricado> listarTodos();
    List<ItemFabricado> buscarPorNome(String termo);
    Optional<ItemFabricado> buscarPorId(Long id);
}
