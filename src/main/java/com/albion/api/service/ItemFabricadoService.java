package com.albion.api.service;

import com.albion.api.dto.CraftRequestDto;
import com.albion.api.dto.CraftResponseDto;
import com.albion.api.dto.ItemSalvoResponseDto;
import com.albion.api.dto.RecursoRequestDto;
import com.albion.api.entity.IngredienteItemEntity;
import com.albion.api.entity.ItemFabricadoEntity;
import com.albion.api.repository.ItemFabricadoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ItemFabricadoService {

    private static final Logger log = LoggerFactory.getLogger(ItemFabricadoService.class);

    private final ItemFabricadoRepository repository;
    private final CalculaViabilidadeDeProdcao calculaViabilidadeDeProdcao;

    public ItemFabricadoService(
            ItemFabricadoRepository repository,
            CalculaViabilidadeDeProdcao calculaViabilidadeDeProdcao) {
        this.repository = repository;
        this.calculaViabilidadeDeProdcao = calculaViabilidadeDeProdcao;
    }

    @Transactional
    public ItemSalvoResponseDto salvarItemFabricado(CraftRequestDto request) {
        log.info("[CLASSE: ItemFabricadoService] [METODO: salvarItemFabricado] [ENTRADA: {}]", request);

        if (request == null) {
            throw new IllegalArgumentException("O payload de fabricação não pode ser nulo.");
        }

        String nome = (request.nomeItem() != null && !request.nomeItem().trim().isEmpty())
                ? request.nomeItem().trim()
                : "Receita Customizada";

        ItemFabricadoEntity entity = new ItemFabricadoEntity();
        entity.setNomeItem(nome);
        entity.setCategoriaProducao(request.categoriaProducao() != null ? request.categoriaProducao() : "CULINARIA");
        entity.setRendimentoPorClique(request.rendimentoPorClique() != null ? request.rendimentoPorClique() : 1);
        entity.setQuantidadeCliques(request.quantidade());
        entity.setTaxaRetorno(BigDecimal.valueOf(request.taxaRetorno()));
        entity.setPrecoVendaUnitario(request.valorVenda());
        entity.setContaPremium(request.contaPremium());
        entity.setTaxaEstacaoPorCemNutricao(request.taxaEstacaoPorCemNutricao());
        entity.setItemValue(request.itemValue());
        entity.setQuantidadeDiarios(request.quantidadeDiarios());
        entity.setValorCompraDiarioVazio(request.valorCompraDiarioVazio());
        entity.setValorVendaDiarioCheio(request.valorVendaDiarioCheio());
        entity.setVendaInstantanea(request.vendaInstantanea());
        entity.setUsoFoco(request.usoFoco());
        entity.setPontosFoco(request.pontosFoco());

        // Calcula custo e lucro estimados para armazenar como histórico
        try {
            CraftResponseDto simulacao = calculaViabilidadeDeProdcao.calcular(request);
            if (simulacao != null) {
                entity.setCustoTotalEstimado(simulacao.custoTotalDaProdcao());
                entity.setLucroEstimado(simulacao.lucro());
            }
        } catch (Exception e) {
            log.warn("[CLASSE: ItemFabricadoService] Erro ao simular viabilidade prévia: {}", e.getMessage());
        }

        // Ingredientes
        if (request.recurso() != null) {
            for (RecursoRequestDto r : request.recurso()) {
                if (r != null && r.nome() != null && !r.nome().trim().isEmpty()) {
                    IngredienteItemEntity ing = new IngredienteItemEntity(
                            r.nome().trim(),
                            r.quantidade(),
                            r.valor() != null ? r.valor() : BigDecimal.ZERO
                    );
                    entity.adicionarIngrediente(ing);
                }
            }
        }

        ItemFabricadoEntity salvo = repository.save(entity);
        log.info("[CLASSE: ItemFabricadoService] [METODO: salvarItemFabricado] [SAIDA: ID={}]", salvo.getId());

        return mapearParaDto(salvo);
    }

    @Transactional(readOnly = true)
    public List<ItemSalvoResponseDto> listarTodos() {
        log.info("[CLASSE: ItemFabricadoService] [METODO: listarTodos]");
        return repository.findAllByOrderByDataCriacaoDesc()
                .stream()
                .map(this::mapearParaDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<ItemSalvoResponseDto> buscarPorNome(String termo) {
        log.info("[CLASSE: ItemFabricadoService] [METODO: buscarPorNome] [ENTRADA: termo={}]", termo);
        if (termo == null || termo.trim().isEmpty()) {
            return listarTodos();
        }
        return repository.buscarPorNome(termo.trim())
                .stream()
                .map(this::mapearParaDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public Optional<ItemSalvoResponseDto> buscarPorId(Long id) {
        log.info("[CLASSE: ItemFabricadoService] [METODO: buscarPorId] [ENTRADA: id={}]", id);
        return repository.findById(id).map(this::mapearParaDto);
    }

    @Transactional
    public boolean deletar(Long id) {
        log.info("[CLASSE: ItemFabricadoService] [METODO: deletar] [ENTRADA: id={}]", id);
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return true;
        }
        return false;
    }

    private ItemSalvoResponseDto mapearParaDto(ItemFabricadoEntity entity) {
        List<RecursoRequestDto> recursos = entity.getIngredientes().stream()
                .map(ing -> new RecursoRequestDto(ing.getNome(), ing.getQuantidade(), ing.getValor()))
                .toList();

        return new ItemSalvoResponseDto(
                entity.getId(),
                entity.getNomeItem(),
                entity.getCategoriaProducao(),
                entity.getRendimentoPorClique(),
                entity.getQuantidadeCliques(),
                entity.getTaxaRetorno(),
                entity.getPrecoVendaUnitario(),
                entity.isContaPremium(),
                entity.getTaxaEstacaoPorCemNutricao(),
                entity.getItemValue(),
                entity.getQuantidadeDiarios(),
                entity.getValorCompraDiarioVazio(),
                entity.getValorVendaDiarioCheio(),
                entity.isVendaInstantanea(),
                entity.isUsoFoco(),
                entity.getPontosFoco(),
                entity.getCustoTotalEstimado(),
                entity.getLucroEstimado(),
                entity.getDataCriacao(),
                recursos
        );
    }
}
