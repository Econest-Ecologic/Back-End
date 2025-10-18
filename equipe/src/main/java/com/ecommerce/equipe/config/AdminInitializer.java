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
            // Informações do admin padrão
            String emailAdmin = "admin@admin.com";
            String senhaAdmin = "admin123";

            // Verifica se as roles já existem. Se não, cria.
            RoleModel roleAdmin = roleRepository.findByNmRole("ADMIN")
                    .orElse(null);

            if (roleAdmin == null) {
                roleAdmin = new RoleModel();
                roleAdmin.setNmRole("ADMIN");
                roleAdmin = roleRepository.save(roleAdmin);
            }

            RoleModel roleUser = roleRepository.findByNmRole("USER")
                    .orElse(null);

            if (roleUser == null) {
                roleUser = new RoleModel();
                roleUser.setNmRole("USER");
                roleUser = roleRepository.save(roleUser);
            }

            // Verifica se o usuário admin já existe
            Optional<UsuarioModel> adminExiste = usuarioRepository.findByNmEmail(emailAdmin);

            if (adminExiste.isEmpty()) {
                // Cria o usuário administrador
                UsuarioModel admin = new UsuarioModel();
                admin.setNmUsuario("Administrador");
                admin.setNmEmail(emailAdmin);
                admin.setNmSenha(passwordEncoder.encode(senhaAdmin));
                admin.setNuCpf("000.000.000-00");
                admin.setDsEndereco("Endereço do admin");
                admin.setNuTelefone("00000000000");
                admin.setFlAtivo(true);
                admin.setEstado(null); // Pode colocar um valor padrão depois
                admin.setImgUsuario(null);

                // Define as roles do admin
                Set<RoleModel> roles = new HashSet<>();
                roles.add(roleAdmin);
                roles.add(roleUser); // Admin também pode agir como usuário
                admin.setRoles(roles);

                // Salva no banco
                usuarioRepository.save(admin);

                System.out.println("Administrador criado! Email: " + emailAdmin + ", Senha: " + senhaAdmin);
            } else {
                System.out.println("Administrador já existente no banco.");
            }
        };
    }
}
