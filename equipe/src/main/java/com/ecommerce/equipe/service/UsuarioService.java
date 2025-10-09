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
    //private final PasswordEncoder passwordEncoder;


    public UsuarioDto salvar(UsuarioDto dto) {
        UsuarioModel usuario = new UsuarioModel();
        usuario.setNmUsuario(dto.nmUsuario());
        usuario.setNmEmail(dto.nmEmail());
        usuario.setNmSenha(dto.nmSenha());
        usuario.setNuCpf(dto.nuCpf());
        usuario.setDsEndereco(dto.dsEndereco());
        usuario.setNuTelefone(dto.nuTelefone());

        Set<RoleModel> roles = new HashSet<>();
        if (dto.roles() != null && !dto.roles().isEmpty()) {
            roles.addAll(dto.roles());
        } else {
            RoleModel roleUser = roleRepository.findByNmRole("USER")
                    .orElseThrow(() -> new RuntimeException("Role USER não encontrada."));
            roles.add(roleUser);
        }
        usuario.setRoles(roles);

        UsuarioModel salvo = usuarioRepository.save(usuario);
        return toDto(salvo);
    }

    public List<UsuarioDto> listarTodos() {
        return usuarioRepository.findAll().stream()
                .filter(UsuarioModel::getFlAtivo)
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UsuarioDto buscarPorId(Integer cdUsuario) {
        UsuarioModel usuario = usuarioRepository.findById(cdUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        return toDto(usuario);
    }

    public UsuarioDto atualizar(Integer cdUsuario, UsuarioDto dto) {
        UsuarioModel usuario = usuarioRepository.findById(cdUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));

        usuario.setNmUsuario(dto.nmUsuario());
        usuario.setNmEmail(dto.nmEmail());
        if (dto.nmSenha() != null && !dto.nmSenha().isBlank()) {
            usuario.setNmSenha(dto.nmSenha());
        }
        usuario.setNuCpf(dto.nuCpf());
        usuario.setDsEndereco(dto.dsEndereco());
        usuario.setNuTelefone(dto.nuTelefone());

        Set<RoleModel> roles = new HashSet<>();
        if (dto.roles() != null && !dto.roles().isEmpty()) {
            roles.addAll(dto.roles());
        } else {
            RoleModel roleUser = roleRepository.findByNmRole("USER")
                    .orElseThrow(() -> new RuntimeException("Role USER não encontrada."));
            roles.add(roleUser);
        }
        usuario.setRoles(roles);

        UsuarioModel atualizado = usuarioRepository.save(usuario);
        return toDto(atualizado);
    }

    public void deletar(Integer cdUsuario) {
        UsuarioModel usuario = usuarioRepository.findById(cdUsuario)
                .orElseThrow(() -> new RuntimeException(("Usuario não encontrado")));

                usuario.setFlAtivo(false);
                usuarioRepository.save(usuario);
    }

    private UsuarioDto toDto(UsuarioModel usuario) {
        List<RoleModel> roles = usuario.getRoles().stream().toList();
        return new UsuarioDto(
                usuario.getCdUsuario(),
                usuario.getNmUsuario(),
                usuario.getNmEmail(),
                null, // senha não é retornada
                usuario.getNuCpf(),
                usuario.getDsEndereco(),
                usuario.getNuTelefone(),
                roles,
                usuario.getFlAtivo()
        );
    }
}
