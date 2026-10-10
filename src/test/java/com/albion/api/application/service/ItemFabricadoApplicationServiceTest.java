package com.albion.api.application.service;

import com.albion.api.domain.model.CategoriaProducao;
import com.albion.api.domain.model.Ingrediente;
import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.domain.port.out.ItemFabricadoRepositoryPort;
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
class ItemFabricadoApplicationServiceTest {

    @Mock
    private ItemFabricadoRepositoryPort repositoryPort;

    @InjectMocks
    private ItemFabricadoApplicationService service;

    @Test
    @DisplayName("Deve salvar receita com estimativas calculadas com sucesso")
    void deveSalvarItemFabricado() {
        ItemFabricado item = new ItemFabricado(
                null,
                "Espada Larga T4",
                CategoriaProducao.EQUIPAMENTOS,
                1,
                5,
                BigDecimal.valueOf(25),
                new BigDecimal("6000.00"),
                true,
                new BigDecimal("500.00"),
                new BigDecimal("800.00"),
                2,
                new BigDecimal("1200.00"),
                new BigDecimal("5000.00"),
                true,
                true,
                1000,
                null,
                null,
                null,
                List.of(new Ingrediente("Barra de Ferro T4", 16, new BigDecimal("200.00")))
        );

        when(repositoryPort.salvar(any(ItemFabricado.class))).thenAnswer(invocation -> {
            ItemFabricado arg = invocation.getArgument(0);
            arg.setId(1L);
            return arg;
        });

        ItemFabricado salvo = service.salvar(item);

        assertNotNull(salvo);
        assertEquals(1L, salvo.getId());
        assertEquals("Espada Larga T4", salvo.getNomeItem());
        assertEquals(CategoriaProducao.EQUIPAMENTOS, salvo.getCategoriaProducao());
        assertEquals(1, salvo.getIngredientes().size());
        verify(repositoryPort).salvar(any(ItemFabricado.class));
    }
}
