package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.ProdutoDto;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.service.ProdutoService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @PostMapping
    public ResponseEntity<ProdutoModel> salvar(@RequestBody @Valid ProdutoDto produtoDto) {
         ProdutoModel produto = produtoService.salvar(produtoDto);
         return ResponseEntity.status(HttpStatus.CREATED).body(produto);
    }

    @GetMapping
    public ResponseEntity<List<ProdutoModel>> listar() {
        return ResponseEntity.ok(produtoService.listar());
    }

    @GetMapping("/{cdProduto}")
    public ResponseEntity<ProdutoModel> buscarProduto(@PathVariable("cdProduto") Integer cdProduto) {
        Optional<ProdutoModel> produto = produtoService.buscarProduto()

    }



}
