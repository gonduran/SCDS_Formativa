package com.demo.backend_recetas.security.jwt;

import com.demo.backend_recetas.service.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JWTAuthtenticationConfigTest {

    @InjectMocks
    private JWTAuthtenticationConfig jwtAuthenticationConfig;

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetJWTToken() {
        // Datos de prueba
        String username = "testuser";
        String role = "USER"; // Solo "USER", sin "ROLE_"

        // Mockear el comportamiento del userDetailsService y UserDetails
        when(userDetailsService.loadUserByUsername(username)).thenReturn(
                User.builder()
                    .username(username)
                    .password("dummy-password") // El password no es relevante aquí
                    .roles(role) // Pasar solo "USER", sin "ROLE_"
                    .build()
        );

        // Llamar al método que genera el token
        String token = jwtAuthenticationConfig.getJWTToken(username);

        // Verificar que el token no es nulo
        assertTrue(token != null && token.startsWith("Bearer "), "Token debe comenzar con 'Bearer '");

        // Verificar que el token contiene la información correcta
        String expectedTokenPrefix = "Bearer ";
        assertTrue(token.startsWith(expectedTokenPrefix), "El token debe comenzar con el prefijo esperado");

    }
}
