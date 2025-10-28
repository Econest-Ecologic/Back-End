package com.ecommerce.equipe.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TBUSUARIO")
public class UsuarioModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CDUSUARIO")
    private Integer cdUsuario;

    @Column(name = "NMUSUARIO")
    private String nmUsuario;

    @Column(name = "NMEMAIL",unique = true)
    private String nmEmail;

    @Column(name = "NMSENHA")
    private String nmSenha;

    @Column(name = "NUCPF")
    private String nuCpf;

    @Column(name = "DSENDERECO")
    private String dsEndereco;

    @Column(name = "NUTELEFONE")
    private String nuTelefone;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "TBUSUARIOS_ROLES",
        joinColumns = @JoinColumn(name = "CDUSUARIO"),
        inverseJoinColumns = @JoinColumn(name = "CDROLE"))
    private Set<RoleModel> roles = new HashSet<>();

    @Column(name = "FLATIVO")
    private Boolean flAtivo = true;

    @Lob
    @Column(name = "IMGUSUARIO", nullable = true)
    private byte[] imgUsuario;

    @Column(name = "ESTADO")
    private Estado estado;
}
