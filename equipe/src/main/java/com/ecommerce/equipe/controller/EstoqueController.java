package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.EstoqueDto;
import com.ecommerce.equipe.service.EstoqueService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

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

    // ✅ CORRIGIDO: Melhor tratamento de erro 404
    @GetMapping("/produto/{cdProduto}")
    public ResponseEntity<Object> buscarPorProduto(@PathVariable Integer cdProduto) {
        try {
            EstoqueDto estoque = estoqueService.buscarPorProduto(cdProduto);
            return ResponseEntity.ok(estoque);
        } catch (RuntimeException e) {
            System.err.println("❌ Erro ao buscar estoque do produto " + cdProduto + ": " + e.getMessage());

            // ✅ Retornar 404 com mensagem clara
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "error", "Estoque não encontrado",
                            "message", "Não há estoque cadastrado para este produto",
                            "cdProduto", cdProduto
                    ));
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

    // ✅ CORRIGIDO: Melhor validação antes de reservar
    @PostMapping("/reservar")
    public ResponseEntity<Object> reservarEstoque(@RequestBody Map<String, Integer> request) {
        try {
            Integer cdProduto = request.get("cdProduto");
            Integer quantidade = request.get("quantidade");

            if (cdProduto == null || quantidade == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("cdProduto e quantidade são obrigatórios");
            }

            if (quantidade <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Quantidade deve ser maior que zero");
            }

            System.out.println("🔒 Reservando estoque - Produto: " + cdProduto + ", Quantidade: " + quantidade);

            // ✅ Usar o método correto que aceita cdProduto
            EstoqueDto atualizado = estoqueService.removerQuantidade(cdProduto, quantidade, true);

            System.out.println("✅ Estoque reservado. Novo estoque: " + atualizado.qtdEstoque());

            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            System.err.println("❌ Erro ao reservar estoque: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // ✅ CORRIGIDO: Melhor validação antes de liberar
    @PostMapping("/liberar")
    public ResponseEntity<Object> liberarEstoque(@RequestBody Map<String, Integer> request) {
        try {
            Integer cdProduto = request.get("cdProduto");
            Integer quantidade = request.get("quantidade");

            if (cdProduto == null || quantidade == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("cdProduto e quantidade são obrigatórios");
            }

            if (quantidade <= 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Quantidade deve ser maior que zero");
            }

            System.out.println("🔓 Liberando estoque - Produto: " + cdProduto + ", Quantidade: " + quantidade);

            // ✅ Usar o método correto que aceita cdProduto
            EstoqueDto atualizado = estoqueService.adicionarQuantidade(cdProduto, quantidade, true);

            System.out.println("✅ Estoque liberado. Novo estoque: " + atualizado.qtdEstoque());

            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            System.err.println("❌ Erro ao liberar estoque: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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