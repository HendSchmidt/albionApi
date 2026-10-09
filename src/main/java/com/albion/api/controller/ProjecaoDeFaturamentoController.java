package com.albion.api.controller;

import com.albion.api.dto.CraftRequestDto;
import com.albion.api.dto.CraftResponseDto;
import com.albion.api.dto.FoodNutritionSaleRequestDto;
import com.albion.api.dto.FoodNutritionSaleResponseDto;
import com.albion.api.service.CalculaNutricaoBarraquinhaService;
import com.albion.api.service.CalculaViabilidadeDeProdcao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
public class ProjecaoDeFaturamentoController {

    private static final Logger log = LoggerFactory.getLogger(ProjecaoDeFaturamentoController.class);

    private final CalculaViabilidadeDeProdcao calculaViabilidadeDeProdcao;
    private final CalculaNutricaoBarraquinhaService calculaNutricaoBarraquinhaService;

    public ProjecaoDeFaturamentoController(
            CalculaViabilidadeDeProdcao calculaViabilidadeDeProdcao,
            CalculaNutricaoBarraquinhaService calculaNutricaoBarraquinhaService) {
        this.calculaViabilidadeDeProdcao = calculaViabilidadeDeProdcao;
        this.calculaNutricaoBarraquinhaService = calculaNutricaoBarraquinhaService;
    }

    @PostMapping("/calculaViabilidadePorRecurso")
    public ResponseEntity<CraftResponseDto> calculaViabilidadePorRecurso(@RequestBody CraftRequestDto request) {
        log.info("[CLASSE: ProjecaoDeFaturamentoController] [METODO: calculaViabilidadePorRecurso] [ENTRADA: {}]", request);
        CraftResponseDto response = calculaViabilidadeDeProdcao.calcula(request);
        log.info("[CLASSE: ProjecaoDeFaturamentoController] [METODO: calculaViabilidadePorRecurso] [SAIDA: {}]", response);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/calculaVendaComidaBarraquinha")
    public ResponseEntity<FoodNutritionSaleResponseDto> calculaVendaComidaBarraquinha(@RequestBody FoodNutritionSaleRequestDto request) {
        log.info("[CLASSE: ProjecaoDeFaturamentoController] [METODO: calculaVendaComidaBarraquinha] [ENTRADA: {}]", request);
        FoodNutritionSaleResponseDto response = calculaNutricaoBarraquinhaService.calcular(request);
        log.info("[CLASSE: ProjecaoDeFaturamentoController] [METODO: calculaVendaComidaBarraquinha] [SAIDA: {}]", response);
        return ResponseEntity.ok(response);
    }
}
