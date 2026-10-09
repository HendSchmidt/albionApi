package com.albion.api.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record ItemSalvoResponseDto(
        Long id,
        String nomeItem,
        String categoriaProducao,
        Integer rendimentoPorClique,
        int quantidadeCliques,
        BigDecimal taxaRetorno,
        BigDecimal precoVendaUnitario,
        boolean contaPremium,
        BigDecimal taxaEstacaoPorCemNutricao,
        BigDecimal itemValue,
        Integer quantidadeDiarios,
        BigDecimal valorCompraDiarioVazio,
        BigDecimal valorVendaDiarioCheio,
        boolean vendaInstantanea,
        boolean usoFoco,
        Integer pontosFoco,
        BigDecimal custoTotalEstimado,
        BigDecimal lucroEstimado,
        LocalDateTime dataCriacao,
        List<RecursoRequestDto> ingredientes
) {
}
