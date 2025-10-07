package com.ecommerce.equipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "TBPAGAMENTO")
public class PagamentoModel {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Integer idPagamento;

    //@Column(name = "METODO")
    //private enum metodo;

    @Column(name = "NUVALOR")
    private Double nuValor;

    @Column(name = "DTPAGAMENTO")
    private Double dtPagamento;
}
