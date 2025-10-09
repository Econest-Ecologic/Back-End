package com.ecommerce.equipe.repository;

import com.ecommerce.equipe.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository <ProdutoModel, Integer>{
    Optional<ProdutoModel> findById(Integer cdProduto);
}
