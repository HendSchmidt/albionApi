package com.albion.api.domain.port.in;

/**
 * Driver Port (Inbound) para exclusão de receitas salvas.
 */
public interface DeletarItemFabricadoUseCase {
    boolean deletar(Long id);
}
