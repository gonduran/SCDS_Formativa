package com.demo.backend_recetas.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RecetaDTOTest {

    @Test
    void constructor_CreaInstanciaCorrecta() {
        // Arrange
        Long id = 1L;
        String nombre = "Paella";
        String descripcion = "Receta tradicional";
        String imagen = "paella.jpg";
        String tiempoCoccion = "45 minutos";
        String dificultad = "Media";

        // Act
        RecetaDTO dto = new RecetaDTO(id, nombre, descripcion, 
            imagen, tiempoCoccion, dificultad);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(nombre, dto.getNombre());
        assertEquals(descripcion, dto.getDescripcion());
        assertEquals(imagen, dto.getImagen());
        assertEquals(tiempoCoccion, dto.getTiempoCoccion());
        assertEquals(dificultad, dto.getDificultad());
    }

    @Test
    void settersAndGetters_FuncionanCorrectamente() {
        // Arrange
        RecetaDTO dto = new RecetaDTO(1L, "", "", "", "", "");

        // Act & Assert
        Long nuevoId = 2L;
        dto.setId(nuevoId);
        assertEquals(nuevoId, dto.getId());

        String nuevoNombre = "Gazpacho";
        dto.setNombre(nuevoNombre);
        assertEquals(nuevoNombre, dto.getNombre());

        String nuevaDescripcion = "Sopa fría";
        dto.setDescripcion(nuevaDescripcion);
        assertEquals(nuevaDescripcion, dto.getDescripcion());

        String nuevaImagen = "gazpacho.jpg";
        dto.setImagen(nuevaImagen);
        assertEquals(nuevaImagen, dto.getImagen());

        String nuevoTiempoCoccion = "15 minutos";
        dto.setTiempoCoccion(nuevoTiempoCoccion);
        assertEquals(nuevoTiempoCoccion, dto.getTiempoCoccion());

        String nuevaDificultad = "Fácil";
        dto.setDificultad(nuevaDificultad);
        assertEquals(nuevaDificultad, dto.getDificultad());
    }

    @Test
    void constructor_ConValoresNulos_CreaInstancia() {
        // Arrange & Act
        RecetaDTO dto = new RecetaDTO(null, null, null, 
            null, null, null);

        // Assert
        assertNull(dto.getId());
        assertNull(dto.getNombre());
        assertNull(dto.getDescripcion());
        assertNull(dto.getImagen());
        assertNull(dto.getTiempoCoccion());
        assertNull(dto.getDificultad());
    }

    @Test
    void comparacion_DtosIguales_RetornaTrue() {
        // Arrange
        RecetaDTO dto1 = new RecetaDTO(1L, "Paella", "Descripción", 
            "imagen.jpg", "45 min", "Media");
        RecetaDTO dto2 = new RecetaDTO(1L, "Paella", "Descripción", 
            "imagen.jpg", "45 min", "Media");

        // Act & Assert
        assertEquals(dto1.getId(), dto2.getId());
        assertEquals(dto1.getNombre(), dto2.getNombre());
        assertEquals(dto1.getDescripcion(), dto2.getDescripcion());
        assertEquals(dto1.getImagen(), dto2.getImagen());
        assertEquals(dto1.getTiempoCoccion(), dto2.getTiempoCoccion());
        assertEquals(dto1.getDificultad(), dto2.getDificultad());
    }
}