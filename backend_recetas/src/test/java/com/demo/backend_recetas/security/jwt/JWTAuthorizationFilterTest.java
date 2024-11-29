package com.demo.backend_recetas.security.jwt;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static com.demo.backend_recetas.security.Constants.*;

@ExtendWith(MockitoExtension.class)
public class JWTAuthorizationFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JWTAuthorizationFilter jwtAuthorizationFilter;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Filtro sin token limpia el contexto de seguridad")
    void doFilterInternal_SinToken_LimpiaContexto() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HEADER_AUTHORIZACION_KEY)).thenReturn(null);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Filtro con token inválido limpia el contexto de seguridad")
    void doFilterInternal_TokenInvalido_LimpiaContexto() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HEADER_AUTHORIZACION_KEY)).thenReturn("InvalidToken");

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Filtro con token malformado retorna forbidden")
    void doFilterInternal_TokenMalformado_RetornaForbidden() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HEADER_AUTHORIZACION_KEY)).thenReturn("Bearer invalid.token.here");

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
    }

    @Test
    @DisplayName("Token válido establece la autenticación correctamente")
    void doFilterInternal_TokenValido_EstableceAutenticacion() throws ServletException, IOException {
        // Arrange
        String validToken = "Bearer " + generarTokenValido();
        when(request.getHeader(HEADER_AUTHORIZACION_KEY)).thenReturn(validToken);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Token válido sin autoridades limpia el contexto")
    void doFilterInternal_TokenValidoSinAutoridades_LimpiaContexto() throws ServletException, IOException {
        // Arrange
        String validTokenSinAutoridades = "Bearer " + generarTokenSinAutoridades();
        when(request.getHeader(HEADER_AUTHORIZACION_KEY)).thenReturn(validTokenSinAutoridades);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Token expirado retorna forbidden")
    void doFilterInternal_TokenExpirado_RetornaForbidden() throws ServletException, IOException {
        // Arrange
        String expiredToken = "Bearer " + generarTokenExpirado();
        when(request.getHeader(HEADER_AUTHORIZACION_KEY)).thenReturn(expiredToken);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
    }

    @Test
    @DisplayName("Token con formato incorrecto retorna false en validación")
    void isJWTValid_TokenMalFormato_RetornaFalse() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HEADER_AUTHORIZACION_KEY)).thenReturn("InvalidFormat");

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    @Test
    @DisplayName("Excepción en setAuthentication limpia el contexto de seguridad")
    void setAuthentication_ConExcepcion_LimpiaContexto() throws ServletException, IOException {
        // Arrange
        String tokenConClaimsInvalidos = "Bearer " + Jwts.builder()
                .subject("user")
                .claim("authorities", "INVALID_FORMAT") // Esto causará una excepción al intentar castearlo a List
                .signWith(getSigningKey(SUPER_SECRET_KEY))
                .compact();
                
        when(request.getHeader(HEADER_AUTHORIZACION_KEY)).thenReturn(tokenConClaimsInvalidos);

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        assertNull(SecurityContextHolder.getContext().getAuthentication());
        verify(filterChain).doFilter(request, response);
    }

    // Métodos auxiliares para generar tokens de prueba
    private String generarTokenValido() {
        return Jwts.builder()
                .subject("user")
                .claim("authorities", Arrays.asList("ROLE_USER"))
                .signWith(getSigningKey(SUPER_SECRET_KEY))
                .compact();
    }

    private String generarTokenSinAutoridades() {
        return Jwts.builder()
                .subject("user")
                .signWith(getSigningKey(SUPER_SECRET_KEY))
                .compact();
    }

    private String generarTokenExpirado() {
        return Jwts.builder()
                .subject("user")
                .claim("authorities", Arrays.asList("ROLE_USER"))
                .expiration(new Date(System.currentTimeMillis() - 1000)) // Cambiado de setExpiration a expiration
                .signWith(getSigningKey(SUPER_SECRET_KEY))
                .compact();
    }
}