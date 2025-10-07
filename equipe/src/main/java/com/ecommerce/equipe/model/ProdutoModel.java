package com.ecommerce.equipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBPRODUTO")
public class ProdutoModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDPRODUTO")
    private Integer idProduto;

    //@Column(name = "IDESTOQUE")
    //private Estoque idEstoque;

    @Column(name = "NMPRODUTO")
    private String nmProduto;

    @Column(name = "DSPRODUTO")
    private String dsProduto;

    @Column(name = "PRECO")
    private Double preco;

    @Column(name = "CATEGORIA")
    private String categoria;

    @Column(name = "DSLINKIMG")
    private String dsLinkImag;

}
