package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.ProdutoDto;
import com.ecommerce.equipe.model.EstoqueModel;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.repository.EstoqueRepository;
import com.ecommerce.equipe.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final EstoqueRepository estoqueRepository;

    @Transactional
    public ProdutoDto criarProduto(ProdutoDto dto) {
        ProdutoModel model = converterParaModel(dto);
        ProdutoModel salvo = produtoRepository.save(model);

        // ✅ SEMPRE criar estoque ao criar produto
        EstoqueModel estoque = new EstoqueModel();
        estoque.setCdProduto(salvo);
        estoque.setQtdEstoque(dto.qtdEstoque() != null ? dto.qtdEstoque() : 0);
        estoqueRepository.save(estoque);

        System.out.println("✅ Produto criado com estoque: " + salvo.getNmProduto() + " (Estoque: " + estoque.getQtdEstoque() + ")");

        return converterParaDto(salvo);
    }

    public List<ProdutoDto> listarProdutos() {
        return produtoRepository.findAll()
                .stream()
                .filter(produto -> produto.getFlAtivo() != null && produto.getFlAtivo())
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public ProdutoDto buscarPorId(Integer id) {
        ProdutoModel produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + id));

        if (produto.getFlAtivo() == null || !produto.getFlAtivo()) {
            throw new RuntimeException("Produto não está disponível");
        }

        return converterParaDto(produto);
    }

    public List<ProdutoDto> buscarPorNome(String nome) {
        return produtoRepository.buscarPorNome(nome)
                .stream()
                .filter(produto -> produto.getFlAtivo() != null && produto.getFlAtivo())
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public List<ProdutoDto> buscarPorCategoria(String categoria) {
        return produtoRepository.buscarPorCategoria(categoria)
                .stream()
                .filter(produto -> produto.getFlAtivo() != null && produto.getFlAtivo())
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    @Transactional
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

        // ✅ Atualizar ou criar estoque se necessário
        if (dto.qtdEstoque() != null) {
            Optional<EstoqueModel> estoqueOpt = estoqueRepository.findByCdProdutoCdProduto(id);

            if (estoqueOpt.isPresent()) {
                // Atualizar estoque existente
                EstoqueModel estoque = estoqueOpt.get();
                estoque.setQtdEstoque(dto.qtdEstoque());
                estoqueRepository.save(estoque);
                System.out.println("✅ Estoque atualizado: " + dto.qtdEstoque());
            } else {
                // Criar estoque se não existir
                EstoqueModel novoEstoque = new EstoqueModel();
                novoEstoque.setCdProduto(atualizado);
                novoEstoque.setQtdEstoque(dto.qtdEstoque());
                estoqueRepository.save(novoEstoque);
                System.out.println("✅ Estoque criado: " + dto.qtdEstoque());
            }
        }

        return converterParaDto(atualizado);
    }

    @Transactional
    public void inativarProduto(Integer cd) {
        ProdutoModel produto = produtoRepository.findById(cd)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado com o ID: " + cd));

        System.out.println("🗑️ Inativando produto: " + produto.getNmProduto());
        produto.setFlAtivo(false);
        produtoRepository.save(produto);
        System.out.println("✅ Produto inativado com sucesso!");
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
        // ✅ SEMPRE tentar buscar estoque, retornar 0 se não existir
        Integer qtdEstoque = estoqueRepository.findByCdProdutoCdProduto(model.getCdProduto())
                .map(EstoqueModel::getQtdEstoque)
                .orElseGet(() -> {
                    System.out.println("⚠️ Produto sem estoque cadastrado: " + model.getNmProduto() + " - Retornando 0");
                    return 0;
                });

        String imagemBase64 = null;
        if (model.getImgProduto() != null && model.getImgProduto().length > 0) {
            imagemBase64 = Base64.getEncoder().encodeToString(model.getImgProduto());
        }

        return new ProdutoDto(
                model.getCdProduto(),
                model.getNmProduto(),
                model.getDsProduto(),
                model.getPreco(),
                model.getCategoria(),
                null,
                imagemBase64,
                model.getFlAtivo(),
                qtdEstoque
        );
    }
}