package com.albion.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class ApiApplication {

	private static final Logger log = LoggerFactory.getLogger(ApiApplication.class);

	public static void main(String[] args) {
		log.info("[Classe: {}] [Metodo: {}] [Entrada: args={}] - Iniciando Albion API Spring Boot", 
				"ApiApplication", 
				"main", 
				Arrays.toString(args));

		SpringApplication.run(ApiApplication.class, args);

		log.info("[Classe: {}] [Metodo: {}] [Saida: Contexto Spring Boot carregado com sucesso]", 
				"ApiApplication", 
				"main");
	}

}
