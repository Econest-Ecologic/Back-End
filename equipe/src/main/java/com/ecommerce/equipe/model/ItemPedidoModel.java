package com.ecommerce.equipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class ItemPedidoModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CDITEMPEDIDO")
    private Integer cdItemPedido;

    @Column(name = "QTDITEM")
    private Integer qtdItem;

    @OneToOne
    @JoinColumn(name = "CDPRODUTO")
    @Column(name = "CDPRODUTO")
    private ProdutoModel cdProduto;

    @Column(name = "PRECOUNITARIO")
    private Double precoUnitario;

}
