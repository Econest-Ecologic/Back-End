package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.PagamentoDto;
import com.ecommerce.equipe.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/pagamento")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping("/pedido/{cdPedido}")
    public ResponseEntity<Object> realizarPagamento(@PathVariable Integer cdPedido, @RequestBody @Valid PagamentoDto pagamentoDto) {
        try {
            PagamentoDto pagamento = pagamentoService.salvar(cdPedido, pagamentoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(pagamento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<List<PagamentoDto>> listar() {
        return ResponseEntity.ok(pagamentoService.listarTodos());
    }

    @GetMapping("/{cdPagamento}")
    public ResponseEntity<Object> buscar(@PathVariable Integer cdPagamento) {
        try {
            PagamentoDto pagamento = pagamentoService.buscarPorId(cdPagamento);
            return ResponseEntity.ok(pagamento);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}