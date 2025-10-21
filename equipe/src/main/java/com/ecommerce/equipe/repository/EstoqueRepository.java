package com.ecommerce.equipe.repository;

import com.ecommerce.equipe.model.EstoqueModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EstoqueRepository extends JpaRepository<EstoqueModel, Integer> {
    Optional<EstoqueModel> findByCdProdutoCdProduto(Integer cdProduto);
}