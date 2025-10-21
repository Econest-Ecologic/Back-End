package com.ecommerce.equipe.repository;

import com.ecommerce.equipe.model.ProdutoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<ProdutoModel, Integer> {

    @Query("SELECT p FROM ProdutoModel p WHERE LOWER(p.nmProduto) LIKE LOWER(CONCAT('%', :nome, '%')) AND p.flAtivo = true")
    List<ProdutoModel> buscarPorNome(@Param("nome") String nome);

    @Query("SELECT p FROM ProdutoModel p WHERE LOWER(p.categoria) = LOWER(:categoria) AND p.flAtivo = true")
    List<ProdutoModel> buscarPorCategoria(@Param("categoria") String categoria);
}