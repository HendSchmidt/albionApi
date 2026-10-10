package com.albion.api.infrastructure.adapter.in.web;

import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.domain.port.in.BuscarItemFabricadoUseCase;
import com.albion.api.domain.port.in.DeletarItemFabricadoUseCase;
import com.albion.api.domain.port.in.SalvarItemFabricadoUseCase;
import com.albion.api.infrastructure.adapter.in.web.dto.CraftRequestDto;
import com.albion.api.infrastructure.adapter.in.web.dto.ItemSalvoResponseDto;
import com.albion.api.infrastructure.adapter.in.web.mapper.ItemFabricadoDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Driver Adapter REST para persistência e gestão de itens fabricados.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/itensFabricados")
public class ItemFabricadoController {

    private static final Logger log = LoggerFactory.getLogger(ItemFabricadoController.java);

    private final SalvarItemFabricadoUseCase salvarUseCase;
    private final BuscarItemFabricadoUseCase buscarUseCase;
    private final DeletarItemFabricadoUseCase deletarUseCase;
    private final ItemFabricadoDtoMapper dtoMapper;

    public ItemFabricadoController(
            SalvarItemFabricadoUseCase salvarUseCase,
            BuscarItemFabricadoUseCase buscarUseCase,
            DeletarItemFabricadoUseCase deletarUseCase,
            ItemFabricadoDtoMapper dtoMapper) {
        this.salvarUseCase = salvarUseCase;
        this.buscarUseCase = buscarUseCase;
        this.deletarUseCase = deletarUseCase;
        this.dtoMapper = dtoMapper;
    }

    @PostMapping
    public ResponseEntity<ItemSalvoResponseDto> salvarItem(@RequestBody CraftRequestDto request) {
        log.info("[REST Adapter: ItemFabricadoController] [POST /itensFabricados] [Entrada: {}]", request);
        ItemFabricado item = dtoMapper.paraDominio(request);
        ItemFabricado salvo = salvarUseCase.salvar(item);
        ItemSalvoResponseDto response = dtoMapper.paraDto(salvo);
        log.info("[REST Adapter: ItemFabricadoController] [POST /itensFabricados] [Criado ID={}]", salvo.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<ItemSalvoResponseDto>> listarOuBuscar(
            @RequestParam(value = "busca", required = false) String busca) {
        log.info("[REST Adapter: ItemFabricadoController] [GET /itensFabricados] [Busca: {}]", busca);
        List<ItemFabricado> lista = (busca != null && !busca.trim().isEmpty())
                ? buscarUseCase.buscarPorNome(busca)
                : buscarUseCase.listarTodos();

        List<ItemSalvoResponseDto> dtos = lista.stream().map(dtoMapper::paraDto).toList();
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemSalvoResponseDto> buscarPorId(@PathVariable Long id) {
        log.info("[REST Adapter: ItemFabricadoController] [GET /itensFabricados/{}]", id);
        return buscarUseCase.buscarPorId(id)
                .map(dtoMapper::paraDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("[REST Adapter: ItemFabricadoController] [DELETE /itensFabricados/{}]", id);
        boolean removido = deletarUseCase.deletar(id);
        if (removido) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
