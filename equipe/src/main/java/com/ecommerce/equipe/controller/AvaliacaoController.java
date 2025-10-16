package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.AvaliacaoDto;
import com.ecommerce.equipe.dto.PedidoDto;
import com.ecommerce.equipe.model.AvaliacaoModel;
import com.ecommerce.equipe.service.AvaliacaoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/avaliacao")
public class AvaliacaoController {

    @Autowired
    private AvaliacaoService avaliacaoService;

    @PostMapping
    public ResponseEntity<AvaliacaoDto> salvar(@RequestBody @Valid AvaliacaoDto avaliacaoDto) {
        AvaliacaoDto avaliacao = avaliacaoService.salvar(avaliacaoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(avaliacao);
    }

    @GetMapping
    public ResponseEntity<List<AvaliacaoDto>> listar() {
        return ResponseEntity.ok(avaliacaoService.listarTodos());
    }

    @GetMapping("/{cdAvaliacao")
    public ResponseEntity<Object> buscar(@PathVariable("cdAvaliacao")Integer cdAvaliacao) {
        try {
            AvaliacaoDto avaliacao = avaliacaoService.buscarPorId(cdAvaliacao);
            return ResponseEntity.ok(avaliacao);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/produto/{cdProduto")
    public ResponseEntity<List<AvaliacaoDto>> buscarPorProduto(@PathVariable("cdProduto")Integer cdProduto) {
        return ResponseEntity.ok(Collections.singletonList(avaliacaoService.buscarPorProduto(cdProduto)));
    }

    @DeleteMapping("/{cdAvaliacao}")
    public ResponseEntity<String> remover(@PathVariable("cdAvaliacao") Integer cdAvaliacao) {
        try{
            avaliacaoService.remover(cdAvaliacao);
            return ResponseEntity.ok("Removido com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
