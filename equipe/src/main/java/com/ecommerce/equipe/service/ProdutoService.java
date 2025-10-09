package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.ProdutoDto;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {
    private final ProdutoRepository produtoRepository;

    public ProdutoDto salvar(ProdutoDto dto){
        ProdutoModel produto = new ProdutoModel();
        produto.setNmProduto(dto.nmProduto());
        produto.setDsProduto(dto.dsProduto());
        produto.setPreco(dto.preco());
        produto.setCategoria(dto.categoria());
        produto.setImgProduto(dto.imgProduto() != null ?
                Base64.getDecoder().decode(dto.imgProduto()) : null);
        produto.setFlAtivo(true);

        ProdutoModel salvo = produtoRepository.save(produto);
        return toDto(salvo);
    }

    public List<ProdutoDto> listarTodos() {
        return produtoRepository.findAll().stream()
                .filter(ProdutoModel::getFlAtivo)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public ProdutoDto buscarPorId(Integer cdProduto){
        ProdutoModel produto = produtoRepository.findById(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));
        if(!produto.getFlAtivo()){
            throw new RuntimeException("Produto inativo");
        }
        return toDto(produto);
    }

    public ProdutoDto atualizar(Integer cdProduto, ProdutoDto dto){
        ProdutoModel produto = produtoRepository.findById(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setNmProduto(dto.nmProduto());
        produto.setDsProduto(dto.dsProduto());
        produto.setPreco(dto.preco());
        produto.setCategoria(dto.categoria());

        if(dto.imgProduto() != null && !dto.imgProduto().isBlank()){
            produto.setImgProduto(Base64.getDecoder().decode(dto.imgProduto()));
        }

        ProdutoModel atualizado = produtoRepository.save(produto);
        return toDto(atualizado);
    }

    public void deletar(Integer cdProduto){
        ProdutoModel produto = produtoRepository.findById(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setFlAtivo(false);
        produtoRepository.save(produto);
    }

    private ProdutoDto toDto(ProdutoModel produto){
        String imagemBase64 = produto.getImgProduto() != null
                ? Base64.getEncoder().encodeToString(produto.getImgProduto())
                : null;

        return new ProdutoDto(
                produto.getCdProduto(),
                produto.getNmProduto(),
                produto.getDsProduto(),
                produto.getPreco(),
                produto.getCategoria(),
                imagemBase64,
                produto.getFlAtivo()
        );
    }
}
