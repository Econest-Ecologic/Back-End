package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.LoginRequestDto;
import com.ecommerce.equipe.dto.LoginResponseDto;
import com.ecommerce.equipe.dto.RegisterRequestDto;
import com.ecommerce.equipe.model.RoleModel;
import com.ecommerce.equipe.model.UsuarioModel;
import com.ecommerce.equipe.repository.RoleRepository;
import com.ecommerce.equipe.repository.UsuarioRepository;
import com.ecommerce.equipe.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public LoginResponseDto login(LoginRequestDto dto) {
        // Autentica o usuário com Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.nmEmail(), dto.nmSenha())
        );

        // Busca o usuário no banco
        UsuarioModel usuario = usuarioRepository.findByNmEmail(dto.nmEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verifica se o usuário está ativo
        if (!usuario.getFlAtivo()) {
            throw new RuntimeException("Usuário inativo");
        }

        // Gera o token JWT
        String token = jwtUtil.generateToken(usuario.getNmEmail());

        // Extrai os nomes das roles
        List<String> roles = usuario.getRoles().stream()
                .map(RoleModel::getNmRole)
                .collect(Collectors.toList());

        return new LoginResponseDto(
                token,
                usuario.getCdUsuario(),
                usuario.getNmUsuario(),
                usuario.getNmEmail(),
                roles,
                usuario.getEstado()
        );
    }

    public LoginResponseDto register(RegisterRequestDto dto) {
        // Verifica se o email já existe
        Optional<UsuarioModel> usuarioExiste = usuarioRepository.findByNmEmail(dto.nmEmail());
        if (usuarioExiste.isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        // Cria o novo usuário
        UsuarioModel novoUsuario = new UsuarioModel();
        novoUsuario.setNmUsuario(dto.nmUsuario());
        novoUsuario.setNmEmail(dto.nmEmail());
        novoUsuario.setNmSenha(passwordEncoder.encode(dto.nmSenha())); // Criptografa a senha
        novoUsuario.setNuCpf(dto.nuCpf());
        novoUsuario.setDsEndereco(dto.dsEndereco());
        novoUsuario.setNuTelefone(dto.nuTelefone());
        novoUsuario.setEstado(dto.estado());
        novoUsuario.setFlAtivo(true);

        // Define a role USER por padrão
        RoleModel roleUser = roleRepository.findByNmRole("USER")
                .orElseThrow(() -> new RuntimeException("Role USER não encontrada"));

        Set<RoleModel> roles = new HashSet<>();
        roles.add(roleUser);
        novoUsuario.setRoles(roles);

        // Salva o usuário
        UsuarioModel salvo = usuarioRepository.save(novoUsuario);

        // Gera o token JWT
        String token = jwtUtil.generateToken(salvo.getNmEmail());

        // Extrai os nomes das roles
        List<String> rolesNomes = salvo.getRoles().stream()
                .map(RoleModel::getNmRole)
                .collect(Collectors.toList());

        return new LoginResponseDto(
                token,
                salvo.getCdUsuario(),
                salvo.getNmUsuario(),
                salvo.getNmEmail(),
                rolesNomes,
                salvo.getEstado()
        );
    }
}