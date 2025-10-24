package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.ProdutoDto;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.repository.ProdutoRepository;
import com.ecommerce.equipe.service.ProdutoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("api/v1/produto")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;
    private final ProdutoRepository produtoRepository;

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

    @GetMapping("/buscar")
    public ResponseEntity<List<ProdutoDto>> buscarPorNome(@RequestParam String nome) {
        List<ProdutoDto> produtos = produtoService.buscarPorNome(nome);
        return ResponseEntity.ok(produtos);
    }

    @GetMapping("/categoria/{categoria}")
    public ResponseEntity<List<ProdutoDto>> buscarPorCategoria(@PathVariable String categoria) {
        List<ProdutoDto> produtos = produtoService.buscarPorCategoria(categoria);
        return ResponseEntity.ok(produtos);
    }


    @DeleteMapping("/{cdProduto}")
    public ResponseEntity<String> inativar(@PathVariable Integer cdProduto) {
        try {
            produtoService.inativarProduto(cdProduto);
            return ResponseEntity.ok("Produto Inativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/{cdProduto}/imagem")
    public ResponseEntity<byte[]> buscarImagem(@PathVariable Integer cdProduto) {
        try {
            ProdutoModel produto = produtoRepository.findById(cdProduto)
                    .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

            byte[] imagem = produto.getImgProduto();

            if (imagem == null || imagem.length == 0) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(imagem);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}