package com.demo.backend_recetas.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;

public class ComentarioTest {

    @Test
    @DisplayName("Constructor inicializa todos los campos correctamente")
    void constructor_InitializesCorrectly() {
        // Arrange
        Receta receta = new Receta();
        String usuario = "testUser";
        String comentarioText = "Excelente receta";
        Integer valoracion = 5;

        // Act
        Comentario comentario = new Comentario(usuario, comentarioText, valoracion, receta);

        // Assert
        assertEquals(usuario, comentario.getUsuario());
        assertEquals(comentarioText, comentario.getComentario());
        assertEquals(valoracion, comentario.getValoracion());
        assertEquals(receta, comentario.getReceta());
    }

    @Test
    @DisplayName("Setters y Getters funcionan correctamente")
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        Receta receta = new Receta();
        Comentario comentario = new Comentario("user", "comentario", 4, receta);
        
        // Act
        Long id = 1L;
        String nuevoUsuario = "nuevoUser";
        String nuevoComentario = "nuevo comentario";
        Integer nuevaValoracion = 3;
        Receta nuevaReceta = new Receta();

        comentario.setId(id);
        comentario.setUsuario(nuevoUsuario);
        comentario.setComentario(nuevoComentario);
        comentario.setValoracion(nuevaValoracion);
        comentario.setReceta(nuevaReceta);

        // Assert
        assertEquals(id, comentario.getId());
        assertEquals(nuevoUsuario, comentario.getUsuario());
        assertEquals(nuevoComentario, comentario.getComentario());
        assertEquals(nuevaValoracion, comentario.getValoracion());
        assertEquals(nuevaReceta, comentario.getReceta());
    }
}