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
import java.util.Optional;
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

    // ✅ CORRIGIDO: Criar estoque automaticamente se não existir
    @Transactional
    public EstoqueDto buscarPorProduto(Integer cdProduto) {
        System.out.println("🔍 Buscando estoque do produto: " + cdProduto);

        // Verificar se o produto existe
        ProdutoModel produto = produtoRepository.findById(cdProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        // Buscar ou criar estoque
        Optional<EstoqueModel> estoqueOpt = estoqueRepository.findByCdProdutoCdProduto(cdProduto);

        if (estoqueOpt.isPresent()) {
            System.out.println("✅ Estoque encontrado: " + estoqueOpt.get().getQtdEstoque());
            return converterParaDto(estoqueOpt.get());
        } else {
            // ✅ Criar estoque zerado se não existir
            System.out.println("⚠️ Estoque não encontrado! Criando estoque zerado para produto: " + produto.getNmProduto());

            EstoqueModel novoEstoque = new EstoqueModel();
            novoEstoque.setCdProduto(produto);
            novoEstoque.setQtdEstoque(0);

            EstoqueModel salvo = estoqueRepository.save(novoEstoque);
            System.out.println("✅ Estoque criado com sucesso: " + salvo.getCdEstoque());

            return converterParaDto(salvo);
        }
    }

    @Transactional
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

        System.out.println("✅ Novo estoque: " + atualizado.getQtdEstoque());

        return converterParaDto(atualizado);
    }

    @Transactional
    public EstoqueDto removerQuantidade(Integer cdEstoque, Integer quantidade) {
        EstoqueModel estoque = estoqueRepository.findById(cdEstoque)
                .orElseThrow(() -> new RuntimeException("Estoque não encontrado"));

        System.out.println("➖ Removendo " + quantidade + " do estoque atual: " + estoque.getQtdEstoque());

        if (estoque.getQtdEstoque() < quantidade) {
            throw new RuntimeException("Quantidade insuficiente em estoque. Disponível: " + estoque.getQtdEstoque());
        }

        estoque.setQtdEstoque(estoque.getQtdEstoque() - quantidade);
        EstoqueModel atualizado = estoqueRepository.save(estoque);

        System.out.println("✅ Novo estoque: " + atualizado.getQtdEstoque());

        return converterParaDto(atualizado);
    }

    // ✅ CORRIGIDO: Métodos por cdProduto com criação automática
    @Transactional
    public EstoqueDto adicionarQuantidade(Integer cdProduto, Integer quantidade, boolean isProdutoId) {
        System.out.println("➕ [POR PRODUTO] Adicionando " + quantidade + " ao produto: " + cdProduto);

        // Buscar ou criar estoque
        EstoqueDto estoqueDto = buscarPorProduto(cdProduto);

        EstoqueModel estoque = estoqueRepository.findById(estoqueDto.cdEstoque())
                .orElseThrow(() -> new RuntimeException("Erro ao buscar estoque"));

        estoque.setQtdEstoque(estoque.getQtdEstoque() + quantidade);
        EstoqueModel atualizado = estoqueRepository.save(estoque);

        System.out.println("✅ [POR PRODUTO] Novo estoque: " + atualizado.getQtdEstoque());

        return converterParaDto(atualizado);
    }

    @Transactional
    public EstoqueDto removerQuantidade(Integer cdProduto, Integer quantidade, boolean isProdutoId) {
        System.out.println("➖ [POR PRODUTO] Removendo " + quantidade + " do produto: " + cdProduto);

        // Buscar ou criar estoque
        EstoqueDto estoqueDto = buscarPorProduto(cdProduto);

        EstoqueModel estoque = estoqueRepository.findById(estoqueDto.cdEstoque())
                .orElseThrow(() -> new RuntimeException("Erro ao buscar estoque"));

        if (estoque.getQtdEstoque() < quantidade) {
            throw new RuntimeException(
                    "Quantidade insuficiente em estoque. Disponível: " + estoque.getQtdEstoque() +
                            ", Solicitado: " + quantidade
            );
        }

        estoque.setQtdEstoque(estoque.getQtdEstoque() - quantidade);
        EstoqueModel atualizado = estoqueRepository.save(estoque);

        System.out.println("✅ [POR PRODUTO] Novo estoque: " + atualizado.getQtdEstoque());

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
            System.err.println("❌ Erro ao verificar disponibilidade: " + e.getMessage());
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