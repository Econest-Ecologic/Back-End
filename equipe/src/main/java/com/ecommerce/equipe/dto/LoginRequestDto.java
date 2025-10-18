package com.ecommerce.equipe.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @NotBlank(message = "Email é obrigatório")
        @Email(message = "Email precisa ser válido")
        String nmEmail,

        @NotBlank(message = "Senha é obrigatória")
        String nmSenha

) {
}