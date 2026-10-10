package com.albion.api.application.service;

import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.domain.model.ViabilidadeCraft;
import com.albion.api.domain.port.in.BuscarItemFabricadoUseCase;
import com.albion.api.domain.port.in.DeletarItemFabricadoUseCase;
import com.albion.api.domain.port.in.SalvarItemFabricadoUseCase;
import com.albion.api.domain.port.out.ItemFabricadoRepositoryPort;
import com.albion.api.domain.service.CraftViabilidadeDomainService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Caso de uso orquestrando a gestão e persistência de receitas de itens fabricados.
 */
@Service
public class ItemFabricadoApplicationService implements SalvarItemFabricadoUseCase, BuscarItemFabricadoUseCase, DeletarItemFabricadoUseCase {

    private static final Logger log = LoggerFactory.getLogger(ItemFabricadoApplicationService.class);

    private final ItemFabricadoRepositoryPort repositoryPort;
    private final CraftViabilidadeDomainService viabilidadeDomainService;

    public ItemFabricadoApplicationService(ItemFabricadoRepositoryPort repositoryPort) {
        this.repositoryPort = repositoryPort;
        this.viabilidadeDomainService = new CraftViabilidadeDomainService();
    }

    public ItemFabricadoApplicationService(ItemFabricadoRepositoryPort repositoryPort,
                                          CraftViabilidadeDomainService viabilidadeDomainService) {
        this.repositoryPort = repositoryPort;
        this.viabilidadeDomainService = viabilidadeDomainService;
    }

    @Override
    public ItemFabricado salvar(ItemFabricado item) {
        log.info("[ApplicationService: ItemFabricadoApplicationService] [Salvar: {}]", item.getNomeItem());

        // Simula viabilidade prévia para estimar custo e lucro histórico
        try {
            ViabilidadeCraft viabilidade = viabilidadeDomainService.calcular(item);
            if (viabilidade != null) {
                item.atualizarEstimativas(viabilidade.custoTotalProducao(), viabilidade.lucro());
            }
        } catch (Exception e) {
            log.warn("[ApplicationService: ItemFabricadoApplicationService] Erro ao estimar viabilidade: {}", e.getMessage());
        }

        return repositoryPort.salvar(item);
    }

    @Override
    public List<ItemFabricado> listarTodos() {
        return repositoryPort.listarTodos();
    }

    @Override
    public List<ItemFabricado> buscarPorNome(String termo) {
        if (termo == null || termo.trim().isEmpty()) {
            return listarTodos();
        }
        return repositoryPort.buscarPorNome(termo.trim());
    }

    @Override
    public Optional<ItemFabricado> buscarPorId(Long id) {
        return repositoryPort.buscarPorId(id);
    }

    @Override
    public boolean deletar(Long id) {
        return repositoryPort.deletar(id);
    }
}
