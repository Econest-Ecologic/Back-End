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
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        // Rotas públicas de autenticação (MELHORADO: Agrupadas)
                        .requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll()

                        // Rotas de produtos (público pode ver, só admin cadastra)
                        .requestMatchers(HttpMethod.GET, "/api/v1/produto/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/produto/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/produto/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/produto/**").hasAuthority("ADMIN")

                        .requestMatchers("/api/v1/item-pedido/**").permitAll()

                        // Rotas de pedido (usuário autenticado)
                        .requestMatchers("/api/v1/pedido/**").authenticated()

                        // Rotas de avaliação (usuário autenticado pode avaliar)
                        .requestMatchers(HttpMethod.GET, "/api/v1/avaliacao/**").permitAll() // Ver avaliações é público
                        .requestMatchers("/api/v1/avaliacao/**").authenticated() // Criar/deletar precisa autenticação

                        // Rotas de usuário (admin)
                        .requestMatchers("/api/v1/usuario/**").hasAuthority("ADMIN")

                        // Rotas de estoque (admin cadastra, usuário pode consultar)
                        .requestMatchers(HttpMethod.GET, "/api/v1/estoque/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/estoque/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/estoque/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/v1/estoque/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/estoque/**").hasAuthority("ADMIN")

                        // Rotas de pagamento (usuário autenticado)
                        .requestMatchers("/api/v1/pagamento/**").authenticated()

                        // Qualquer outra requisição precisa estar autenticada
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"error\":\"Unauthorized\",\"message\":\"Token required\"}");
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
}