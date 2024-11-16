package com.demo.backend_recetas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.backend_recetas.security.jwt.JWTAuthtenticationConfig;
import com.demo.backend_recetas.service.MyUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;

// Creamos una clase para representar la respuesta
record LoginResponse(String token, String username) {}
record ErrorResponse(String error) {}

@RestController
public class LoginController {

    private final JWTAuthtenticationConfig jwtAuthenticationConfig;
    private final MyUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    // Constructor injection en lugar de @Autowired
    public LoginController(
            JWTAuthtenticationConfig jwtAuthenticationConfig,
            MyUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        this.jwtAuthenticationConfig = jwtAuthenticationConfig;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @RequestParam("user") String username,
            @RequestParam("encryptedPass") String password) {
        try {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            
            if (userDetails != null && passwordEncoder.matches(password, userDetails.getPassword())) {
                String token = jwtAuthenticationConfig.getJWTToken(username);
                return ResponseEntity.ok(new LoginResponse(token, username));
            } else {
                return ResponseEntity.status(401)
                    .body(new ErrorResponse("Credenciales inválidas"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(401)
                .body(new ErrorResponse("Error en la autenticación: " + e.getMessage()));
        }
    }
}