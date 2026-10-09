package com.albion.api.controller;

import com.albion.api.dto.CraftRequestDto;
import com.albion.api.dto.ItemSalvoResponseDto;
import com.albion.api.service.ItemFabricadoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/itensFabricados")
public class ItemFabricadoController {

    private static final Logger log = LoggerFactory.getLogger(ItemFabricadoController.java);

    private final ItemFabricadoService service;

    public ItemFabricadoController(ItemFabricadoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<ItemSalvoResponseDto> salvarItem(@RequestBody CraftRequestDto request) {
        log.info("[CLASSE: ItemFabricadoController] [METODO: salvarItem] [ENTRADA: {}]", request);
        ItemSalvoResponseDto salvo = service.salvarItemFabricado(request);
        log.info("[CLASSE: ItemFabricadoController] [METODO: salvarItem] [SAIDA: ID={}]", salvo.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(salvo);
    }

    @GetMapping
    public ResponseEntity<List<ItemSalvoResponseDto>> listarOuBuscar(
            @RequestParam(value = "busca", required = false) String busca) {
        log.info("[CLASSE: ItemFabricadoController] [METODO: listarOuBuscar] [PARAM: busca={}]", busca);
        List<ItemSalvoResponseDto> lista = (busca != null && !busca.trim().isEmpty())
                ? service.buscarPorNome(busca)
                : service.listarTodos();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemSalvoResponseDto> buscarPorId(@PathVariable Long id) {
        log.info("[CLASSE: ItemFabricadoController] [METODO: buscarPorId] [PARAM: id={}]", id);
        return service.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        log.info("[CLASSE: ItemFabricadoController] [METODO: deletar] [PARAM: id={}]", id);
        boolean removido = service.deletar(id);
        if (removido) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
