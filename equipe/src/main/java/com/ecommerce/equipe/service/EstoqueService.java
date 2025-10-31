package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.EstoqueDto;
import com.ecommerce.equipe.model.EstoqueModel;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.repository.EstoqueRepository;
import com.ecommerce.equipe.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public EstoqueDto adicionarQuantidade(Integer cdEstoque, Integer quantidade) {
        EstoqueModel estoque = estoqueRepository.findById(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        System.out.println("➕ Adicionando " + quantidade + " ao estoque atual: " + estoque.getQtdEstoque());

        estoque.setQtdEstoque(estoque.getQtdEstoque() + quantidade);
        EstoqueModel atualizado = estoqueRepository.save(estoque);

        System.out.println("Novo estoque: " + atualizado.getQtdEstoque());

        return converterParaDto(atualizado);
    }

    @Transactional
    public EstoqueDto removerQuantidade(Integer cdEstoque, Integer quantidade) {
        EstoqueModel estoque = estoqueRepository.findById(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        System.out.println("Removendo " + quantidade + " do estoque atual: " + estoque.getQtdEstoque());

        if (estoque.getQtdEstoque() < quantidade) {
            throw new RuntimeException("Quantidade insuficiente em estoque. Disponível: " + estoque.getQtdEstoque());
        }

        estoque.setQtdEstoque(estoque.getQtdEstoque() - quantidade);
        EstoqueModel atualizado = estoqueRepository.save(estoque);

        System.out.println("Novo estoque: " + atualizado.getQtdEstoque());

        return converterParaDto(atualizado);
    }

    @Transactional
    public EstoqueDto adicionarQuantidade(Integer cdProduto, Integer quantidade, boolean isProdutoId) {
        EstoqueModel estoque = estoqueRepository.findByCdProdutoCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para este produto"));

        System.out.println("➕ [POR PRODUTO] Adicionando " + quantidade + " ao estoque atual: " + estoque.getQtdEstoque());

        estoque.setQtdEstoque(estoque.getQtdEstoque() + quantidade);
        EstoqueModel atualizado = estoqueRepository.save(estoque);

        System.out.println("[POR PRODUTO] Novo estoque: " + atualizado.getQtdEstoque());

        return converterParaDto(atualizado);
    }

    @Transactional
    public EstoqueDto removerQuantidade(Integer cdProduto, Integer quantidade, boolean isProdutoId) {
        EstoqueModel estoque = estoqueRepository.findByCdProdutoCdProduto(cdProduto)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado para este produto"));

        System.out.println("[POR PRODUTO] Removendo " + quantidade + " do estoque atual: " + estoque.getQtdEstoque());

        if (estoque.getQtdEstoque() < quantidade) {
            throw new RuntimeException(
                    "Quantidade insuficiente em estoque. Disponível: " + estoque.getQtdEstoque() +
                            ", Solicitado: " + quantidade
            );
        }

        estoque.setQtdEstoque(estoque.getQtdEstoque() - quantidade);
        EstoqueModel atualizado = estoqueRepository.save(estoque);

        System.out.println("[POR PRODUTO] Novo estoque: " + atualizado.getQtdEstoque());

        return converterParaDto(atualizado);
    }

    public boolean verificarDisponibilidade(Integer cdProduto, Integer quantidade) {
        try {
            EstoqueDto estoque = buscarPorProduto(cdProduto);
            boolean disponivel = estoque.qtdEstoque() >= quantidade && estoque.qtdEstoque() > 0;

            System.out.println("🔍 Verificando disponibilidade - Produto: " + cdProduto +
                    ", Solicitado: " + quantidade +
                    ", Disponível: " + estoque.qtdEstoque() +
                    ", Resultado: " + (disponivel ? "OK" : "INSUFICIENTE"));

            return disponivel;
        } catch (RuntimeException e) {
            System.err.println("Erro ao verificar disponibilidade: " + e.getMessage());
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