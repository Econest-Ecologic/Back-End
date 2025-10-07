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
    @Column(name = "IDAVALIACAO")
    private Integer idAvaliacao;

    //@Column(name = "IDPRODUTO")
    //private ProdutoModel idProduto;

    //@Column(name = "IDUSUARIO")
    //private UsuarioModel idUsuario;

    @Column(name = "NUNOTA")
    private Integer nuNota; // 1 - 5

    @Column(name = "DSCOMENTARIO")
    private String dsComentario;

    @Column(name = "DTAVALIACAO")
    private Timestamp dtAvaliacao;

}
