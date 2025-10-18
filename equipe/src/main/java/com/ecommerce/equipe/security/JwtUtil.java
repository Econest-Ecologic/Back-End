package com.ecommerce.equipe.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class JwtUtil {

    private final String secretKey = "sua-chave-secreta-super-segura-que-deve-ser-bem-longa";

    // Método original só com username
    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }

    // Novo método que recebe username + roles
    public String generateToken(String username, List<String> roles) {
        return Jwts.builder()
                .setSubject(username) // Define o nome de usuário como o assunto do token
                .claim("roles", roles) // Adiciona as roles como claim personalizada
                .setIssuedAt(new Date()) // Data de emissão
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora validade
                .signWith(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
                .compact();
    }



    // Extrai as claims do token JWT (informações adicionais do token)
    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8))) // Define a chave secreta para validar a assinatura do token
                .build()
                .parseClaimsJws(token) // Analisa o token JWT e obtém as claims
                .getBody(); // Retorna o corpo das claims
    }

    // Extrai o nome de usuário do token JWT
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date()); // Compara a data de expiração do token com a data atual
    }

    // Valida o token JWT verificando o nome de usuário e se o token não está expirado
    public boolean validateToken(String token, String username) {
        final String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    // Extrai as roles do token JWT
    public List<String> extractRoles(String token) {
        Claims claims = extractClaims(token); // Pega as claims do token
        return claims.get("roles", List.class); // Extrai a claim "roles" como lista
    }
}
