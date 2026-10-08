package com.albion.api.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProjecaoDeFaturamentoController {

	@PostMapping(value = "/calculaViabilidadePorRecurso")
	public String calculaFaturamento(@RequestBody String obj){
		return obj;
	}
}
