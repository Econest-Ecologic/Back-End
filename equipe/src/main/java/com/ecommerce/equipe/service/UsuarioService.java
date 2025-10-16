package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.UsuarioDto;
import com.ecommerce.equipe.model.RoleModel;
import com.ecommerce.equipe.model.UsuarioModel;
import com.ecommerce.equipe.repository.RoleRepository;
import com.ecommerce.equipe.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public UsuarioDto salvar(UsuarioDto dto) {
        UsuarioModel model = converterParaModel(dto);
        UsuarioModel salvo = usuarioRepository.save(model);
        return converterParaDto(salvo);
    }

    public List<UsuarioDto> listarTodos() {
        return usuarioRepository.findAll().stream()
                .filter(UsuarioModel::getFlAtivo)
                .map(this::converterParaDto)
                .collect(Collectors.toList());
    }

    public UsuarioDto buscarPorId(Integer cdUsuario) {
        UsuarioModel usuario = usuarioRepository.findById(cdUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        return converterParaDto(usuario);
    }

    public UsuarioDto atualizar(Integer cdUsuario, UsuarioDto dto) {
        UsuarioModel usuario = usuarioRepository.findById(cdUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setNmUsuario(dto.nmUsuario());
        usuario.setNmEmail(dto.nmEmail());

        if (dto.nmSenha() != null && !dto.nmSenha().isBlank()) {
            usuario.setNmSenha(dto.nmSenha());
        }

        usuario.setNuCpf(dto.nuCpf());
        usuario.setDsEndereco(dto.dsEndereco());
        usuario.setNuTelefone(dto.nuTelefone());

        usuario.setRoles(obterRoles(dto));
        usuario.setEstado(dto.estado());

        UsuarioModel atualizado = usuarioRepository.save(usuario);
        return converterParaDto(atualizado);
    }

    public void inativar(Integer cdUsuario) {
        UsuarioModel usuario = usuarioRepository.findById(cdUsuario)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        usuario.setFlAtivo(false);
        usuarioRepository.save(usuario);
    }


    private UsuarioModel converterParaModel(UsuarioDto dto) {
        UsuarioModel model = new UsuarioModel();

        model.setNmUsuario(dto.nmUsuario());
        model.setNmEmail(dto.nmEmail());
        model.setNmSenha(dto.nmSenha());
        model.setNuCpf(dto.nuCpf());
        model.setDsEndereco(dto.dsEndereco());
        model.setNuTelefone(dto.nuTelefone());
        model.setFlAtivo(dto.flAtivo() != null ? dto.flAtivo() : true);

        model.setRoles(obterRoles(dto));

        return model;
    }

    private UsuarioDto converterParaDto(UsuarioModel model) {
        List<RoleModel> roles = model.getRoles().stream().toList();

        return new UsuarioDto(
                model.getCdUsuario(),
                model.getNmUsuario(),
                model.getNmEmail(),
                null,
                model.getNuCpf(),
                model.getDsEndereco(),
                model.getNuTelefone(),
                roles,
                model.getFlAtivo(),
                model.getEstado()
        );
    }

    private Set<RoleModel> obterRoles(UsuarioDto dto) {
        Set<RoleModel> roles = new HashSet<>();

        if (dto.roles() != null && !dto.roles().isEmpty()) {
            roles.addAll(dto.roles());
        } else {
            RoleModel roleUser = roleRepository.findByNmRole("USER")
                    .orElseThrow(() -> new RuntimeException("Role USER não encontrada."));
            roles.add(roleUser);
        }

        return roles;
    }
}
