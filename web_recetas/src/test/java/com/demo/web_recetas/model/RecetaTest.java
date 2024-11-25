package com.demo.web_recetas.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecetaTest {

    @Test
    void testConstructorVacio() {
        Receta receta = new Receta();
        assertNull(receta.getId());
        assertNull(receta.getNombre());
        assertNull(receta.getDescripcion());
        assertNull(receta.getTipoCocina());
        assertNull(receta.getIngredientes());
        assertNull(receta.getPaisOrigen());
        assertNull(receta.getDetallePreparacion());
        assertNull(receta.getImagen());
        assertNull(receta.getTiempoCoccion());
        assertNull(receta.getDificultad());
        assertNull(receta.getFotos());
        assertNull(receta.getVideos());
        assertNotNull(receta.getComentarios()); // Inicia como lista vacía
        assertNull(receta.getValoracionPromedio());
    }

    @Test
    void testConstructorConParametros() {
        List<String> ingredientes = Arrays.asList("Harina", "Azúcar", "Huevos");
        List<String> fotos = Arrays.asList("foto1.jpg", "foto2.jpg");
        List<String> videos = Arrays.asList("video1.mp4", "video2.mp4");
        List<Comentario> comentarios = new ArrayList<>();
        Double valoracionPromedio = 4.5;

        Receta receta = new Receta(1L, "Tarta", "Postre delicioso", "Postres", ingredientes,
                "España", "Mezclar y hornear", "imagen.jpg", "30 minutos",
                "Media", fotos, videos, comentarios, valoracionPromedio);

        assertEquals(1L, receta.getId());
        assertEquals("Tarta", receta.getNombre());
        assertEquals("Postre delicioso", receta.getDescripcion());
        assertEquals("Postres", receta.getTipoCocina());
        assertEquals(ingredientes, receta.getIngredientes());
        assertEquals("España", receta.getPaisOrigen());
        assertEquals("Mezclar y hornear", receta.getDetallePreparacion());
        assertEquals("imagen.jpg", receta.getImagen());
        assertEquals("30 minutos", receta.getTiempoCoccion());
        assertEquals("Media", receta.getDificultad());
        assertEquals(fotos, receta.getFotos());
        assertEquals(videos, receta.getVideos());
        assertEquals(comentarios, receta.getComentarios());
        assertEquals(valoracionPromedio, receta.getValoracionPromedio());
    }

    @Test
    void testSettersAndGetters() {
        Receta receta = new Receta();

        receta.setId(2L);
        receta.setNombre("Paella");
        receta.setDescripcion("Plato típico español");
        receta.setTipoCocina("Española");
        receta.setIngredientes(Arrays.asList("Arroz", "Mariscos", "Azafrán"));
        receta.setPaisOrigen("España");
        receta.setDetallePreparacion("Cocinar todo junto");
        receta.setImagen("paella.jpg");
        receta.setTiempoCoccion("45 minutos");
        receta.setDificultad("Alta");
        receta.setFotos(Arrays.asList("foto1.jpg", "foto2.jpg"));
        receta.setVideos(Arrays.asList("video1.mp4"));
        receta.setComentarios(new ArrayList<>());
        receta.setValoracionPromedio(4.8);

        assertEquals(2L, receta.getId());
        assertEquals("Paella", receta.getNombre());
        assertEquals("Plato típico español", receta.getDescripcion());
        assertEquals("Española", receta.getTipoCocina());
        assertEquals(Arrays.asList("Arroz", "Mariscos", "Azafrán"), receta.getIngredientes());
        assertEquals("España", receta.getPaisOrigen());
        assertEquals("Cocinar todo junto", receta.getDetallePreparacion());
        assertEquals("paella.jpg", receta.getImagen());
        assertEquals("45 minutos", receta.getTiempoCoccion());
        assertEquals("Alta", receta.getDificultad());
        assertEquals(Arrays.asList("foto1.jpg", "foto2.jpg"), receta.getFotos());
        assertEquals(Arrays.asList("video1.mp4"), receta.getVideos());
        assertEquals(new ArrayList<>(), receta.getComentarios());
        assertEquals(4.8, receta.getValoracionPromedio());
    }

    @Test
    void testComentariosSetterAndGetter() {
        Receta receta = new Receta();
        List<Comentario> comentarios = new ArrayList<>();

        Comentario comentario1 = new Comentario();
        comentario1.setComentario("Deliciosa receta");
        comentarios.add(comentario1);

        receta.setComentarios(comentarios);

        assertEquals(1, receta.getComentarios().size());
        assertEquals("Deliciosa receta", receta.getComentarios().get(0).getComentario());
    }
}
