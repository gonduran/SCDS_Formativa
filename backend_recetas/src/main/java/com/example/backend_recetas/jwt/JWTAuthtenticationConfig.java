package com.example.backend_recetas.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.backend_recetas.jwt.Constants.*;

@Configuration
public class JWTAuthtenticationConfig {

    public String getJWTToken(String username) {
        // Cambiamos a List<GrantedAuthority> para que getAuthority() esté disponible
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils
                .commaSeparatedStringToAuthorityList("ROLE_USER");

        // Especificamos el tipo de claims como Map<String, Object>
        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList()));

        String token = Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 1440))
                .and()
                .signWith(getSigningKey(SUPER_SECRET_KEY))
                .compact();

        return "Bearer " + token;
    }

}
