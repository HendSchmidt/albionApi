package com.albion.api.infrastructure.adapter.out.persistence.seeder;

import com.albion.api.domain.model.ItemFabricado;
import com.albion.api.domain.port.out.ItemFabricadoRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DatabaseSeederTest {

    @Mock
    private ItemFabricadoRepositoryPort repositoryPort;

    @InjectMocks
    private DatabaseSeeder databaseSeeder;

    @Test
    @DisplayName("Deve cadastrar todas as 18 receitas de culinária padrão quando o banco estiver vazio")
    void deveCadastrarTodasAs18ReceitasPadraoQuandoBancoVazio() {
        when(repositoryPort.existePorNome(anyString())).thenReturn(false);
        when(repositoryPort.contar()).thenReturn(18L);

        databaseSeeder.run();

        verify(repositoryPort, times(18)).salvar(any(ItemFabricado.class));
        verify(repositoryPort, atLeastOnce()).contar();
    }

    @Test
    @DisplayName("Não deve duplicar receitas caso já estejam cadastradas no banco")
    void naoDeveDuplicarReceitasCasoJaExistam() {
        when(repositoryPort.existePorNome(anyString())).thenReturn(true);
        when(repositoryPort.contar()).thenReturn(18L);

        databaseSeeder.run();

        verify(repositoryPort, never()).salvar(any(ItemFabricado.class));
        verify(repositoryPort, atLeastOnce()).contar();
    }
}
