package com.albion.api.dto;

import java.math.BigDecimal;
import java.util.List;

public record CraftResponseDto(
		BigDecimal  custoTotalDaProdcao,
		List<RecursoResponseDto> custoPorRecurso,
		BigDecimal lucro
) {
}
