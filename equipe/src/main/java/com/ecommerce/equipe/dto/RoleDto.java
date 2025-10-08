package com.ecommerce.equipe.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RoleDto(

        Integer cdRole,

        @NotBlank(message = "Você precisa dar um nome à ROLE")
        @Size(min = 4, max = 30, message = "O nome da ROLE deve ter entre 4 e 30 caracteres")
        @Pattern(
                regexp = "^ROLE_[A-Z]+$",
                message = "O nome da ROLE deve estar em letras maiúsculas e começar com 'ROLE_', ex: ROLE_ADMIN"
        )
        String nmRole
) {
}
