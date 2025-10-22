package com.ecommerce.equipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "TBPAGAMENTO")
public class PagamentoModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CDPAGAMENTO")
    private Integer cdPagamento;

    @Column(name = "METODO")
    private MetodoPagamento metodo;

    @Column(name = "NUVALOR")
    private Double nuValor;

    @Column(name = "DTPAGAMENTO")
    private Date dtPagamento;
}

