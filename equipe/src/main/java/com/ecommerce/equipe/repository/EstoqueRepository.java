package com.ecommerce.equipe.repository;

import com.ecommerce.equipe.model.EstoqueModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstoqueRepository extends JpaRepository <EstoqueModel, Integer>{
}
