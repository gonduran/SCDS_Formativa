package com.example.backend_recetas.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.backend_recetas.jwt.Constants;
import com.example.backend_recetas.jwt.JWTAuthorizationFilter;

@EnableWebSecurity()
@Configuration
class WebSecurityConfig{

    @Autowired
    JWTAuthorizationFilter jwtAuthorizationFilter;

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {

        http
                .csrf((csrf) -> csrf
                        .disable())
                .authorizeHttpRequests( authz -> authz
                        .requestMatchers(HttpMethod.POST,Constants.LOGIN_URL).permitAll()
                        .anyRequest().authenticated())
                .addFilterAfter(jwtAuthorizationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}