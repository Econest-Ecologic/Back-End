package com.ecommerce.equipe.repository;

import com.ecommerce.equipe.model.ItemPedidoModel;
import com.ecommerce.equipe.model.PagamentoModel;
import com.ecommerce.equipe.model.PedidoModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository <ItemPedidoModel, Integer>{
    List<ItemPedidoModel> findByPedidoCdPedido(Integer cdPedido);
}
