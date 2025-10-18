package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.EstoqueDto;
import com.ecommerce.equipe.service.EstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/estoque")
@RequiredArgsConstructor
public class EstoqueController {

    private final EstoqueService estoqueService;

    @PostMapping
    public ResponseEntity<EstoqueDto> salvar(@RequestBody @Valid EstoqueDto estoqueDto) {
        EstoqueDto estoque = estoqueService.salvar(estoqueDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(estoque);
    }

    @GetMapping
    public ResponseEntity<List<EstoqueDto>> listar() {
        return ResponseEntity.ok(estoqueService.listarTodos());
    }

    @GetMapping("/{cdEstoque}")
    public ResponseEntity<Object> buscar(@PathVariable Integer cdEstoque) {
        try {
            EstoqueDto estoque = estoqueService.buscarPorId(cdEstoque);
            return ResponseEntity.ok(estoque);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/produto/{cdProduto}")
    public ResponseEntity<Object> buscarPorProduto(@PathVariable Integer cdProduto) {
        try {
            EstoqueDto estoque = estoqueService.buscarPorProduto(cdProduto);
            return ResponseEntity.ok(estoque);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{cdEstoque}")
    public ResponseEntity<Object> atualizar(@PathVariable Integer cdEstoque, @RequestBody @Valid EstoqueDto estoqueDto) {
        try {
            EstoqueDto atualizado = estoqueService.atualizar(cdEstoque, estoqueDto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{cdEstoque}/adicionar")
    public ResponseEntity<Object> adicionarQuantidade(@PathVariable Integer cdEstoque, @RequestParam Integer quantidade) {
        try {
            EstoqueDto atualizado = estoqueService.adicionarQuantidade(cdEstoque, quantidade);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PatchMapping("/{cdEstoque}/remover")
    public ResponseEntity<Object> removerQuantidade(@PathVariable Integer cdEstoque, @RequestParam Integer quantidade) {
        try {
            EstoqueDto atualizado = estoqueService.removerQuantidade(cdEstoque, quantidade);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/verificar/{cdProduto}")
    public ResponseEntity<Object> verificarDisponibilidade(@PathVariable Integer cdProduto, @RequestParam Integer quantidade) {
        boolean disponivel = estoqueService.verificarDisponibilidade(cdProduto, quantidade);
        if (disponivel) {
            return ResponseEntity.ok("Produto disponível em estoque");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Quantidade insuficiente em estoque");
        }
    }
}