package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.PagamentoDto;
import com.ecommerce.equipe.model.PagamentoModel;
import com.ecommerce.equipe.model.PedidoModel;
import com.ecommerce.equipe.model.StatusPedido;
import com.ecommerce.equipe.repository.PagamentoRepository;
import com.ecommerce.equipe.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PagamentoService {

    private final PagamentoRepository pagamentoRepository;
    private final PedidoRepository pedidoRepository;

    public PagamentoDto salvar(Integer cdPedido, PagamentoDto dto) {
        PedidoModel pedido = pedidoRepository.findById(cdPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        if (pedido.getStatus() == StatusPedido.PAGO) {
            throw new RuntimeException("Este pedido já foi pago");
        }

        if (pedido.getStatus() == StatusPedido.CANCELADO) {
            throw new RuntimeException("Não é possível pagar um pedido cancelado");
        }

        PagamentoModel model = converterParaModel(dto);
        PagamentoModel salvo = pagamentoRepository.save(model);

        pedido.setStatus(StatusPedido.PAGO);
        pedidoRepository.save(pedido);

        return converterParaDto(salvo);
    }

    public List<PagamentoDto> listarTodos() {
        return pagamentoRepository.findAll().stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public PagamentoDto buscarPorId(Integer cdPagamento) {
        PagamentoModel pagamento = pagamentoRepository.findById(cdPagamento)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));
        return converterParaDto(pagamento);
    }

    private PagamentoModel converterParaModel(PagamentoDto dto) {
        PagamentoModel model = new PagamentoModel();
        model.setMetodo(dto.metodo());
        model.setNuValor(dto.nuValor());
        model.setDtPagamento(dto.dtPagamento() != null ? dto.dtPagamento() : new Date());
        return model;
    }

    private PagamentoDto converterParaDto(PagamentoModel model) {
        return new PagamentoDto(
                model.getCdPagamento(),
                model.getMetodo(),
                model.getNuValor(),
                model.getDtPagamento()
        );
    }
}