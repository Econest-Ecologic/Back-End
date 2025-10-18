package com.ecommerce.equipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "TBESTOQUE")
public class EstoqueModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "IDESTOQUE")
    private Integer cdEstoque;

    @Column(name = "QTDESTOQUE")
    private Integer qtdEstoque;

    @OneToOne
    @JoinColumn(name = "CDPRODUTO")
    private ProdutoModel cdProduto;

}
