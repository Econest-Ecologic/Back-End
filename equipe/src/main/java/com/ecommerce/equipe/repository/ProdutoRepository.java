package com.ecommerce.equipe.repository;

import com.ecommerce.equipe.model.PedidoModel;
import com.ecommerce.equipe.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository <ProdutoModel, Integer>{

    @Query("SELECT p FROM ProdutoModel p WHERE p.flAtivo = true")
    List<PedidoModel> findAllAtivos();
}
