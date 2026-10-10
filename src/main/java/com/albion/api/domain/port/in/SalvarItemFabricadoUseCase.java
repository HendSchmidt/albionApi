package com.albion.api.domain.port.in;

import com.albion.api.domain.model.ItemFabricado;

/**
 * Driver Port (Inbound) para salvar receitas de itens fabricados.
 */
public interface SalvarItemFabricadoUseCase {
    ItemFabricado salvar(ItemFabricado item);
}
