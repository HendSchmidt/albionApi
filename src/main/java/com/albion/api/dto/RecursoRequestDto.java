package com.albion.api.dto;

import java.math.BigDecimal;

public record RecursoRequestDto(String nome,
								int quantidade,
								BigDecimal valor)
{
}
