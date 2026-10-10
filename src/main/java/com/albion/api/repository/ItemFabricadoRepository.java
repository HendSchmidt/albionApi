package com.albion.api.repository;

import com.albion.api.entity.ItemFabricadoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemFabricadoRepository extends JpaRepository<ItemFabricadoEntity, Long> {

    List<ItemFabricadoEntity> findAllByOrderByDataCriacaoDesc();

    @Query("SELECT i FROM ItemFabricadoEntity i WHERE LOWER(i.nomeItem) LIKE LOWER(CONCAT('%', :termo, '%')) ORDER BY i.dataCriacao DESC")
    List<ItemFabricadoEntity> buscarPorNome(@Param("termo") String termo);

    List<ItemFabricadoEntity> findByCategoriaProducaoOrderByDataCriacaoDesc(String categoriaProducao);

    boolean existsByNomeItemIgnoreCase(String nomeItem);
}
