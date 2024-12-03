package com.demo.backend_recetas.security;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.security.Key;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.WeakKeyException;
import static org.junit.jupiter.api.Assertions.*;

public class ConstantsTest {

    private static final String TEST_SECRET_KEY = "R0FWYk16aXJIemVTU1puRmRXbEJPREtNVWVKS3VqWHFjdHk1cWJYTXZLRjZBUXdDTmdIeUxxWURKU2xF";

    @Test
    @DisplayName("Prueba creación de instancia de Constants")
    void testConstructor() {
        Constants constants = new Constants();
        assertNotNull(constants);
    }

    @Test
    @DisplayName("Prueba generación de Key con Base64 válido")
    void getSigningKeyB64_GeneraKeyCorrecta() {
        // Arrange & Act
        Key key = Constants.getSigningKeyB64(Constants.SUPER_SECRET_KEY);

        // Assert
        assertNotNull(key);
        assertTrue(key.getAlgorithm().startsWith("Hmac"));
    }

    @Test
    @DisplayName("Prueba getSigningKeyB64 con entrada inválida")
    void getSigningKeyB64_ConEntradaInvalida() {
        // Arrange
        String invalidBase64 = "!!!InvalidBase64!!!";
    
        // Act & Assert
        assertThrows(io.jsonwebtoken.security.WeakKeyException.class, () -> {
            Constants.getSigningKeyB64(invalidBase64);
        });
    }
    
    @Test
    @DisplayName("Prueba getSigningKeyB64 con string vacío")
    void getSigningKeyB64_ConStringVacio() {
        // Act & Assert
        assertThrows(io.jsonwebtoken.security.WeakKeyException.class, () -> {
            Constants.getSigningKeyB64("");
        });
    }
    
    @Test
    @DisplayName("Prueba getSigningKeyB64 con null")
    void getSigningKeyB64_ConNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            Constants.getSigningKeyB64(null);
        });
    }

    @Test
    @DisplayName("Prueba generación de Key con string normal")
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
    @DisplayName("Prueba getSigningKey con string corto")
    void getSigningKey_ConStringCorto() {
        // Arrange
        String shortKey = "corta";

        // Act & Assert
        assertThrows(WeakKeyException.class, () -> {
            Constants.getSigningKey(shortKey);
        });
    }

    @Test
    @DisplayName("Prueba getSigningKey con string vacío")
    void getSigningKey_ConStringVacio() {
        // Act & Assert
        assertThrows(WeakKeyException.class, () -> {
            Constants.getSigningKey("");
        });
    }

    @Test
    @DisplayName("Prueba getSigningKey con null")
    void getSigningKey_ConNull() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> {
            Constants.getSigningKey(null);
        });
    }

    @Test
    @DisplayName("Verificación de valores de constantes")
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
    @DisplayName("Verificación de requisitos de seguridad de la clave secreta")
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

    @Test
    @DisplayName("Verificación del formato del token Bearer")
    void tokenBearerPrefix_TieneFormatoCorrecto() {
        assertTrue(Constants.TOKEN_BEARER_PREFIX.endsWith(" "), 
            "TOKEN_BEARER_PREFIX debe terminar con un espacio");
        assertTrue(Constants.TOKEN_BEARER_PREFIX.startsWith("Bearer"), 
            "TOKEN_BEARER_PREFIX debe comenzar con 'Bearer'");
    }
}