package com.ecommerce.equipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.CustomLog;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBUSUARIO")
public class UsuarioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IDUSUARIO")
    private Integer idUsuario;

    @Column(name = "NMUSUARIO")
    private String nmUsuario;

    @Column(name = "NMEMAIL")
    private String nmEmail;

    @Column(name = "NUCPF")
    private String nuCpf;

    @Column(name = "DSENDERECO")
    private String dsEndereco;

    @Column(name = "NUTELEFONE")
    private String nuTelefone;

    //@Column(name = "TIPO")
    //private enum tipo;

}
