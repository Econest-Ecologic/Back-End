package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.ItemPedidoDto;
import com.ecommerce.equipe.model.EstoqueModel;
import com.ecommerce.equipe.model.ItemPedidoModel;
import com.ecommerce.equipe.model.PedidoModel;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.repository.EstoqueRepository;
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
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;

    public ItemPedidoModel salvar(Integer cdPedido, ItemPedidoDto itemPedidoDto) {
        PedidoModel pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        ProdutoModel produto = produtoRepository.findById(itemPedidoDto.cdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        if (!produto.getFlAtivo()) {
            throw new RuntimeException("Produto inativo não pode ser adicionado ao pedido");
        }

        EstoqueModel estoque = estoqueRepository.findByCdProdutoCdProduto(itemPedidoDto.cdProduto())
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para este produto"));

        if (estoque.getQtdEstoque() <= 0) {
            throw new RuntimeException("Produto sem estoque disponível");
        }

        if (estoque.getQtdEstoque() < itemPedidoDto.qtdItem()) {
            throw new RuntimeException("Quantidade solicitada maior que o estoque disponível");
        }

        estoque.setQtdEstoque(estoque.getQtdEstoque() - itemPedidoDto.qtdItem());
        estoqueRepository.save(estoque);

        ItemPedidoModel model = converterParaModel(itemPedidoDto);
        model.setPedido(pedido);

        ItemPedidoModel salvo = itemPedidoRepository.save(model);

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

        Integer qtdAnterior = item.getQtdItem();
        Integer qtdNova = itemPedidoDto.qtdItem();
        Integer cdProduto = item.getCdProduto().getCdProduto();

        EstoqueModel estoque = estoqueRepository.findByCdProdutoCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para este produto"));

        Integer diferenca = qtdNova - qtdAnterior;

        if (diferenca > 0) {
            if (estoque.getQtdEstoque() < diferenca) {
                throw new RuntimeException("Estoque insuficiente");
            }
            estoque.setQtdEstoque(estoque.getQtdEstoque() - diferenca);
        } else if (diferenca < 0) {
            estoque.setQtdEstoque(estoque.getQtdEstoque() + Math.abs(diferenca));
        }

        estoqueRepository.save(estoque);

        item.setQtdItem(itemPedidoDto.qtdItem());
        item.setPrecoUnitario(itemPedidoDto.precoUnitario());

        ItemPedidoModel atualizado = itemPedidoRepository.save(item);

        calcularValorTotal(item.getPedido().getCdPedido());

        return atualizado;
    }

    public void remover(Integer cdItemPedido) {
        ItemPedidoModel item = itemPedidoRepository.findById(cdItemPedido)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        Integer cdProduto = item.getCdProduto().getCdProduto();
        EstoqueModel estoque = estoqueRepository.findByCdProdutoCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para este produto"));

        estoque.setQtdEstoque(estoque.getQtdEstoque() + item.getQtdItem());
        estoqueRepository.save(estoque);

        Integer cdPedido = item.getPedido().getCdPedido();
        itemPedidoRepository.delete(item);

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

    // REMOVIDO: converterParaDto duplicado que não era usado

    private ItemPedidoModel converterParaModel(ItemPedidoDto dto) {
        ItemPedidoModel model = new ItemPedidoModel();

        ProdutoModel produto = produtoRepository.findById(dto.cdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        model.setQtdItem(dto.qtdItem());
        model.setPrecoUnitario(dto.precoUnitario());
        model.setCdProduto(produto);

        return model;
    }
}