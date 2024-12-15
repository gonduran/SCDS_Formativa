package com.demo.backend_recetas.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EstadoComentarioDTOTest {

    @Test
    void testGetterAndSetter() {
        // Arrange
        EstadoComentarioDTO dto = new EstadoComentarioDTO();
        Integer estadoEsperado = 1;

        // Act
        dto.setEstado(estadoEsperado);
        Integer estadoObtenido = dto.getEstado();

        // Assert
        assertEquals(estadoEsperado, estadoObtenido, "El estado devuelto debe coincidir con el estado establecido");
        assertNotNull(dto.getEstado(), "El estado no debería ser null después de establecerlo");
    }

    @Test
    void testConstructorPorDefecto() {
        // Act
        EstadoComentarioDTO dto = new EstadoComentarioDTO();

        // Assert
        assertNull(dto.getEstado(), "El estado debería ser null por defecto");
    }
}