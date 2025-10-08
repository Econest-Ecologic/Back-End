package com.ecommerce.equipe.dto;

import com.ecommerce.equipe.model.RoleModel;
import jakarta.validation.constraints.*;

import java.util.List;

public record itemPedidoDto(

        @NotNull(message = "Não é possível salvar um produto sem quantidade")
        Integer qut,

        @NotNull(message = "Não é possível salvar um produto sem valor")
        @DecimalMin(value = "0.01", message = "Não é possivel salvar um valor abaixo de 0.01")
        Double precoUnitario),





        @NotBlank(message = "Descrição é obrigatório")
        @Size(max = 255, message = "Endereço não pode ultrapassar 255 caracteres")
        String dsEndereco,

        @NotBlank(message = "Telefone é obrigatório")
        @Pattern(regexp = "\\d{10,11}", message = "Telefone deve ter entre 10 e 11 dígitos numéricos")
        String nuTelefone,

        ){}
