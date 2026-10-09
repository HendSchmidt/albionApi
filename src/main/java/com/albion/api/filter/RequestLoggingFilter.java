package com.albion.api.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestLoggingFilter extends OncePerRequestFilter {

	private static final Logger log = LoggerFactory.getLogger(RequestLoggingFilter.class);

	@Override
	protected void doFilterInternal(HttpServletRequest request,
									HttpServletResponse response,
									FilterChain filterChain) throws ServletException, IOException {
		String method = request.getMethod();
		String uri = request.getRequestURI();
		long startTime = System.currentTimeMillis();

		log.info("[Classe: {}] [Metodo: {}] [Entrada: HTTP {} {}]",
				"RequestLoggingFilter",
				"doFilterInternal",
				method,
				uri);

		try {
			filterChain.doFilter(request, response);
		} finally {
			long duration = System.currentTimeMillis() - startTime;
			log.info("[Classe: {}] [Metodo: {}] [Saida: HTTP {} {} - Status: {} ({}ms)]",
					"RequestLoggingFilter",
					"doFilterInternal",
					method,
					uri,
					response.getStatus(),
					duration);
		}
	}
}
