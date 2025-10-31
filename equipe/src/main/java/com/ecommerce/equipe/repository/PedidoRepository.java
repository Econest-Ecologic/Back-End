package com.ecommerce.equipe.repository;

import com.ecommerce.equipe.model.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<PedidoModel, Integer> {

    @Query("SELECT p FROM PedidoModel p WHERE p.usuario.cdUsuario = :cdUsuario AND p.flAtivo = true")
    List<PedidoModel> findByUsuarioCdUsuario(@Param("cdUsuario") Integer cdUsuario);

    @Query("SELECT p FROM PedidoModel p WHERE p.flAtivo = true")
    List<PedidoModel> findAllAtivos();
}