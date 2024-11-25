package com.demo.web_recetas.exception;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TokenNotFoundExceptionTest {

    @Test
    void testConstructorWithMessage() {
        String errorMessage = "Token de autenticación no disponible";

        // Crear una instancia de la excepción
        TokenNotFoundException exception = new TokenNotFoundException(errorMessage);

        // Validar que el mensaje se ha asignado correctamente
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testConstructorWithNullMessage() {
        // Crear una instancia de la excepción con mensaje nulo
        TokenNotFoundException exception = new TokenNotFoundException(null);

        // Validar que el mensaje es nulo
        assertNull(exception.getMessage());
    }
}