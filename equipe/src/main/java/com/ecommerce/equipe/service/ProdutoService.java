package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.ProdutoDto;
import com.ecommerce.equipe.model.EstoqueModel;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.repository.EstoqueRepository;
import com.ecommerce.equipe.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;

    public ProdutoDto criarProduto(ProdutoDto dto) {
        ProdutoModel model = converterParaModel(dto);
        ProdutoModel salvo = produtoRepository.save(model);

        EstoqueModel estoque = new EstoqueModel();
        estoque.setCdProduto(salvo);
        estoque.setQtdEstoque(dto.qtdEstoque());
        estoqueRepository.save(estoque);

        return converterParaDto(salvo);
    }

    public List<ProdutoDto> listarProdutos() {
        return produtoRepository.findAll()
                .stream()
                .map(this::converterParaDto)
                .toList();
    }

    public ProdutoDto buscarPorId(Integer id) {
        ProdutoModel produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));
        return converterParaDto(produto);
    }

    public List<ProdutoDto> buscarPorNome(String nome) {
        return produtoRepository.buscarPorNome(nome)
                .stream()
                .map(this::converterParaDto)
                .toList();
    }

    public List<ProdutoDto> buscarPorCategoria(String categoria) {
        return produtoRepository.buscarPorCategoria(categoria)
                .stream()
                .map(this::converterParaDto)
                .toList();
    }

    public ProdutoDto atualizarProduto(Integer id, ProdutoDto dto) {
        ProdutoModel produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));

        produto.setNmProduto(dto.nmProduto());
        produto.setDsProduto(dto.dsProduto());
        produto.setPreco(dto.preco());
        produto.setCategoria(dto.categoria());
        produto.setFlAtivo(dto.flAtivo());

        MultipartFile imagem = dto.imgProduto();
        if (imagem != null && !imagem.isEmpty()) {
            try {
                produto.setImgProduto(imagem.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Erro ao atualizar imagem do produto.", e);
            }
        }

        ProdutoModel atualizado = produtoRepository.save(produto);

        if (dto.qtdEstoque() != null) {
            EstoqueModel estoque = estoqueRepository.findByCdProdutoCdProduto(id)
                    .orElseThrow(() -> new RuntimeException("Estoque não encontrado para este produto"));
            estoque.setQtdEstoque(dto.qtdEstoque());
            estoqueRepository.save(estoque);
        }

        return converterParaDto(atualizado);
    }

    public void inativarProduto(Integer cd) {
        ProdutoModel produto = produtoRepository.findById(cd)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + cd));

        produto.setFlAtivo(false);
        produtoRepository.save(produto);
    }

    private ProdutoModel converterParaModel(ProdutoDto dto) {
        ProdutoModel produto = new ProdutoModel();

        produto.setNmProduto(dto.nmProduto());
        produto.setDsProduto(dto.dsProduto());
        produto.setPreco(dto.preco());
        produto.setCategoria(dto.categoria());
        produto.setFlAtivo(dto.flAtivo() != null ? dto.flAtivo() : true);

        MultipartFile imagem = dto.imgProduto();
        if (imagem != null && !imagem.isEmpty()) {
            try {
                produto.setImgProduto(imagem.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("Erro ao processar imagem do produto.", e);
            }
        }

        return produto;
    }

    private ProdutoDto converterParaDto(ProdutoModel model) {
        Integer qtdEstoque = estoqueRepository.findByCdProdutoCdProduto(model.getCdProduto())
                .map(EstoqueModel::getQtdEstoque)
                .orElse(0);

        return new ProdutoDto(
                model.getCdProduto(),
                model.getNmProduto(),
                model.getDsProduto(),
                model.getPreco(),
                model.getCategoria(),
                null,
                model.getFlAtivo(),
                qtdEstoque
        );
    }
}