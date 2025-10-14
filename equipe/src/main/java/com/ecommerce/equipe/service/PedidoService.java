package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.ItemPedidoDto;
import com.ecommerce.equipe.model.ItemPedidoModel;
import com.ecommerce.equipe.model.PedidoModel;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.repository.ItemPedidoRepository;
import com.ecommerce.equipe.repository.PedidoRepository;
import com.ecommerce.equipe.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final ItemPedidoRepository itemPedidoRepository;

    private ItemPedidoModel converterParaModel(Integer cdPedido, ItemPedidoDto dto) {
        ItemPedidoModel model = new ItemPedidoModel();

        PedidoModel pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        ProdutoModel produto = produtoRepository.findById(dto.cdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        model.setCdItemPedido(pedido);
        model.setCdProduto(produto);
        model.setPrecoUnitario(dto.precoUnitario());
        model.setQtdItem(dto.qtdItem());
        return model;
    }

    private ItemPedidoDto converterParaDto(ItemPedidoModel item) {
        return new ItemPedidoDto(
                item.getCdProduto(),
                item.getCdItemPedido(),
                item.getQtdItem(),
                item.getPrecoUnitario()
        );
    }
}
