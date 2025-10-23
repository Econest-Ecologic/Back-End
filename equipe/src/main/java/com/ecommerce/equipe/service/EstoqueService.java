package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.EstoqueDto;
import com.ecommerce.equipe.model.EstoqueModel;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.repository.EstoqueRepository;
import com.ecommerce.equipe.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EstoqueService {

    private final EstoqueRepository estoqueRepository;
    private final ProdutoRepository produtoRepository;

    public EstoqueDto salvar(EstoqueDto dto) {
        EstoqueModel model = converterParaModel(dto);
        EstoqueModel salvo = estoqueRepository.save(model);
        return converterParaDto(salvo);
    }

    public List<EstoqueDto> listarTodos() {
        return estoqueRepository.findAll().stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public EstoqueDto buscarPorId(Integer cdEstoque) {
        EstoqueModel estoque = estoqueRepository.findById(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));
        return converterParaDto(estoque);
    }

    public EstoqueDto buscarPorProduto(Integer cdProduto) {
        EstoqueModel estoque = estoqueRepository.findByCdProdutoCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para este produto"));
        return converterParaDto(estoque);
    }

    public EstoqueDto atualizar(Integer cdEstoque, EstoqueDto dto) {
        EstoqueModel estoque = estoqueRepository.findById(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        estoque.setQtdEstoque(dto.qtdEstoque());

        EstoqueModel atualizado = estoqueRepository.save(estoque);
        return converterParaDto(atualizado);
    }

    public EstoqueDto adicionarQuantidade(Integer cdEstoque, Integer quantidade) {
        EstoqueModel estoque = estoqueRepository.findById(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        estoque.setQtdEstoque(estoque.getQtdEstoque() + quantidade);
        EstoqueModel atualizado = estoqueRepository.save(estoque);
        return converterParaDto(atualizado);
    }

    public EstoqueDto removerQuantidade(Integer cdEstoque, Integer quantidade) {
        EstoqueModel estoque = estoqueRepository.findById(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        if (estoque.getQtdEstoque() < quantidade) {
            throw new RuntimeException("Quantidade insuficiente em estoque");
        }

        estoque.setQtdEstoque(estoque.getQtdEstoque() - quantidade);
        EstoqueModel atualizado = estoqueRepository.save(estoque);
        return converterParaDto(atualizado);
    }

    public boolean verificarDisponibilidade(Integer cdProduto, Integer quantidade) {
        try {
            EstoqueDto estoque = buscarPorProduto(cdProduto);
            return estoque.qtdEstoque() >= quantidade && estoque.qtdEstoque() > 0;
        } catch (RuntimeException e) {
            return false;
        }
    }

    private EstoqueModel converterParaModel(EstoqueDto dto) {
        EstoqueModel model = new EstoqueModel();

        ProdutoModel produto = produtoRepository.findById(dto.cdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        model.setQtdEstoque(dto.qtdEstoque());
        model.setCdProduto(produto);

        return model;
    }

    private EstoqueDto converterParaDto(EstoqueModel model) {
        return new EstoqueDto(
                model.getCdEstoque(),
                model.getQtdEstoque(),
                model.getCdProduto().getCdProduto()
        );
    }
}