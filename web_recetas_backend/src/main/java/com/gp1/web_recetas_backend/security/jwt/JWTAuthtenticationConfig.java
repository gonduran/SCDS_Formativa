package com.gp1.web_recetas_backend.security.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import com.gp1.web_recetas_backend.service.MyUserDetailsService;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.gp1.web_recetas_backend.security.Constants.*;

@Configuration
public class JWTAuthtenticationConfig {

    @Autowired
    private MyUserDetailsService userDetailsService;

    public String getJWTToken(String username) {
        // Obtener el UserDetails para acceder a las authorities reales del usuario
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        
        // Obtener las authorities del usuario
        List<String> authorities = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        Map<String, Object> claims = new HashMap<>();
        claims.put("authorities", authorities);

        String token = Jwts.builder()
                .claims()
                .add(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
                .and()
                .signWith(getSigningKey(SUPER_SECRET_KEY))
                .compact();

        return TOKEN_BEARER_PREFIX + token;
    }
}