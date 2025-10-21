package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.PedidoDto;
import com.ecommerce.equipe.model.PedidoModel;
import com.ecommerce.equipe.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;

    @PostMapping("/usuario/{cdUsuario}")
    public ResponseEntity<PedidoModel> salvar(
            @PathVariable Integer cdUsuario,
            @RequestBody @Valid PedidoDto pedidoDto) {
        PedidoModel pedido = pedidoService.salvar(cdUsuario, pedidoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping
    public ResponseEntity<List<PedidoModel>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    @GetMapping("/{cdPedido}")
    public ResponseEntity<Object> buscarPedido(@PathVariable("cdPedido") Integer cdPedido) {
        Optional<PedidoModel> pedido = pedidoService.buscarPorId(cdPedido);
        if (pedido.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(pedido.get());
    }

    @GetMapping("/usuario/{cdUsuario}")
    public ResponseEntity<List<PedidoModel>> listarPorUsuario(@PathVariable Integer cdUsuario) {
        List<PedidoModel> pedidos = pedidoService.listarPorUsuario(cdUsuario);
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/{cdPedido}")
    public ResponseEntity<Object> atualizar(@PathVariable Integer cdPedido, @RequestBody @Valid PedidoDto pedidoDto) {
        try {
            PedidoModel atualizado = pedidoService.atualizar(cdPedido, pedidoDto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{cdPedido}")
    public ResponseEntity<String> cancelar(@PathVariable Integer cdPedido) {
        try {
            pedidoService.cancelarPedido(cdPedido);
            return ResponseEntity.ok("Pedido cancelado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}