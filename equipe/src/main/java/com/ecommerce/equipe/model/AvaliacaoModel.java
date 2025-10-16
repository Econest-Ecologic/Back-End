package com.ecommerce.equipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "TBAVALIACAO")
public class AvaliacaoModel {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "CDAVALIACAO")
    private Integer cdAvaliacao;

    @ManyToOne
    @JoinColumn(name = "CDUSUARIO")
    private UsuarioModel cdUsuario;

    @ManyToOne
    @JoinColumn(name = "CDPRODUTO")
    private ProdutoModel cdProduto;

    @Column(name = "NUNOTA")
    private Integer nuNota; // 1 - 5

    @Column(name = "DSCOMENTARIO")
    private String dsComentario;

    @Column(name = "DTAVALIACAO")
    private Timestamp dtAvaliacao;

}
