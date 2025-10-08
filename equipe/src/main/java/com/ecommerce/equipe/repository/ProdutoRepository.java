package com.ecommerce.equipe.repository;

import com.ecommerce.equipe.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProdutoRepository extends JpaRepository <ProdutoModel, Integer>{
}
