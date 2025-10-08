package com.ecommerce.equipe.dto;

import com.ecommerce.equipe.model.MetodoPagamento;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PastOrPresent;

import java.util.Date;

public record PagamentoDto(

        Integer cdPagamento,

        @NotNull(message = "O método de pagamento é obrigatório")
        MetodoPagamento metodo,

        @NotNull(message = "O valor do pagamento não pode ser nulo")
        @Positive(message = "O valor do pagamento deve ser positivo")
        Double nuValor,

        @NotNull(message = "A data do pagamento é obrigatória")
        @PastOrPresent(message = "A data do pagamento não pode ser futura")
        Date dtPagamento

) { }
