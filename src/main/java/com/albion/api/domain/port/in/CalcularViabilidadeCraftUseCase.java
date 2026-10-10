package com.albion.api.domain.port.in;

import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.domain.model.ViabilidadeCraft;

/**
 * Driver Port (Inbound) para execução do caso de uso de cálculo de viabilidade de craft.
 */
public interface CalcularViabilidadeCraftUseCase {
    ViabilidadeCraft calcular(ItemFabricado item);
}
