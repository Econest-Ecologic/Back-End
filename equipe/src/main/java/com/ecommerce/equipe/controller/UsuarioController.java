package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.UsuarioDto;
import com.ecommerce.equipe.model.UsuarioModel;
import com.ecommerce.equipe.repository.UsuarioRepository;
import com.ecommerce.equipe.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<UsuarioDto> salvar(@RequestBody @Valid UsuarioDto usuarioDto) {
        UsuarioDto usuario = usuarioService.salvar(usuarioDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
    }

    @GetMapping
    public ResponseEntity<List<UsuarioDto>> listar() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{cdUsuario}")
    public ResponseEntity<Object> buscar(
            @PathVariable Integer cdUsuario,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {

            UsuarioModel usuarioLogado = usuarioRepository.findByNmEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


            boolean isAdmin = usuarioLogado.getRoles().stream()
                    .anyMatch(role -> role.getNmRole().equals("ADMIN"));

            // ser nao e admin ve somente o proprio perfil
            if (!isAdmin && !usuarioLogado.getCdUsuario().equals(cdUsuario)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você só pode ver seu próprio perfil!");
            }

            UsuarioDto usuario = usuarioService.buscarPorId(cdUsuario);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{cdUsuario}")
    public ResponseEntity<Object> atualizar(
            @PathVariable Integer cdUsuario,
            @RequestBody @Valid UsuarioDto usuarioDto,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // busca o usuário logado
            UsuarioModel usuarioLogado = usuarioRepository.findByNmEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // verificar se é administrador
            boolean isAdmin = usuarioLogado.getRoles().stream()
                    .anyMatch(role -> role.getNmRole().equals("ADMIN"));

            // se nao e admin, so pode edita o próprio perfil
            if (!isAdmin && !usuarioLogado.getCdUsuario().equals(cdUsuario)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você só pode editar seu próprio perfil!");
            }

            UsuarioDto usuario = usuarioService.atualizar(cdUsuario, usuarioDto);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{cdUsuario}")
    public ResponseEntity<String> inativar(@PathVariable Integer cdUsuario) {
        try {
            usuarioService.inativar(cdUsuario);
            return ResponseEntity.ok("Usuario inativado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
