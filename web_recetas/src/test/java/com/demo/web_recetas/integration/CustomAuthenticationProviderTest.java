package com.demo.web_recetas.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomAuthenticationProviderTest {

    private CustomAuthenticationProvider authProvider;
    private TokenStore tokenStore;
    private RestTemplate restTemplate;
    private static final String MOCK_BACKEND_URL = "http://mock-backend";

    @BeforeEach
    void setUp() throws Exception {
        // Inicializar mocks
        tokenStore = mock(TokenStore.class);
        restTemplate = mock(RestTemplate.class);

        // Crear el provider con el TokenStore mockeado
        authProvider = new CustomAuthenticationProvider(tokenStore);

        // Inyectar backendUrl usando reflection
        Field backendUrlField = CustomAuthenticationProvider.class.getDeclaredField("backendUrl");
        backendUrlField.setAccessible(true);
        backendUrlField.set(authProvider, MOCK_BACKEND_URL);
    }


    @Test
    @DisplayName("Verifica soporte para UsernamePasswordAuthenticationToken")
    void supports_ValidClass() {
        assertTrue(authProvider.supports(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    @DisplayName("Verifica no soporte para otras clases")
    void supports_InvalidClass() {
        assertFalse(authProvider.supports(Object.class));
    }

    @Test
    @DisplayName("Limpia autenticación correctamente")
    void clearAuthentication() {
        authProvider.clearAuthentication();
        verify(tokenStore, times(1)).clearToken();
    }

    @Test
    public void testSupports_ValidAuthentication() {
        assertTrue(authProvider.supports(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void testSupports_InvalidAuthentication() {
        assertFalse(authProvider.supports(Object.class));
    }

    @Test
    public void testClearAuthentication() {
        // Invocar clearAuthentication
        authProvider.clearAuthentication();

        // Verificar que clearToken fue llamado
        verify(tokenStore, times(1)).clearToken();
    }
}