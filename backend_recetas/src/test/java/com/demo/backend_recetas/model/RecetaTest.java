package com.demo.backend_recetas.model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class RecetaTest {

    @Test
    @DisplayName("Constructor vacío crea una instancia correcta")
    void constructorVacio_CreaInstanciaCorrecta() {
        Receta receta = new Receta();
        assertNotNull(receta);
        assertNotNull(receta.getComentarios()); // Debería inicializarse como ArrayList vacío
    }

    @Test
    @DisplayName("Constructor completo inicializa todos los campos correctamente")
    void constructorCompleto_CreaInstanciaCorrecta() {
        // Arrange
        Long id = 1L;
        String nombre = "Paella";
        String descripcion = "Receta tradicional";
        String tipoCocina = "Española";
        List<String> ingredientes = Arrays.asList("arroz", "azafrán");
        String paisOrigen = "España";
        String detallePreparacion = "Pasos detallados...";
        String imagen = "paella.jpg";
        String tiempoCoccion = "45 minutos";
        String dificultad = "Media";
        List<String> fotos = Arrays.asList("foto1.jpg", "foto2.jpg");
        List<String> videos = Arrays.asList("video1.mp4");
        List<Comentario> comentarios = new ArrayList<>();
        Double valoracionPromedio = 4.5;

        // Act
        Receta receta = new Receta(id, nombre, descripcion, tipoCocina,
                ingredientes, paisOrigen, detallePreparacion, imagen,
                tiempoCoccion, dificultad, fotos, videos,
                comentarios, valoracionPromedio);

        // Assert
        assertEquals(id, receta.getId());
        assertEquals(nombre, receta.getNombre());
        assertEquals(descripcion, receta.getDescripcion());
        assertEquals(tipoCocina, receta.getTipoCocina());
        assertEquals(ingredientes, receta.getIngredientes());
        assertEquals(paisOrigen, receta.getPaisOrigen());
        assertEquals(detallePreparacion, receta.getDetallePreparacion());
        assertEquals(imagen, receta.getImagen());
        assertEquals(tiempoCoccion, receta.getTiempoCoccion());
        assertEquals(dificultad, receta.getDificultad());
        assertEquals(fotos, receta.getFotos());
        assertEquals(videos, receta.getVideos());
        assertEquals(comentarios, receta.getComentarios());
        assertEquals(valoracionPromedio, receta.getValoracionPromedio());
    }

    @Test
    @DisplayName("Setters y Getters funcionan correctamente")
    void settersAndGetters_FuncionanCorrectamente() {
        // Arrange
        Receta receta = new Receta();
        
        // Act & Assert - Probamos cada setter y getter
        Long id = 1L;
        receta.setId(id);
        assertEquals(id, receta.getId());

        String nombre = "Paella";
        receta.setNombre(nombre);
        assertEquals(nombre, receta.getNombre());

        String descripcion = "Descripción";
        receta.setDescripcion(descripcion);
        assertEquals(descripcion, receta.getDescripcion());

        List<String> ingredientes = Arrays.asList("arroz", "azafrán");
        receta.setIngredientes(ingredientes);
        assertEquals(ingredientes, receta.getIngredientes());

        String paisOrigen = "España";
        receta.setPaisOrigen(paisOrigen);
        assertEquals(paisOrigen, receta.getPaisOrigen());

        Double valoracion = 4.5;
        receta.setValoracionPromedio(valoracion);
        assertEquals(valoracion, receta.getValoracionPromedio());
    }

    @Test
    @DisplayName("Manipulación de comentarios funciona correctamente")
    void comentarios_ManipulacionCorrecta() {
        // Arrange
        Receta receta = new Receta();
        Comentario comentario = new Comentario("usuario", "Buen plato", 5, 0, receta);
        List<Comentario> comentarios = new ArrayList<>();
        comentarios.add(comentario);

        // Act
        receta.setComentarios(comentarios);

        // Assert
        assertEquals(1, receta.getComentarios().size());
        assertEquals(comentario, receta.getComentarios().get(0));
    }

    @Test
    @DisplayName("SetImagen y GetImagen funcionan correctamente")
    void setImagen_GetImagen_FuncionanCorrectamente() {
        // Arrange
        Receta receta = new Receta();
        String imagen = "paella.jpg";

        // Act
        receta.setImagen(imagen);

        // Assert
        assertEquals(imagen, receta.getImagen());
    }
}