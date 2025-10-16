package com.ecommerce.equipe.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "TBPEDIDO")
public class PedidoModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CDPEDIDO")
    private Integer cdPedido;

    @Column(name = "DTPEDIDO")
    private Date dtPedido;

    @Column(name = "STATUS")
    private StatusPedido status;

    @Column(name = "VLTOTAL")
    private Double vlTotal;

    @Column(name = "VLFRETE")
    private Double vlFrete;




}
