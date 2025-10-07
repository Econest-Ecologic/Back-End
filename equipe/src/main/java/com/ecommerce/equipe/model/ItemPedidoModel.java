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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDITEMCARRINHO")
    private Integer idItemCarrinho;

    @Column(name = "QTITEM")
    private Integer qtItem;

    //@Column(name = "IDPRODUTO")
    //private ProdutoModel idProduto;

    @Column(name = "PRECOUNITARIO")
    private Double precoUnitario;

}
