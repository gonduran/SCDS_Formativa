package com.demo.backend_recetas.controller;

import com.demo.backend_recetas.model.User;
import com.demo.backend_recetas.security.jwt.JWTAuthtenticationConfig;
import com.demo.backend_recetas.service.MyUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.util.Collections;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class LoginControllerTest {

    @Mock
    private JWTAuthtenticationConfig jwtAuthenticationConfig;

    @Mock
    private MyUserDetailsService userDetailsService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private LoginController loginController;

    private User testUser;
    private String testToken;

    @BeforeEach
    void setUp() {
        // Crear usuario de prueba usando la clase User
        testUser = new User(
            "testuser",
            "test@example.com",
            "encodedPassword123",
            "Test User",
            1  // Usuario normal
        );
        
        // Token de prueba
        testToken = "test.jwt.token";
    }
    @Test
    void login_Success() {
        // Arrange
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(testUser);
        when(passwordEncoder.matches("password123", testUser.getPassword())).thenReturn(true);
        when(jwtAuthenticationConfig.getJWTToken("testuser")).thenReturn(testToken);

        // Act
        ResponseEntity<Object> response = loginController.login("testuser", "password123");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof LoginResponse);
        
        LoginResponse loginResponse = (LoginResponse) response.getBody();
        assertEquals(testToken, loginResponse.token());
        assertEquals("testuser", loginResponse.username());
        assertEquals(1, loginResponse.userType());
        assertTrue(loginResponse.roles().contains("USER")); // Verifica que tiene el rol USER
    }

    @Test
    void login_InvalidCredentials() {
        // Arrange
        when(userDetailsService.loadUserByUsername("testuser")).thenReturn(testUser);
        when(passwordEncoder.matches("wrongpassword", "encodedPassword123")).thenReturn(false);

        // Act
        ResponseEntity<Object> response = loginController.login("testuser", "wrongpassword");

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponse);
        assertEquals("Credenciales inválidas", ((ErrorResponse) response.getBody()).error());
    }

    @Test
    void login_UserNotFound() {
        // Arrange
        when(userDetailsService.loadUserByUsername("nonexistent")).thenReturn(null);

        // Act
        ResponseEntity<Object> response = loginController.login("nonexistent", "password123");

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponse);
    }

    @Test
    void login_ExceptionThrown() {
        // Arrange
        when(userDetailsService.loadUserByUsername("testuser"))
            .thenThrow(new RuntimeException("Error de autenticación"));

        // Act
        ResponseEntity<Object> response = loginController.login("testuser", "password123");

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponse);
        assertTrue(((ErrorResponse) response.getBody()).error().contains("Error de autenticación"));
    }

    @Test
    void login_EmptyCredentials() {
        // Act
        ResponseEntity<Object> response = loginController.login("", "");

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof ErrorResponse);
    }
}