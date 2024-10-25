package com.gp1.web_recetas_backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gp1.web_recetas_backend.security.jwt.JWTAuthtenticationConfig;
import com.gp1.web_recetas_backend.service.MyUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

@RestController
public class LoginController {

    @Autowired
    private JWTAuthtenticationConfig jwtAuthenticationConfig;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @RequestParam("user") String username,
            @RequestParam("encryptedPass") String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (userDetails != null && passwordEncoder.matches(password, userDetails.getPassword())) {
                String token = jwtAuthenticationConfig.getJWTToken(username);
                return ResponseEntity.ok()
                    .body(Map.of(
                        "token", token,
                        "username", username
                    ));
            } else {
                return ResponseEntity.status(401)
                    .body(Map.of("error", "Credenciales inválidas"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401)
                .body(Map.of("error", "Error en la autenticación: " + e.getMessage()));
        }
    }
}