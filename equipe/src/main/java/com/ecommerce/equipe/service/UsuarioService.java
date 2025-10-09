package com.ecommerce.equipe.service;

import com.ecommerce.equipe.dto.UsuarioDto;
import com.ecommerce.equipe.repository.RoleRepository;
import com.ecommerce.equipe.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final RoleRepository roleRepository;

    public UsuarioDto salvar(UsuarioDto usuarioDto) {

    }
}
