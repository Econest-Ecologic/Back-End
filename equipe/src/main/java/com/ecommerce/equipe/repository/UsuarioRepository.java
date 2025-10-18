package com.ecommerce.equipe.repository;

import com.ecommerce.equipe.model.UsuarioModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository <UsuarioModel, Integer>{
    List<UsuarioModel> findByAtivoTrue();

}

