package com.albion.api.infrastructure.adapter.out.persistence.adapter;

import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.domain.port.out.ItemFabricadoRepositoryPort;
import com.albion.api.infrastructure.adapter.out.persistence.entity.ItemFabricadoJpaEntity;
import com.albion.api.infrastructure.adapter.out.persistence.mapper.ItemFabricadoEntityMapper;
import com.albion.api.infrastructure.adapter.out.persistence.repository.SpringDataItemFabricadoRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Driven Adapter que implementa a porta de persistência ItemFabricadoRepositoryPort
 * conectando o domínio com o Spring Data JPA / H2.
 */
@Component
public class ItemFabricadoRepositoryAdapter implements ItemFabricadoRepositoryPort {

    private final SpringDataItemFabricadoRepository repository;
    private final ItemFabricadoEntityMapper mapper;

    public ItemFabricadoRepositoryAdapter(SpringDataItemFabricadoRepository repository,
                                         ItemFabricadoEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    @Transactional
    public ItemFabricado salvar(ItemFabricado item) {
        ItemFabricadoJpaEntity entity = mapper.paraJpaEntity(item);
        ItemFabricadoJpaEntity salvo = repository.save(entity);
        return mapper.paraDominio(salvo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemFabricado> listarTodos() {
        return repository.findAllByOrderByDataCriacaoDesc()
                .stream()
                .map(mapper::paraDominio)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemFabricado> buscarPorNome(String termo) {
        return repository.buscarPorNome(termo)
                .stream()
                .map(mapper::paraDominio)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ItemFabricado> buscarPorId(Long id) {
        return repository.findById(id).map(mapper::paraDominio);
    }

    @Override
    @Transactional
    public boolean deletar(Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorNome(String nome) {
        return repository.existsByNomeItemIgnoreCase(nome);
    }

    @Override
    @Transactional(readOnly = true)
    public long contar() {
        return repository.count();
    }
}
