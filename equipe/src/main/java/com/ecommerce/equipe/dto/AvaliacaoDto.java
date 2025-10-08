package com.ecommerce.equipe.dto;

import jakarta.validation.constraints.*;

import java.sql.Timestamp;

public record AvaliacaoDto(

        Integer cdAvaliacao,

        @NotNull(message = "O código do usuário é obrigatório")
        @Positive(message = "O código do usuário deve ser positivo")
        Integer cdUsuario,

        @NotNull(message = "O código do produto é obrigatório")
        @Positive(message = "O código do produto deve ser positivo")
        Integer cdProduto,

        @NotNull(message = "A nota da avaliação é obrigatória")
        @Min(value = 1, message = "A nota mínima é 1")
        @Max(value = 5, message = "A nota máxima é 5")
        Integer nuNota,

        @Size(max = 500, message = "O comentário não pode ter mais de 500 caracteres")
        String dsComentario,

        @NotNull(message = "A data da avaliação é obrigatória")
        @PastOrPresent(message = "A data da avaliação não pode ser futura")
        Timestamp dtAvaliacao

) {}
