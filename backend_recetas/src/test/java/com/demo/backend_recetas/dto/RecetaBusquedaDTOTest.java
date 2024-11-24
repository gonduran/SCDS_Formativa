package com.demo.backend_recetas.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RecetaBusquedaDTOTest {

    @Test
    void constructor_CreaInstanciaCorrecta() {
        // Arrange
        Long id = 1L;
        String nombre = "Paella";
        String descripcion = "Receta tradicional";
        String imagen = "paella.jpg";
        String tipoCocina = "Española";
        String paisOrigen = "España";
        String tiempoCoccion = "45 minutos";
        String dificultad = "Media";

        // Act
        RecetaBusquedaDTO dto = new RecetaBusquedaDTO(id, nombre, descripcion, 
            imagen, tipoCocina, paisOrigen, tiempoCoccion, dificultad);

        // Assert
        assertEquals(id, dto.getId());
        assertEquals(nombre, dto.getNombre());
        assertEquals(descripcion, dto.getDescripcion());
        assertEquals(imagen, dto.getImagen());
        assertEquals(tipoCocina, dto.getTipoCocina());
        assertEquals(paisOrigen, dto.getPaisOrigen());
        assertEquals(tiempoCoccion, dto.getTiempoCoccion());
        assertEquals(dificultad, dto.getDificultad());
    }

    @Test
    void settersAndGetters_FuncionanCorrectamente() {
        // Arrange
        RecetaBusquedaDTO dto = new RecetaBusquedaDTO(1L, "", "", "", "", "", "", "");

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

        String nuevoTipoCocina = "Mediterránea";
        dto.setTipoCocina(nuevoTipoCocina);
        assertEquals(nuevoTipoCocina, dto.getTipoCocina());

        String nuevoPaisOrigen = "España";
        dto.setPaisOrigen(nuevoPaisOrigen);
        assertEquals(nuevoPaisOrigen, dto.getPaisOrigen());

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
        RecetaBusquedaDTO dto = new RecetaBusquedaDTO(null, null, null, 
            null, null, null, null, null);

        // Assert
        assertNull(dto.getId());
        assertNull(dto.getNombre());
        assertNull(dto.getDescripcion());
        assertNull(dto.getImagen());
        assertNull(dto.getTipoCocina());
        assertNull(dto.getPaisOrigen());
        assertNull(dto.getTiempoCoccion());
        assertNull(dto.getDificultad());
    }
}