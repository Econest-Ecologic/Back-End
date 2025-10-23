package com.ecommerce.equipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "TBITEMPEDIDO")
public class ItemPedidoModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CDITEMPEDIDO")
    private Integer cdItemPedido;

    @Column(name = "QTDITEM")
    private Integer qtdItem;

    @Column(name = "PRECOUNITARIO")
    private Double precoUnitario;

    @OneToOne
    @JoinColumn(name = "CDPRODUTO")
    private ProdutoModel cdProduto;

    @ManyToOne
    @JoinColumn(name = "CDPEDIDO")
    private PedidoModel pedido;

}
