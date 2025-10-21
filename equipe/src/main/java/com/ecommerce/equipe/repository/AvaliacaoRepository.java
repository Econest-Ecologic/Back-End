package com.ecommerce.equipe.repository;

import com.ecommerce.equipe.model.AvaliacaoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliacaoRepository extends JpaRepository<AvaliacaoModel, Integer> {
    List<AvaliacaoModel> findByCdProdutoCdProduto(Integer cdProduto);
}