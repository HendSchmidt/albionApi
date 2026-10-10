package com.albion.api.application.service;

import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.domain.model.ViabilidadeCraft;
import com.albion.api.domain.port.in.CalcularViabilidadeCraftUseCase;
import com.albion.api.domain.service.CraftViabilidadeDomainService;
import org.springframework.stereotype.Service;

/**
 * Caso de uso orquestrando o cálculo de viabilidade de craft.
 */
@Service
public class CalcularViabilidadeCraftApplicationService implements CalcularViabilidadeCraftUseCase {

    private final CraftViabilidadeDomainService domainService;

    public CalcularViabilidadeCraftApplicationService() {
        this.domainService = new CraftViabilidadeDomainService();
    }

    public CalcularViabilidadeCraftApplicationService(CraftViabilidadeDomainService domainService) {
        this.domainService = domainService;
    }

    @Override
    public ViabilidadeCraft calcular(ItemFabricado item) {
        return domainService.calcular(item);
    }
}
