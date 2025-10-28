package com.ecommerce.equipe.dto;

import com.ecommerce.equipe.model.Estado;
import com.ecommerce.equipe.model.RoleModel;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record UsuarioDto (

        Integer cdUsuario,

        @NotBlank(message = "Nome é obrigatório")
        @Size(min = 3, max = 100, message = "O nome deve ter entre 3 e 100 caracteres")
        String nmUsuario,

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email precisa ser válido")
        String nmEmail,

        @NotBlank(message = "Senha é obrigatório")
        @Size(min = 3, message = "A senha deve ter no mínimo 3 caracteres")
        String nmSenha,

        @NotBlank(message = "CPF é obrigatório")
        @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos numéricos")
        String nuCpf,

        @NotBlank(message = "Descrição é obrigatório")
        @Size(max = 255, message = "Endereço não pode ultrapassar 255 caracteres")
        String dsEndereco,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter entre 10 e 11 dígitos numéricos")
        String nuTelefone,

        List<RoleModel> roles,

        Boolean flAtivo,

        @NotNull(message = "Estado é obrigatório")
        Estado estado,

        MultipartFile imgUsuario
){
}
