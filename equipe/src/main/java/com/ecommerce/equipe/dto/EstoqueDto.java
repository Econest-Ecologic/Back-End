package com.ecommerce.equipe.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record EstoqueDto(

        Integer cdEstoque,

        @NotNull(message = "A quantidade em estoque é obrigatória")
        @Min(value = 0, message = "A quantidade em estoque não pode ser negativa")
        Integer qtdEstoque,

        @NotNull(message = "O código do produto é obrigatório")
        @Positive(message = "O código do produto deve ser positivo")
        Integer cdProduto

) { }
