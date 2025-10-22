package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.PedidoDto;
import com.ecommerce.equipe.model.EstoqueModel;
import com.ecommerce.equipe.model.ItemPedidoModel;
import com.ecommerce.equipe.model.PedidoModel;
import com.ecommerce.equipe.model.StatusPedido;
import com.ecommerce.equipe.model.UsuarioModel;
import com.ecommerce.equipe.repository.EstoqueRepository;
import com.ecommerce.equipe.repository.ItemPedidoRepository;
import com.ecommerce.equipe.repository.PedidoRepository;
import com.ecommerce.equipe.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final PedidoRepository pedidoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final EstoqueRepository estoqueRepository;

    public PedidoModel salvar(Integer cdUsuario, PedidoDto pedidoDto) {
        UsuarioModel usuario = usuarioRepository.findById(cdUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        PedidoModel model = converterParaModel(pedidoDto);
        model.setUsuario(usuario);
        return pedidoRepository.save(model);
    }

    public List<PedidoModel> listar() {
        return pedidoRepository.findAll();
    }

    public Optional<PedidoModel> buscarPorId(Integer cdPedido) {
        return pedidoRepository.findById(cdPedido);
    }

    public List<PedidoModel> listarPorUsuario(Integer cdUsuario) {
        return pedidoRepository.findByUsuarioCdUsuario(cdUsuario);
    }

    public PedidoModel atualizar(Integer cdPedido, PedidoDto pedidoDto) {
        PedidoModel pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));
        pedido.setDtPedido(pedidoDto.dtPedido());
        pedido.setStatus(pedidoDto.status());
        pedido.setVlTotal(pedidoDto.vlTotal());
        pedido.setVlFrete(pedidoDto.vlFrete());
        return pedidoRepository.save(pedido);
    }

    public void cancelarPedido(Integer cdPedido) {
        PedidoModel pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() == StatusPedido.ENVIADO || pedido.getStatus() == StatusPedido.ENTREGUE) {
            throw new IllegalStateException("Não é possível cancelar um pedido já enviado ou entregue.");
        }

        List<ItemPedidoModel> itens = itemPedidoRepository.findByPedidoCdPedido(cdPedido);
        for (ItemPedidoModel item : itens) {
            Integer cdProduto = item.getCdProduto().getCdProduto();
            EstoqueModel estoque = estoqueRepository.findByCdProdutoCdProduto(cdProduto)
                    .orElseThrow(() -> new RuntimeException("Estoque não encontrado para o produto"));

            estoque.setQtdEstoque(estoque.getQtdEstoque() + item.getQtdItem());
            estoqueRepository.save(estoque);
        }

        pedido.setStatus(StatusPedido.CANCELADO);
        pedidoRepository.save(pedido);
    }

    private PedidoModel converterParaModel(PedidoDto pedidoDto) {
        PedidoModel model = new PedidoModel();
        model.setDtPedido(pedidoDto.dtPedido());
        model.setStatus(pedidoDto.status() != null ? pedidoDto.status() : StatusPedido.ABERTO);
        model.setVlTotal(pedidoDto.vlTotal());
        model.setVlFrete(pedidoDto.vlFrete());
        return model;
    }
}