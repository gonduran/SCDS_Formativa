package com.demo.web_recetas.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class CustomAuthenticationProviderTest {

    private CustomAuthenticationProvider customAuthenticationProvider;
    private TokenStore tokenStore;
    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() throws Exception {
        tokenStore = mock(TokenStore.class);
        restTemplate = mock(RestTemplate.class);

        // Injectar RestTemplate mock en el CustomAuthenticationProvider
        customAuthenticationProvider = new CustomAuthenticationProvider(tokenStore);

        // Configurar el campo privado `backendUrl` usando Reflection
        Field backendUrlField = CustomAuthenticationProvider.class.getDeclaredField("backendUrl");
        backendUrlField.setAccessible(true);
        backendUrlField.set(customAuthenticationProvider, "http://mock-backend");
    }

    

    /*@Test
    public void testAuthenticate_Success_User() {
        // Simular respuesta del backend
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("mock-token");
        loginResponse.setUserType(1); // User type 1

        ResponseEntity<LoginResponse> response = new ResponseEntity<>(loginResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(LoginResponse.class))).thenReturn(response);

        Authentication auth = new UsernamePasswordAuthenticationToken("user", "password");
        Authentication result = customAuthenticationProvider.authenticate(auth);

        // Validaciones
        assertNotNull(result);
        assertEquals("user", result.getName());
        assertEquals("password", result.getCredentials());
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        verify(tokenStore, times(1)).setToken("mock-token");
    }*/

    /*@Test
    public void testAuthenticate_Success_Admin() {
        // Simular respuesta del backend
        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("mock-token");
        loginResponse.setUserType(0); // Admin type 0

        ResponseEntity<LoginResponse> response = new ResponseEntity<>(loginResponse, HttpStatus.OK);
        when(restTemplate.postForEntity(anyString(), any(), eq(LoginResponse.class))).thenReturn(response);

        Authentication auth = new UsernamePasswordAuthenticationToken("admin", "password");
        Authentication result = customAuthenticationProvider.authenticate(auth);

        // Validaciones
        assertNotNull(result);
        assertEquals("admin", result.getName());
        assertEquals("password", result.getCredentials());
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(result.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
        verify(tokenStore, times(1)).setToken("mock-token");
    }*/

    /*@Test
    public void testAuthenticate_InvalidCredentials() {
        // Simular excepción de HttpClientErrorException con código 401
        when(restTemplate.postForEntity(anyString(), any(), eq(LoginResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.UNAUTHORIZED, "Invalid credentials"));

        Authentication auth = new UsernamePasswordAuthenticationToken("invalidUser", "wrongPassword");

        Exception exception = assertThrows(BadCredentialsException.class, () -> customAuthenticationProvider.authenticate(auth));
        assertEquals("Credenciales inválidas: Invalid credentials", exception.getMessage());
    }*/

    /*@Test
    public void testAuthenticate_BackendError() {
        // Simular excepción de HttpClientErrorException con código 500
        when(restTemplate.postForEntity(anyString(), any(), eq(LoginResponse.class)))
                .thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        Authentication auth = new UsernamePasswordAuthenticationToken("user", "password");

        Exception exception = assertThrows(BadCredentialsException.class, () -> customAuthenticationProvider.authenticate(auth));
        assertEquals("Error inesperado en la autenticación", exception.getMessage());
    }*/

    @Test
    public void testSupports_ValidAuthentication() {
        assertTrue(customAuthenticationProvider.supports(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testSupports_InvalidAuthentication() {
        assertFalse(customAuthenticationProvider.supports(Object.class));
    }

    @Test
    public void testClearAuthentication() {
        // Invocar clearAuthentication
        customAuthenticationProvider.clearAuthentication();

        // Verificar que clearToken fue llamado
        verify(tokenStore, times(1)).clearToken();
    }

}