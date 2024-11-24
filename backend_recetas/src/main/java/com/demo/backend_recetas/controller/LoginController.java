package com.demo.backend_recetas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.GrantedAuthority;

import com.demo.backend_recetas.security.jwt.JWTAuthtenticationConfig;
import com.demo.backend_recetas.service.MyUserDetailsService;
import com.demo.backend_recetas.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Set;
import java.util.stream.Collectors;

record LoginResponse(
    String token, 
    String username, 
    Set<String> roles,
    Integer userType
) {}

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
                // Convertimos UserDetails a nuestro User personalizado
                User user = (User) userDetails;
                
                // Obtener los roles del usuario
                Set<String> roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .map(role -> role.replace("ROLE_", "")) // Remover el prefijo ROLE_
                    .collect(Collectors.toSet());

                String token = jwtAuthenticationConfig.getJWTToken(username);
                
                return ResponseEntity.ok(new LoginResponse(
                    token, 
                    username, 
                    roles,
                    user.getUserType()  // Incluimos el userType en la respuesta
                ));
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