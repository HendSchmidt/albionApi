package com.albion.api.controller;

import com.albion.api.dto.CraftRequestDto;
import com.albion.api.dto.CraftResponseDto;
import com.albion.api.service.CalculaViabilidadeDeProdcao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*")
public class ProjecaoDeFaturamentoController {

	private static final Logger log = LoggerFactory.getLogger(ProjecaoDeFaturamentoController.class);

	private final CalculaViabilidadeDeProdcao calculaViabilidadeDeProdcao;

	public ProjecaoDeFaturamentoController(CalculaViabilidadeDeProdcao calculaViabilidadeDeProdcao) {
		this.calculaViabilidadeDeProdcao = calculaViabilidadeDeProdcao;
	}

	@PostMapping(value = "/calculaViabilidadePorRecurso")
	public ResponseEntity<CraftResponseDto> calculaFaturamento(@RequestBody CraftRequestDto request) {
		log.info("[Classe: {}] [Metodo: {}] [Entrada: {}]", 
				"ProjecaoDeFaturamentoController", 
				"calculaFaturamento", 
				request);

		CraftResponseDto response = calculaViabilidadeDeProdcao.calcular(request);

		log.info("[Classe: {}] [Metodo: {}] [Saida: {}]", 
				"ProjecaoDeFaturamentoController", 
				"calculaFaturamento", 
				response);

		return ResponseEntity.ok(response);
	}
}
