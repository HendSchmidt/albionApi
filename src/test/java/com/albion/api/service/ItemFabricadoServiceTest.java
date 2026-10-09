package com.albion.api.service;

import com.albion.api.dto.CraftRequestDto;
import com.albion.api.dto.ItemSalvoResponseDto;
import com.albion.api.dto.RecursoRequestDto;
import com.albion.api.entity.ItemFabricadoEntity;
import com.albion.api.repository.ItemFabricadoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ItemFabricadoServiceTest {

    @Mock
    private ItemFabricadoRepository repository;

    @Mock
    private CalculaViabilidadeDeProdcao calculaViabilidadeDeProdcao;

    @InjectMocks
    private ItemFabricadoService service;

    @Test
    @DisplayName("Deve salvar receita de item fabricado com ingredientes com sucesso")
    void deveSalvarItemFabricado() {
        CraftRequestDto request = new CraftRequestDto(
                List.of(new RecursoRequestDto("Barra de Ferro T4", 16, new BigDecimal("200.00"))),
                5,
                25,
                new BigDecimal("6000.00"),
                true,
                new BigDecimal("500.00"),
                new BigDecimal("800.00"),
                2,
                new BigDecimal("1200.00"),
                new BigDecimal("5000.00"),
                null,
                false,
                true,
                1000,
                1,
                "EQUIPAMENTO",
                "Espada Larga T4"
        );

        when(repository.save(any(ItemFabricadoEntity.class))).thenAnswer(invocation -> {
            ItemFabricadoEntity entity = invocation.getArgument(0);
            entity.setId(1L);
            return entity;
        });

        ItemSalvoResponseDto response = service.salvarItemFabricado(request);

        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Espada Larga T4", response.nomeItem());
        assertEquals("EQUIPAMENTO", response.categoriaProducao());
        assertEquals(1, response.ingredientes().size());
        assertEquals("Barra de Ferro T4", response.ingredientes().get(0).nome());
        verify(repository).save(any(ItemFabricadoEntity.class));
    }
}
