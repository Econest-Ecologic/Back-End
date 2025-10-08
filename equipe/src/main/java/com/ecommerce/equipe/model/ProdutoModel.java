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
    @Column(name = "CDPRODUTO")
    private Integer cdProduto;


    @Column(name = "NMPRODUTO")
    private String nmProduto;

    @Column(name = "DSPRODUTO")
    private String dsProduto;

    @Column(name = "PRECO")
    private Double  preco;
git commit -
    @Column(name = "CATEGORIA")
    private String categoria;

    @Lob
    @Column(name = "DSLINKIMG", nullable = false)
    private byte[] dsLinkImag;

}
