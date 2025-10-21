package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.AvaliacaoDto;
import com.ecommerce.equipe.model.AvaliacaoModel;
import com.ecommerce.equipe.model.ProdutoModel;
import com.ecommerce.equipe.model.UsuarioModel;
import com.ecommerce.equipe.repository.AvaliacaoRepository;
import com.ecommerce.equipe.repository.ProdutoRepository;
import com.ecommerce.equipe.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AvaliacaoService {

    private final AvaliacaoRepository avaliacaoRepository;
    private final ProdutoRepository produtoRepository;
    private final UsuarioRepository usuarioRepository;

    public AvaliacaoDto salvar(AvaliacaoDto avaliacaoDto) {
        AvaliacaoModel model = converterParaModel(avaliacaoDto);
        AvaliacaoModel salvo = avaliacaoRepository.save(model);
        return converterParaDto(salvo);
    }

    public List<AvaliacaoDto> listarTodos() {
        return avaliacaoRepository.findAll().stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public AvaliacaoDto buscarPorId(Integer cdAvaliacao) {
        AvaliacaoModel avaliacao = avaliacaoRepository.findById(cdAvaliacao)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
        return converterParaDto(avaliacao);
    }

    public void remover(Integer cdAvaliacao) {
        AvaliacaoModel avaliacao = avaliacaoRepository.findById(cdAvaliacao)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));
        avaliacaoRepository.delete(avaliacao);
    }

    public List<AvaliacaoDto> listarPorProduto(Integer cdProduto) {
        return avaliacaoRepository.findByCdProdutoCdProduto(cdProduto).stream()
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public AvaliacaoDto atualizar(Integer cdAvaliacao, AvaliacaoDto dto) {
        AvaliacaoModel model = avaliacaoRepository.findById(cdAvaliacao)
                .orElseThrow(() -> new RuntimeException("Avaliação não encontrada"));

        model.setDtAvaliacao(dto.dtAvaliacao());
        model.setNuNota(dto.nuNota());
        model.setDsComentario(dto.dsComentario());
        AvaliacaoModel salvo = avaliacaoRepository.save(model);
        return converterParaDto(salvo);
    }

    private AvaliacaoModel converterParaModel(AvaliacaoDto avaliacaoDto) {
        AvaliacaoModel model = new AvaliacaoModel();
        UsuarioModel usuario = usuarioRepository.findById(avaliacaoDto.cdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        ProdutoModel produto = produtoRepository.findById(avaliacaoDto.cdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        model.setCdUsuario(usuario);
        model.setCdProduto(produto);
        model.setNuNota(avaliacaoDto.nuNota());
        model.setDsComentario(avaliacaoDto.dsComentario());
        model.setDtAvaliacao(avaliacaoDto.dtAvaliacao() != null ? avaliacaoDto.dtAvaliacao() : new Timestamp(System.currentTimeMillis()));
        return model;
    }

    private AvaliacaoDto converterParaDto(AvaliacaoModel model) {
        return new AvaliacaoDto(
                model.getCdAvaliacao(),
                model.getCdUsuario().getCdUsuario(),
                model.getCdProduto().getCdProduto(),
                model.getNuNota(),
                model.getDsComentario(),
                model.getDtAvaliacao()
        );
    }
}