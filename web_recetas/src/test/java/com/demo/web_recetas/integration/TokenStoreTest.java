package com.demo.web_recetas.integration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TokenStoreTest {

    @Test
    void testSetAndGetToken() {
        TokenStore tokenStore = new TokenStore();
        
        // Validar que inicialmente el token es nulo
        assertNull(tokenStore.getToken(), "El token debería ser nulo inicialmente");
        
        // Establecer un token
        String testToken = "test-token";
        tokenStore.setToken(testToken);

        // Validar que el token establecido se devuelve correctamente
        assertEquals(testToken, tokenStore.getToken(), "El token debería coincidir con el valor establecido");
    }

    @Test
    void testSetTokenToNull() {
        TokenStore tokenStore = new TokenStore();

        // Establecer un token inicial
        String testToken = "test-token";
        tokenStore.setToken(testToken);

        // Validar que el token es igual al valor establecido
        assertEquals(testToken, tokenStore.getToken(), "El token debería coincidir con el valor establecido");

        // Establecer el token a null
        tokenStore.setToken(null);

        // Validar que el token ahora es nulo
        assertNull(tokenStore.getToken(), "El token debería ser nulo después de establecerlo como null");
    }

    @Test
    void testClearToken() {
        TokenStore tokenStore = new TokenStore();
    
        // Establecer un token inicial
        String testToken = "test-token";
        tokenStore.setToken(testToken);
    
        // Validar que el token es igual al valor establecido
        assertEquals(testToken, tokenStore.getToken(), "El token debería coincidir con el valor establecido");
    
        // Limpiar el token
        tokenStore.clearToken();
    
        // Validar que el token ahora es nulo
        assertNull(tokenStore.getToken(), "El token debería ser nulo después de llamar a clearToken");
    }
    
}