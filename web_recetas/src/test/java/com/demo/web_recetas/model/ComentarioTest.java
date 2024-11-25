package com.demo.web_recetas.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ComentarioTest {

    @Test
    void testConstructorVacio() {
        Comentario comentario = new Comentario();
        assertNull(comentario.getId());
        assertNull(comentario.getUsuario());
        assertNull(comentario.getComentario());
        assertNull(comentario.getValoracion());
    }

    @Test
    void testConstructorConParametros() {
        Long id = 1L;
        String usuario = "usuarioTest";
        String textoComentario = "Este es un comentario de prueba.";
        Integer valoracion = 5;

        Comentario comentario = new Comentario(id, usuario, textoComentario, valoracion);

        assertEquals(id, comentario.getId());
        assertEquals(usuario, comentario.getUsuario());
        assertEquals(textoComentario, comentario.getComentario());
        assertEquals(valoracion, comentario.getValoracion());
    }

    @Test
    void testSettersAndGetters() {
        Comentario comentario = new Comentario();

        Long id = 2L;
        String usuario = "nuevoUsuario";
        String textoComentario = "Actualización de comentario.";
        Integer valoracion = 4;

        comentario.setId(id);
        comentario.setUsuario(usuario);
        comentario.setComentario(textoComentario);
        comentario.setValoracion(valoracion);

        assertEquals(id, comentario.getId());
        assertEquals(usuario, comentario.getUsuario());
        assertEquals(textoComentario, comentario.getComentario());
        assertEquals(valoracion, comentario.getValoracion());
    }
}