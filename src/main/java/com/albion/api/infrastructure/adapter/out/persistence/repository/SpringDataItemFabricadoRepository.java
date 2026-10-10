package com.albion.api.infrastructure.adapter.out.persistence.repository;

import com.albion.api.infrastructure.adapter.out.persistence.entity.ItemFabricadoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SpringDataItemFabricadoRepository extends JpaRepository<ItemFabricadoJpaEntity, Long> {

    List<ItemFabricadoJpaEntity> findAllByOrderByDataCriacaoDesc();

    @Query("SELECT i FROM ItemFabricadoJpaEntity i WHERE LOWER(i.nomeItem) LIKE LOWER(CONCAT('%', :termo, '%')) ORDER BY i.dataCriacao DESC")
    List<ItemFabricadoJpaEntity> buscarPorNome(@Param("termo") String termo);

    boolean existsByNomeItemIgnoreCase(String nomeItem);
}
