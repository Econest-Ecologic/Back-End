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

    }
}
