package com.ecommerce.equipe.dto;

import jakarta.validation.constraints.*;

public record ItemPedidoDto(

        @NotNull(message = "Não é possível salvar um produto sem quantidade")
        Integer qtdItem,

        @NotNull(message = "Não é possível salvar um produto sem valor")
        @DecimalMin(value = "0.01", message = "Não é possivel salvar um valor abaixo de 0.01")
        Double precoUnitario,

        @NotNull(message = "O produto é obrigatório")
        Integer cdProduto

        ){}
