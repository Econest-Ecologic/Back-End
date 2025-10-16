package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.PedidoDto;
import com.ecommerce.equipe.model.PedidoModel;
import com.ecommerce.equipe.service.PedidoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/vi/pedido")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<PedidoModel> salvar(@RequestBody @Valid PedidoDto pedidoDto) {
        PedidoModel pedido = pedidoService.salvar(pedidoDto); // Criar metodo no service
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping
    public ResponseEntity<List<PedidoModel>> listar() {
        return ResponseEntity.ok(pedidoService.listar());
    }

    @GetMapping("/{cdPedido}")
    public ResponseEntity<Object> buscarPedido(@PathVariable("cdPedido") Integer cdPedido) {
        Optional<PedidoModel> pedido = pedidoService.buscarPorId(cdPedido);
        if(pedido.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(pedido.get());
    }

    @PutMapping("/{cdPedido}")
    public ResponseEntity<Object> atualizar(@PathVariable Integer cdPedido, @RequestBody @Valid PedidoDto pedidoDto) {
        try{
            PedidoModel atualizado = pedidoService.atualizar(cdPedido, pedidoDto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{cdPedido}")
    public ResponseEntity<String> cancelar(@PathVariable Integer cdPedido) {
        try{
            pedidoService.cancelarPedido(cdPedido);
            return ResponseEntity.ok("Pedido atualizado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
