package com.demo.backend_recetas.security;

import org.junit.jupiter.api.Test;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import static org.junit.jupiter.api.Assertions.*;

public class ConstantsTest {

    private static final String TEST_SECRET_KEY = "R0FWYk16aXJIemVTU1puRmRXbEJPREtNVWVKS3VqWHFjdHk1cWJYTXZLRjZBUXdDTmdIeUxxWURKU2xF";

    @Test
    void getSigningKeyB64_GeneraKeyCorrecta() {
        // Arrange & Act
        Key key = Constants.getSigningKeyB64(Constants.SUPER_SECRET_KEY);

        // Assert
        assertNotNull(key);
        assertTrue(key.getAlgorithm().startsWith("Hmac"));
    }

    @Test
    void getSigningKey_GeneraKeyCorrecta() {
        // Arrange
        // Usamos una clave lo suficientemente larga para HMAC-SHA256
        String secretKey = TEST_SECRET_KEY;
        
        // Act
        Key key = Constants.getSigningKey(secretKey);

        // Assert
        assertNotNull(key);
        assertTrue(key.getAlgorithm().startsWith("Hmac"));
    }

    @Test
    void constantes_TienenValoresCorrectos() {
        // Assert
        assertEquals("/login", Constants.LOGIN_URL);
        assertEquals("Authorization", Constants.HEADER_AUTHORIZACION_KEY);
        assertEquals("Bearer ", Constants.TOKEN_BEARER_PREFIX);
        assertEquals("https://www.duocuc.cl/", Constants.ISSUER_INFO);
        assertEquals(864_000_000, Constants.TOKEN_EXPIRATION_TIME);
        assertNotNull(Constants.SUPER_SECRET_KEY);
        assertTrue(Constants.SUPER_SECRET_KEY.length() >= 256 / 8); // Al menos 256 bits
    }

    @Test
    void claveSecreta_CumpleRequisitosSeguridad() {
        // Assert
        assertNotNull(Constants.SUPER_SECRET_KEY);
        byte[] keyBytes = Constants.SUPER_SECRET_KEY.getBytes();
        assertTrue(keyBytes.length >= 32, "La clave secreta debe tener al menos 256 bits (32 bytes)");
        
        // Verificar que se puede usar para crear una clave HMAC
        assertDoesNotThrow(() -> {
            Keys.hmacShaKeyFor(keyBytes);
        });
    }
}