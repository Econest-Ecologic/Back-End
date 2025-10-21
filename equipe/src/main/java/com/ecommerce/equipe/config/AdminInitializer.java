package com.ecommerce.equipe.config;

import com.ecommerce.equipe.model.RoleModel;
import com.ecommerce.equipe.model.UsuarioModel;
import com.ecommerce.equipe.repository.RoleRepository;
import com.ecommerce.equipe.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Configuration
public class AdminInitializer {

    @Bean
    public CommandLineRunner criarAdminInicial(UsuarioRepository usuarioRepository,
                                               RoleRepository roleRepository,
                                               PasswordEncoder passwordEncoder) {
        return args -> {
            String emailAdmin = "admin@admin.com";
            String senhaAdmin = "admin123";

            RoleModel roleAdmin = buscarOuCriarRole(roleRepository, "ADMIN");
            RoleModel roleUser = buscarOuCriarRole(roleRepository, "USER");

            Optional<UsuarioModel> adminExiste = usuarioRepository.findByNmEmail(emailAdmin);

            if (adminExiste.isEmpty()) {
                UsuarioModel admin = new UsuarioModel();
                admin.setNmUsuario("Administrador");
                admin.setNmEmail(emailAdmin);
                admin.setNmSenha(passwordEncoder.encode(senhaAdmin));
                admin.setNuCpf("000.000.000-00");
                admin.setDsEndereco("Endereço do admin");
                admin.setNuTelefone("00000000000");
                admin.setFlAtivo(true);
                admin.setEstado(null);
                admin.setImgUsuario(null);

                Set<RoleModel> roles = new HashSet<>();
                roles.add(roleAdmin);
                roles.add(roleUser);
                admin.setRoles(roles);

                usuarioRepository.save(admin);

                System.out.println("Administrador criado! Email: " + emailAdmin + ", Senha: " + senhaAdmin);
            } else {
                System.out.println("Administrador já existente no banco.");
            }
        };
    }

    private RoleModel buscarOuCriarRole(RoleRepository roleRepository, String nomeRole) {
        return roleRepository.findByNmRole(nomeRole)
                .orElseGet(() -> {
                    RoleModel role = new RoleModel();
                    role.setNmRole(nomeRole);
                    return roleRepository.save(role);
                });
    }
}