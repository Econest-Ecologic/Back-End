package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.ItemPedidoDto;
import com.ecommerce.equipe.model.ItemPedidoModel;
import com.ecommerce.equipe.model.PedidoModel;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.repository.ItemPedidoRepository;
import com.ecommerce.equipe.repository.PedidoRepository;
import com.ecommerce.equipe.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;

    public ItemPedidoModel salvar(Integer cdPedido, ItemPedidoDto itemPedidoDto) {
        PedidoModel pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        ItemPedidoModel model = converterParaModel(itemPedidoDto);
        model.setPedido(pedido);

        ItemPedidoModel salvo = itemPedidoRepository.save(model);

        // Recalcula o total do pedido
        calcularValorTotal(cdPedido);

        return salvo;
    }

    public List<ItemPedidoModel> listarPorPedido(Integer cdPedido) {
        return itemPedidoRepository.findByPedidoCdPedido(cdPedido);
    }

    public ItemPedidoModel buscarPorId(Integer cdItemPedido) {
        return itemPedidoRepository.findById(cdItemPedido)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));
    }

    public ItemPedidoModel atualizar(Integer cdItemPedido, ItemPedidoDto itemPedidoDto) {
        ItemPedidoModel item = itemPedidoRepository.findById(cdItemPedido)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        item.setQtdItem(itemPedidoDto.qtdItem());
        item.setPrecoUnitario(itemPedidoDto.precoUnitario());

        ItemPedidoModel atualizado = itemPedidoRepository.save(item);

        // Recalcula o total do pedido
        calcularValorTotal(item.getPedido().getCdPedido());

        return atualizado;
    }

    public void remover(Integer cdItemPedido) {
        ItemPedidoModel item = itemPedidoRepository.findById(cdItemPedido)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        Integer cdPedido = item.getPedido().getCdPedido();
        itemPedidoRepository.delete(item);

        // Recalcula o total do pedido
        calcularValorTotal(cdPedido);
    }

    private void calcularValorTotal(Integer cdPedido) {
        PedidoModel pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        List<ItemPedidoModel> itens = itemPedidoRepository.findByPedidoCdPedido(cdPedido);

        double subtotal = 0.0;
        for (ItemPedidoModel item : itens) {
            subtotal += item.getQtdItem() * item.getPrecoUnitario();
        }

        pedido.setVlTotal(subtotal + pedido.getVlFrete());
        pedidoRepository.save(pedido);
    }

    private ItemPedidoModel converterParaModel(ItemPedidoDto dto) {
        ItemPedidoModel model = new ItemPedidoModel();

        ProdutoModel produto = produtoRepository.findById(dto.cdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        model.setQtdItem(dto.qtdItem());
        model.setPrecoUnitario(dto.precoUnitario());
        model.setCdProduto(produto);

        return model;
    }

    private ItemPedidoDto converterParaDto(ItemPedidoModel model) {
        return new ItemPedidoDto(
                model.getQtdItem(),
                model.getPrecoUnitario(),
                model.getCdProduto().getCdProduto()
        );
    }
}