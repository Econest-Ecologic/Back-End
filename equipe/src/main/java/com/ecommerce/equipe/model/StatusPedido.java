package com.ecommerce.equipe.model;

public enum StatusPedido {
    ABERTO("Pedido aberto"),
    PAGO("Pagamento confirmado"),
    ENVIADO("Pedido enviado"),
    ENTREGUE("Pedido entregue");

    private final String descricao;

    StatusPedido(String descricao) {
        this.descricao = descricao;
    }

}

