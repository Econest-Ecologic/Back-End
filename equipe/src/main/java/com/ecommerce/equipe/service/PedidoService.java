package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.ItemPedidoDto;
import com.ecommerce.equipe.dto.PedidoDto;
import com.ecommerce.equipe.model.ItemPedidoModel;
import com.ecommerce.equipe.model.PedidoModel;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.model.StatusPedido;
import com.ecommerce.equipe.repository.ItemPedidoRepository;
import com.ecommerce.equipe.repository.PedidoRepository;
import com.ecommerce.equipe.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.hibernate.cache.spi.support.AbstractReadWriteAccess;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PedidoService {
    private final  PedidoRepository pedidoRepository;

    public PedidoModel salvar(PedidoDto pedidoDto) {
        PedidoModel model = converterParaModel(pedidoDto);
        return pedidoRepository.save(model);
    }

    public List<PedidoModel> listar() {
        return pedidoRepository.findAll();
    }

    public Optional<PedidoModel> buscarPorCd(Integer cdPedido) {
        return  pedidoRepository.findById(cdPedido);
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

    private PedidoDto converterParaDto(PedidoModel pedidoModel) {
        return new PedidoDto(
                pedidoModel.getCdPedido(),
                pedidoModel.getDtPedido(),
                pedidoModel.getStatus(),
                pedidoModel.getVlTotal(),
                pedidoModel.getVlFrete()
        );
    }
}
