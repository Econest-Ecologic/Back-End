package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.ProdutoDto;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;


@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/produto")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProdutoDto> salvar(@ModelAttribute @Valid ProdutoDto produtoDto) {
         ProdutoDto produto = produtoService.criarProduto(produtoDto);
         return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoDto>> listar() {
        return ResponseEntity.ok(produtoService.listarProdutos());
    }

    @GetMapping("/{cdProduto}")
    public ResponseEntity<Object> buscarProduto(@PathVariable("cdProduto") Integer cdProduto) {
        try {
            ProdutoDto produto = produtoService.buscarPorId(cdProduto);
            return ResponseEntity.ok(produto);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping(value = "/{cdProduto}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Object> atualizar(@PathVariable Integer cdProduto, @ModelAttribute @Valid ProdutoDto produtoDto) {
        try {
            ProdutoDto atualizado = produtoService.atualizarProduto(cdProduto, produtoDto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{cdProduto}")
    public ResponseEntity<String> inativar(@PathVariable Integer cdProduto) {
        try{
            produtoService.inativarProduto(cdProduto);
            return ResponseEntity.ok("Produto Inativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
