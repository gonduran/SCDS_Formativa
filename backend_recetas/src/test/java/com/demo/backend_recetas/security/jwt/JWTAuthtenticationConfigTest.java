package com.demo.backend_recetas.security.jwt;

import com.demo.backend_recetas.model.User;
import com.demo.backend_recetas.service.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertTrue;

class JWTAuthtenticationConfigTest {

    @InjectMocks
    private JWTAuthtenticationConfig jwtAuthenticationConfig;

    @Mock
    private MyUserDetailsService userDetailsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Generación de token JWT válido")
    void testGetJWTToken() {
        // Datos de prueba
        String username = "testuser";
        
        // Crear una instancia de User con el constructor completo
        User testUser = new User(
            username,
            "test@example.com",
            "dummy-password",
            "Test User",
            1  // Usuario normal
        );

        // Mockear el comportamiento del userDetailsService
        when(userDetailsService.loadUserByUsername(username)).thenReturn(testUser);

        // Llamar al método que genera el token
        String token = jwtAuthenticationConfig.getJWTToken(username);

        // Verificaciones
        assertTrue(token != null && token.startsWith("Bearer "), "Token debe comenzar con 'Bearer '");
    }
}
