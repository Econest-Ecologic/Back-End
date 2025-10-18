package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.UsuarioDto;
import com.ecommerce.equipe.service.UsuarioService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/usuario")
@RequiredArgsConstructor
public class UsuarioController {
    private final UsuarioService usuarioService;

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
    public ResponseEntity<Object> buscar(@PathVariable Integer cdUsuario) {
        try{
            UsuarioDto usuario = usuarioService.buscarPorId(cdUsuario);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping("/{cdUsuario}")
    public ResponseEntity<Object> atualizar(@PathVariable Integer cdUsuario, @RequestBody @Valid UsuarioDto usuarioDto) {
        try{
            UsuarioDto usuario = usuarioService.atualizar(cdUsuario, usuarioDto);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{cdUsuario}")
    public ResponseEntity<String> inativar(@PathVariable Integer cdUsuario) {
        try{
            usuarioService.inativar(cdUsuario);
            return ResponseEntity.ok("Usuario inativado com sucesso");
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
