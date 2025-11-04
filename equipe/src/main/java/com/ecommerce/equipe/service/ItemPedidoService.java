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
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemPedidoService {

    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoRepository pedidoRepository;
    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;


    @Transactional
    public ItemPedidoModel salvar(Integer cdPedido, ItemPedidoDto itemPedidoDto) {
        System.out.println("Adicionando item ao pedido...");


        PedidoModel pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));


        ProdutoModel produto = produtoRepository.findById(itemPedidoDto.cdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));


        if (produto.getFlAtivo() == null || !produto.getFlAtivo()) {
            throw new RuntimeException("Produto não está disponível para venda");
        }


        EstoqueModel estoque = estoqueRepository.findByCdProdutoCdProduto(itemPedidoDto.cdProduto())
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para este produto"));

        System.out.println("Estoque atual: " + estoque.getQtdEstoque());
        System.out.println("Quantidade solicitada: " + itemPedidoDto.qtdItem());


        if (estoque.getQtdEstoque() <= 0) {
            throw new RuntimeException("Produto sem estoque disponível");
        }

        if (estoque.getQtdEstoque() < itemPedidoDto.qtdItem()) {
            throw new RuntimeException(
                    "Quantidade solicitada (" + itemPedidoDto.qtdItem() +
                            ") maior que o estoque disponível (" + estoque.getQtdEstoque() + ")"
            );
        }

        int novoEstoque = estoque.getQtdEstoque() - itemPedidoDto.qtdItem();
        estoque.setQtdEstoque(novoEstoque);
        estoqueRepository.save(estoque);

        System.out.println("Estoque atualizado: " + novoEstoque);

        ItemPedidoModel model = converterParaModel(itemPedidoDto);
        model.setPedido(pedido);
        model.setCdProduto(produto);

        ItemPedidoModel salvo = itemPedidoRepository.save(model);

        calcularValorTotal(cdPedido);

        System.out.println("Item adicionado ao pedido com sucesso!");
        return salvo;
    }

    public List<ItemPedidoModel> listarPorPedido(Integer cdPedido) {
        return itemPedidoRepository.findByPedidoCdPedido(cdPedido);
    }

    public ItemPedidoModel buscarPorId(Integer cdItemPedido) {
        return itemPedidoRepository.findById(cdItemPedido)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));
    }


    @Transactional
    public ItemPedidoModel atualizar(Integer cdItemPedido, ItemPedidoDto itemPedidoDto) {
        System.out.println("Atualizando item do pedido...");

        ItemPedidoModel item = itemPedidoRepository.findById(cdItemPedido)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        Integer qtdAnterior = item.getQtdItem();
        Integer qtdNova = itemPedidoDto.qtdItem();
        Integer cdProduto = item.getCdProduto().getCdProduto();

        System.out.println("Quantidade anterior: " + qtdAnterior);
        System.out.println("Quantidade nova: " + qtdNova);


        EstoqueModel estoque = estoqueRepository.findByCdProdutoCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para este produto"));


        Integer diferenca = qtdNova - qtdAnterior;
        System.out.println("Diferença: " + diferenca);

        if (diferenca > 0) {
            if (estoque.getQtdEstoque() < diferenca) {
                throw new RuntimeException(
                        "Estoque insuficiente. Disponível: " + estoque.getQtdEstoque()
                );
            }
            estoque.setQtdEstoque(estoque.getQtdEstoque() - diferenca);
            System.out.println("Diminuindo estoque em " + diferenca);
        } else if (diferenca < 0) {
            estoque.setQtdEstoque(estoque.getQtdEstoque() + Math.abs(diferenca));
            System.out.println("Aumentando estoque em " + Math.abs(diferenca));
        }

        estoqueRepository.save(estoque);
        System.out.println("Novo estoque: " + estoque.getQtdEstoque());

        item.setQtdItem(itemPedidoDto.qtdItem());
        item.setPrecoUnitario(itemPedidoDto.precoUnitario());

        ItemPedidoModel atualizado = itemPedidoRepository.save(item);

        calcularValorTotal(item.getPedido().getCdPedido());

        return atualizado;
    }


    @Transactional
    public void remover(Integer cdItemPedido) {
        System.out.println("Removendo item do pedido...");

        ItemPedidoModel item = itemPedidoRepository.findById(cdItemPedido)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        Integer cdProduto = item.getCdProduto().getCdProduto();
        Integer quantidade = item.getQtdItem();


        EstoqueModel estoque = estoqueRepository.findByCdProdutoCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para este produto"));

        System.out.println("Devolvendo " + quantidade + " unidades ao estoque");
        estoque.setQtdEstoque(estoque.getQtdEstoque() + quantidade);
        estoqueRepository.save(estoque);

        System.out.println("Novo estoque: " + estoque.getQtdEstoque());

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

        System.out.println("Valor total atualizado: R$ " + pedido.getVlTotal());
    }

    private ItemPedidoModel converterParaModel(ItemPedidoDto dto) {
        ItemPedidoModel model = new ItemPedidoModel();
        model.setQtdItem(dto.qtdItem());
        model.setPrecoUnitario(dto.precoUnitario());
        return model;
    }
}