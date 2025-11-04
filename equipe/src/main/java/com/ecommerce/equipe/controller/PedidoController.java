package com.ecommerce.equipe.controller;

import com.ecommerce.equipe.dto.PedidoDto;
import com.ecommerce.equipe.model.PedidoModel;
import com.ecommerce.equipe.model.UsuarioModel;
import com.ecommerce.equipe.repository.UsuarioRepository;
import com.ecommerce.equipe.service.PedidoService;
import jakarta.validation.Valid;
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
public class PedidoController {

    private final PedidoService pedidoService;
    private final UsuarioRepository usuarioRepository;

    public PedidoController(PedidoService pedidoService, UsuarioRepository usuarioRepository) {
        this.pedidoService = pedidoService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/usuario/{cdUsuario}")
    public ResponseEntity<?> salvar(@PathVariable Integer cdUsuario,
                                    @RequestBody @Valid PedidoDto pedidoDto) {
        try {
            PedidoModel pedido = pedidoService.salvar(cdUsuario, pedidoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(pedido);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> listar(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            UsuarioModel usuarioLogado = usuarioRepository.findByNmEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            boolean isAdmin = usuarioLogado.getRoles().stream()
                    .anyMatch(role -> role.getNmRole().equals("ADMIN"));

            if (isAdmin) {
                return ResponseEntity.ok(pedidoService.listar());
            } else {
                List<PedidoModel> pedidos = pedidoService.listarPorUsuario(usuarioLogado.getCdUsuario());
                return ResponseEntity.ok(pedidos);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário não autenticado");
        }
    }

    @GetMapping("/{cdPedido}")
    public ResponseEntity<?> buscarPedido(@PathVariable Integer cdPedido, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UsuarioModel usuarioLogado = usuarioRepository.findByNmEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            Optional<PedidoModel> pedido = pedidoService.buscarPorId(cdPedido);

            if (pedido.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
            }

            boolean isAdmin = usuarioLogado.getRoles().stream()
                    .anyMatch(role -> role.getNmRole().equals("ADMIN"));

            if (!isAdmin && !pedido.get().getUsuario().getCdUsuario().equals(usuarioLogado.getCdUsuario())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você só pode ver seus próprios pedidos!");
            }

            return ResponseEntity.ok(pedido.get());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{cdPedido}")
    public ResponseEntity<?> atualizar(@PathVariable Integer cdPedido, @RequestBody @Valid PedidoDto pedidoDto,
                                       @AuthenticationPrincipal UserDetails userDetails) {
        try {
            UsuarioModel usuarioLogado = usuarioRepository.findByNmEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            Optional<PedidoModel> pedido = pedidoService.buscarPorId(cdPedido);
            if (pedido.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
            }

            boolean isAdmin = usuarioLogado.getRoles().stream()
                    .anyMatch(role -> role.getNmRole().equals("ADMIN"));

            if (!isAdmin && !pedido.get().getUsuario().getCdUsuario().equals(usuarioLogado.getCdUsuario())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Você não pode atualizar o pedido de outro usuário!");
            }

            PedidoModel atualizado = pedidoService.atualizar(cdPedido, pedidoDto);
            return ResponseEntity.ok(atualizado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{cdPedido}")
    public ResponseEntity<?> cancelar(@PathVariable Integer cdPedido,
                                      @AuthenticationPrincipal UserDetails userDetails) {
        try {

            UsuarioModel usuarioLogado = usuarioRepository.findByNmEmail(userDetails.getUsername())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));


            boolean isAdmin = usuarioLogado.getRoles().stream()
                    .anyMatch(role -> role.getNmRole().equals("ADMIN"));

            if (!isAdmin) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("Apenas administradores podem deletar pedidos!");
            }


            Optional<PedidoModel> pedido = pedidoService.buscarPorId(cdPedido);
            if (pedido.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pedido não encontrado");
            }

            pedidoService.cancelarPedido(cdPedido);
            return ResponseEntity.ok("Pedido deletado com sucesso pelo administrador!");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
