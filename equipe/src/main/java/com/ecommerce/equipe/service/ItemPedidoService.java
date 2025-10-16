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

@Service
@RequiredArgsConstructor
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final PedidoRepository pedidoRepository;

    // Criar ItemPedido
    public ItemPedidoDto criarItemPedido(Integer cdPedido, ItemPedidoDto dto) {
        ItemPedidoModel model = converterParaModel(cdPedido, dto);
        ItemPedidoModel salvo = itemPedidoRepository.save(model);
        return converterParaDto(salvo);
    }

    // Converter DTO → Model
    private ItemPedidoModel converterParaModel(Integer cdPedido, ItemPedidoDto dto) {
        PedidoModel pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        ProdutoModel produto = produtoRepository.findById(dto.cdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        ItemPedidoModel model = new ItemPedidoModel();
        model.setQtdItem(dto.qtdItem());
        model.setPrecoUnitario(dto.precoUnitario());
        model.setCdProduto(produto); // atribui ProdutoModel
        // Se quiser relacionar o pedido no item, pode adicionar model.setPedido(pedido);
        return model;
    }

    // Converter Model → DTO
    private ItemPedidoDto converterParaDto(ItemPedidoModel model) {
        return new ItemPedidoDto(
                model.getQtdItem(),
                model.getPrecoUnitario(),
                model.getCdProduto().getCdProduto() // retorna só o ID do produto
        );
    }

    // Listar todos os itens de um pedido
    public List<ItemPedidoDto> listarItensDoPedido(Integer cdPedido) {
        PedidoModel pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        // Aqui você precisaria de um método no repositório para buscar itens por pedido
        return itemPedidoRepository.findByPedido(pedido)
                .stream()
                .map(this::converterParaDto)
                .toList();
    }

    // Atualizar item
    public ItemPedidoDto atualizarItemPedido(Integer cdItemPedido, ItemPedidoDto dto) {
        ItemPedidoModel item = itemPedidoRepository.findById(cdItemPedido)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        ProdutoModel produto = produtoRepository.findById(dto.cdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        item.setQtdItem(dto.qtdItem());
        item.setPrecoUnitario(dto.precoUnitario());
        item.setCdProduto(produto);

        ItemPedidoModel atualizado = itemPedidoRepository.save(item);
        return converterParaDto(atualizado);
    }

    // Inativar (opcional, se quiser soft delete)
    public void inativarItemPedido(Integer cdItemPedido) {
        ItemPedidoModel item = itemPedidoRepository.findById(cdItemPedido)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        // Se quiser implementar soft delete, precisaria de um atributo flAtivo no ItemPedidoModel
        itemPedidoRepository.delete(item); // ou salvar com flAtivo=false
    }
}

