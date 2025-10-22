package com.ecommerce.equipe.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Min;
import org.springframework.web.multipart.MultipartFile;

public record ProdutoDto(

        Integer cdProduto,

        @NotBlank(message = "Não é possível salvar um produto sem nome")
        @Size(min = 3, max = 50, message = "O nome do produto deve ter entre 3 e 50 caracteres.")
        String nmProduto,

        @NotBlank(message = "A descrição do produto não pode estar em branco.")
        @Size(min = 3, max = 200, message = "A descrição do produto deve ter entre 3 e 200 caracteres.")
        String dsProduto,

        @NotNull(message = "Não é possível salvar um produto sem valor")
        @DecimalMin(value = "0.01", message = "Não é possivel salvar um valor abaixo de 0.01")
        Double preco,

        @NotBlank(message = "A categoria do produto não pode estar em branco.")
        @Size(min = 3, max = 50, message = "A categoria do produto deve ter entre 3 e 50 caracteres.")
        String categoria,

        MultipartFile imgProduto,

        Boolean flAtivo,

        @NotNull(message = "A quantidade em estoque é obrigatória")
        @Min(value = 0, message = "A quantidade em estoque não pode ser negativa")
        Integer qtdEstoque
) {}