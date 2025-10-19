package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.ItemPedidoDto;
import com.ecommerce.equipe.model.ItemPedidoModel;
import com.ecommerce.equipe.service.ItemPedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/item-pedido")
@RequiredArgsConstructor
public class ItemPedidoController {

    private final ItemPedidoService itemPedidoService;

    @PostMapping("/pedido/{cdPedido}")
    public ResponseEntity<Object> salvar(@PathVariable Integer cdPedido, @RequestBody @Valid ItemPedidoDto itemPedidoDto) {
        try {
            ItemPedidoModel item = itemPedidoService.salvar(cdPedido, itemPedidoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(item);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/pedido/{cdPedido}")
    public ResponseEntity<List<ItemPedidoModel>> listarPorPedido(@PathVariable Integer cdPedido) {
        List<ItemPedidoModel> itens = itemPedidoService.listarPorPedido(cdPedido);
        return ResponseEntity.ok(itens);
    }

    @GetMapping("/{cdItemPedido}")
    public ResponseEntity<Object> buscar(@PathVariable Integer cdItemPedido) {
        try {
            ItemPedidoModel item = itemPedidoService.buscarPorId(cdItemPedido);
            return ResponseEntity.ok(item);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{cdItemPedido}")
    public ResponseEntity<Object> atualizar(@PathVariable Integer cdItemPedido, @RequestBody @Valid ItemPedidoDto itemPedidoDto) {
        try {
            ItemPedidoModel atualizado = itemPedidoService.atualizar(cdItemPedido, itemPedidoDto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{cdItemPedido}")
    public ResponseEntity<String> remover(@PathVariable Integer cdItemPedido) {
        try {
            itemPedidoService.remover(cdItemPedido);
            return ResponseEntity.ok("Item removido com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}