package com.demo.backend_recetas.config;

import static com.demo.backend_recetas.security.Constants.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;

import com.demo.backend_recetas.security.jwt.JWTAuthorizationFilter;

@EnableWebSecurity
@EnableMethodSecurity
@Configuration
class WebSecurityConfig {

    @Autowired
    JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                .requestMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
                .requestMatchers("/api/public").permitAll()
                .requestMatchers("/api/recetas").permitAll()
                .requestMatchers("/api/recetas_buscar").permitAll()
                .requestMatchers("/api/register").permitAll()
                .requestMatchers("/api/recetas_detalle/**").authenticated()
                .requestMatchers("/api/recetas/publicar").authenticated()
                .requestMatchers("/api/recetas/{id}/comentarios").authenticated()
                .requestMatchers("/api/recetas/{id}/media").authenticated()
                .anyRequest().authenticated())
            .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        // Usamos solo domainName para desarrollo local
        serializer.setDomainName("localhost");
        serializer.setSameSite("strict");
        serializer.setUseSecureCookie(false); // false para desarrollo local, true para producción
        serializer.setCookiePath("/");
        serializer.setCookieName("JSESSIONID");
        serializer.setCookieMaxAge(3600); // 1 hora
        return serializer;
    }
}