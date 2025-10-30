package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.PedidoDto;
import com.ecommerce.equipe.model.PedidoModel;
import com.ecommerce.equipe.model.UsuarioModel;
import com.ecommerce.equipe.repository.PedidoRepository;
import com.ecommerce.equipe.repository.UsuarioRepository;
import com.ecommerce.equipe.service.PedidoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/v1/pedido")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioRepository usuarioRepository;

    @PostMapping("/usuario/{cdUsuario}")
    public ResponseEntity<PedidoModel> salvar(
            @PathVariable Integer cdUsuario,
            @RequestBody @Valid PedidoDto pedidoDto) {
        PedidoModel pedido = pedidoService.salvar(cdUsuario, pedidoDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
    }

    @GetMapping
    public ResponseEntity<List<PedidoModel>> listar(@AuthenticationPrincipal UserDetails userDetails) {
        // Buscar o usuário logado
        UsuarioModel usuarioLogado = usuarioRepository.findByNmEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Verificar se é ADMIN
        boolean isAdmin = usuarioLogado.getRoles().stream()
                .anyMatch(role -> role.getNmRole().equals("ADMIN"));

        if (isAdmin) {
            // ADMIN vê TODOS os pedidos
            return ResponseEntity.ok(pedidoService.listar());
        } else {
            // USER vê apenas SEUS pedidos
            List<PedidoModel> pedidos = pedidoService.listarPorUsuario(usuarioLogado.getCdUsuario());
            return ResponseEntity.ok(pedidos);
        }
    }

    @GetMapping("/{cdPedido}")
    public ResponseEntity<Object> buscarPedido(
            @PathVariable("cdPedido") Integer cdPedido,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            // Buscar o usuário logado
            UsuarioModel usuarioLogado = usuarioRepository.findByNmEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            // Buscar o pedido
            Optional<PedidoModel> pedido = pedidoService.buscarPorId(cdPedido);
            if (pedido.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
            }

            // Verificar se é ADMIN
            boolean isAdmin = usuarioLogado.getRoles().stream()
                    .anyMatch(role -> role.getNmRole().equals("ADMIN"));

            // Se não for ADMIN, só pode ver SEUS pedidos
            if (!isAdmin && !pedido.get().getUsuario().getCdUsuario().equals(usuarioLogado.getCdUsuario())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você só pode ver seus próprios pedidos!");
            }

            return ResponseEntity.status(HttpStatus.OK).body(pedido.get());
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{cdUsuario}")
    public ResponseEntity<List<PedidoModel>> listarPorUsuario(@PathVariable Integer cdUsuario) {
        List<PedidoModel> pedidos = pedidoService.listarPorUsuario(cdUsuario);
        return ResponseEntity.ok(pedidos);
    }

    @PutMapping("/{cdPedido}")
    public ResponseEntity<Object> atualizar(@PathVariable Integer cdPedido, @RequestBody @Valid PedidoDto pedidoDto) {
        try {
            PedidoModel atualizado = pedidoService.atualizar(cdPedido, pedidoDto);
            return ResponseEntity.ok(atualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/{cdPedido}")
    public ResponseEntity<String> cancelar(@PathVariable Integer cdPedido) {
        try {
            pedidoService.cancelarPedido(cdPedido);
            return ResponseEntity.ok("Pedido cancelado com sucesso");
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}