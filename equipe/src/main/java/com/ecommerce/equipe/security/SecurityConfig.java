package com.ecommerce.equipe.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.cors.CorsConfigurationSource;


import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtRequestFilter jwtRequestFilter = new JwtRequestFilter(jwtUtil, userDetailsService);

        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // ========== ROTAS PÚBLICAS ==========
                        // Registro e Login (qualquer um pode criar conta e logar)
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()

                        // ========== PRODUTOS (E-COMMERCE) ==========
                        // Qualquer um pode VER produtos (para navegar na loja)
                        .requestMatchers(HttpMethod.GET, "/api/v1/produto/**").permitAll()
                        // Apenas ADMIN pode cadastrar, editar e deletar produtos
                        .requestMatchers(HttpMethod.POST, "/api/v1/produto/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/produto/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/produto/**").hasAuthority("ADMIN")

                        // ========== USUÁRIOS ==========
                        // Listar TODOS os usuários - apenas ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuario").hasAuthority("ADMIN")

                        // Ver perfil específico - qualquer usuário autenticado
                        // (validação no controller: só pode ver o próprio ou ADMIN pode ver todos)
                        .requestMatchers(HttpMethod.GET, "/api/v1/usuario/**").authenticated()

                        // Criar usuário via /usuario - apenas ADMIN (usuários normais usam /auth/register)
                        .requestMatchers(HttpMethod.POST, "/api/v1/usuario").hasAuthority("ADMIN")

                        // Atualizar perfil - qualquer usuário autenticado
                        // (validação no controller: só pode editar o próprio ou ADMIN pode editar todos)
                        .requestMatchers(HttpMethod.PUT, "/api/v1/usuario/**").authenticated()

                        // Deletar usuário - APENAS ADMIN
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/usuario/**").hasAuthority("ADMIN")



                        // ========== PEDIDOS ==========
                        // Usuário pode criar, ver e atualizar APENAS SEUS pedidos
                        .requestMatchers("/api/v1/pedido/**").authenticated()

                        // ========== ITENS DO PEDIDO ==========
                        // Usuário pode adicionar/remover itens dos SEUS pedidos
                        .requestMatchers("/api/v1/item-pedido/**").authenticated()

                        // ========== AVALIAÇÕES ==========
                        // Qualquer um pode VER avaliações (para ver reviews dos produtos)
                        .requestMatchers(HttpMethod.GET, "/api/v1/avaliacao/**").permitAll()
                        // Usuário autenticado pode criar/deletar avaliação
                        .requestMatchers("/api/v1/avaliacao/**").authenticated()


// ========== ESTOQUE ==========
// Qualquer um pode VER estoque (para saber se tem produto disponível)
                                .requestMatchers(HttpMethod.GET, "/api/v1/estoque/**").permitAll()

// ✅ NOVO: Usuários autenticados podem RESERVAR e LIBERAR estoque
                                .requestMatchers(HttpMethod.POST, "/api/v1/estoque/reservar").authenticated()
                                .requestMatchers(HttpMethod.POST, "/api/v1/estoque/liberar").authenticated()

// Apenas ADMIN pode gerenciar estoque diretamente
                                .requestMatchers(HttpMethod.POST, "/api/v1/estoque").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PUT, "/api/v1/estoque/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.PATCH, "/api/v1/estoque/**").hasAuthority("ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/api/v1/estoque/**").hasAuthority("ADMIN")

                        // ========== PAGAMENTO ==========
                        // Usuário pode pagar APENAS SEUS pedidos
                        .requestMatchers("/api/v1/pagamento/**").authenticated()

                        // Qualquer outra rota requer autenticação
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Token inválido ou ausente\"}");
                        })
                )
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}