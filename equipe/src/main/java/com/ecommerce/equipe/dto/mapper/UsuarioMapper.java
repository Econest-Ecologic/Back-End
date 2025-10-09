package com.ecommerce.equipe.dto.mapper;

import com.ecommerce.equipe.dto.UsuarioDto;
import com.ecommerce.equipe.model.RoleModel;
import com.ecommerce.equipe.model.UsuarioModel;

import java.util.stream.Collectors;

public class UsuarioMapper {

    public static UsuarioDto toDto(UsuarioModel usuario) {
        if (usuario == null) return null;

            return new UsuarioDto(
                    usuario.getCdUsuario(),
                    usuario.getNmUsuario(),
                    usuario.getNuCpf(),
                    usuario.getNmEmail(),
                    usuario.getNmSenha(),
                    usuario.getNuTelefone(),
                    usuario.getRoles().stream()
                            .map(RoleModel::getNmRole)
                            .collect(Collectors.toList())
            );
        }

        public static UsuarioModel toModel(UsuarioDto dto) {
        if (dto == null) return null;

        UsuarioModel usuario = new UsuarioModel();
        usuario.setCdUsuario(dto.cdUsuario());
        usuario.setNmUsuario(dto.nmUsuario());
        usuario.setNuCpf(dto.nuCpf());
        usuario.setNmEmail(dto.nmEmail());
        usuario.setNmSenha(dto.nmSenha());
        usuario.setNuTelefone(dto.nuTelefone());
        return usuario;
    }
}
