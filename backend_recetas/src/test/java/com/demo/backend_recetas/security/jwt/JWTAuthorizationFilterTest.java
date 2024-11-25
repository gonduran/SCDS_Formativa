package com.demo.backend_recetas.security.jwt;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.context.SecurityContextHolder;

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
    void doFilterInternal_TokenMalformado_RetornaForbidden() throws ServletException, IOException {
        // Arrange
        when(request.getHeader(HEADER_AUTHORIZACION_KEY)).thenReturn("Bearer invalid.token.here");

        // Act
        jwtAuthorizationFilter.doFilterInternal(request, response, filterChain);

        // Assert
        verify(response).setStatus(HttpServletResponse.SC_FORBIDDEN);
        verify(response).sendError(eq(HttpServletResponse.SC_FORBIDDEN), anyString());
    }
}