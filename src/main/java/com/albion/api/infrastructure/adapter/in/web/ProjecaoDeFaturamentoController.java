package com.albion.api.infrastructure.adapter.in.web;

import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.domain.model.VendaBarraquinha;
import com.albion.api.domain.model.ViabilidadeCraft;
import com.albion.api.domain.port.in.CalcularVendaBarraquinhaUseCase;
import com.albion.api.domain.port.in.CalcularViabilidadeCraftUseCase;
import com.albion.api.infrastructure.adapter.in.web.dto.CraftRequestDto;
import com.albion.api.infrastructure.adapter.in.web.dto.CraftResponseDto;
import com.albion.api.infrastructure.adapter.in.web.dto.FoodNutritionSaleRequestDto;
import com.albion.api.infrastructure.adapter.in.web.dto.FoodNutritionSaleResponseDto;
import com.albion.api.infrastructure.adapter.in.web.mapper.BarraquinhaDtoMapper;
import com.albion.api.infrastructure.adapter.in.web.mapper.CraftDtoMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Driver Adapter REST para Projeção de Faturamento e Viabilidade Econômica.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/projecaoFaturamento")
public class ProjecaoDeFaturamentoController {

    private static final Logger log = LoggerFactory.getLogger(ProjecaoDeFaturamentoController.class);

    private final CalcularViabilidadeCraftUseCase calcularViabilidadeUseCase;
    private final CalcularVendaBarraquinhaUseCase calcularVendaBarraquinhaUseCase;
    private final CraftDtoMapper craftDtoMapper;
    private final BarraquinhaDtoMapper barraquinhaDtoMapper;

    public ProjecaoDeFaturamentoController(
            CalcularViabilidadeCraftUseCase calcularViabilidadeUseCase,
            CalcularVendaBarraquinhaUseCase calcularVendaBarraquinhaUseCase,
            CraftDtoMapper craftDtoMapper,
            BarraquinhaDtoMapper barraquinhaDtoMapper) {
        this.calcularViabilidadeUseCase = calcularViabilidadeUseCase;
        this.calcularVendaBarraquinhaUseCase = calcularVendaBarraquinhaUseCase;
        this.craftDtoMapper = craftDtoMapper;
        this.barraquinhaDtoMapper = barraquinhaDtoMapper;
    }

    @PostMapping("/calculaViabilidadePorRecurso")
    public ResponseEntity<CraftResponseDto> calculaViabilidadePorRecurso(@RequestBody CraftRequestDto request) {
        log.info("[REST Adapter: ProjecaoDeFaturamentoController] [POST /calculaViabilidadePorRecurso] [Entrada: {}]", request);
        ItemFabricado item = craftDtoMapper.paraDominio(request);
        ViabilidadeCraft resultado = calcularViabilidadeUseCase.calcular(item);
        CraftResponseDto response = craftDtoMapper.paraDto(resultado);
        log.info("[REST Adapter: ProjecaoDeFaturamentoController] [POST /calculaViabilidadePorRecurso] [Saida: Lucro={}]", response.lucro());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculaVendaComidaBarraquinha")
    public ResponseEntity<FoodNutritionSaleResponseDto> calculaVendaComidaBarraquinha(@RequestBody FoodNutritionSaleRequestDto request) {
        log.info("[REST Adapter: ProjecaoDeFaturamentoController] [POST /calculaVendaComidaBarraquinha] [Entrada: {}]", request);
        if (request == null) {
            return ResponseEntity.badRequest().build();
        }

        List<Ingrediente> ingredientes = barraquinhaDtoMapper.paraIngredientes(request.ingredientes());
        VendaBarraquinha resultado = calcularVendaBarraquinhaUseCase.calcular(
                request.nomeComida(),
                request.nutricaoPorUnidade(),
                request.comidaFavorita(),
                request.valorPorCemNutricao(),
                request.quantidadeProducao(),
                request.taxaDeRetorno(),
                request.precoMercadoUnitario(),
                request.contaPremium(),
                request.ordemDeVenda(),
                ingredientes
        );

        FoodNutritionSaleResponseDto response = barraquinhaDtoMapper.paraDto(resultado);
        log.info("[REST Adapter: ProjecaoDeFaturamentoController] [POST /calculaVendaComidaBarraquinha] [Saida: Opcao={}]", response.melhorOpcao());
        return ResponseEntity.ok(response);
    }
}
