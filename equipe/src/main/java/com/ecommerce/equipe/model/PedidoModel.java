package com.ecommerce.equipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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

    @Column(name = "FLATIVO")
    private Boolean flAtivo = true;

    @ManyToOne
    @JoinColumn(name = "CDUSUARIO")
    private UsuarioModel usuario;

    @OneToMany(mappedBy = "pedido", cascade = CascadeType.ALL)
    private List<ItemPedidoModel> itens = new ArrayList<>();


}
