package com.ecommerce.equipe.model;

public enum MetodoPagamento {
    PIX("Pagamento via Pix"),
    CARTAO("Pagamento via Cartão"),
    BOLETO("Pagamento via Boleto");

    private final String descricao;

    MetodoPagamento(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}

