package com.ecommerce.equipe.dto;

import com.ecommerce.equipe.model.Estado;
import com.ecommerce.equipe.model.StatusPedido;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PastOrPresent;

import java.util.Date;

public record PedidoDto(

        Integer cdPedido,

        @NotNull(message = "A data do pedido não pode ser nula")
        @PastOrPresent(message = "A data do pedido não pode ser futura")
        Date dtPedido,

        @NotNull(message = "O status do pedido é obrigatório")
        StatusPedido status,

        @NotNull(message = "O valor total não pode ser nulo")
        @Positive(message = "O valor total deve ser positivo")
        Double vlTotal,

        @NotNull(message = "O valor do frete não pode ser nulo")
        @Positive(message = "O valor do frete deve ser positivo")
        Double vlFrete


) { }
