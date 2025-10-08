package com.ecommerce.equipe.repository;

import com.ecommerce.equipe.model.PagamentoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PagamentoRepository extends JpaRepository <PagamentoModel, Integer>{
}
