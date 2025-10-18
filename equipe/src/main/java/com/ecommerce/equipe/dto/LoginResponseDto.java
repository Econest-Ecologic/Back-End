package com.ecommerce.equipe.dto;

import com.ecommerce.equipe.model.Estado;

import java.util.List;

public record LoginResponseDto(

        String token,
        Integer cdUsuario,
        String nmUsuario,
        String nmEmail,
        List<String> roles,
        Estado estado

) {
}