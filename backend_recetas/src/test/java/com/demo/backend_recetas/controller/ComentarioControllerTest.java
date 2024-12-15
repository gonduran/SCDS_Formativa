package com.demo.backend_recetas.controller;

import com.demo.backend_recetas.dto.EstadoComentarioDTO;
import com.demo.backend_recetas.model.Comentario;
import com.demo.backend_recetas.model.Receta;
import com.demo.backend_recetas.service.ComentarioService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioControllerTest {

    @Mock
    private ComentarioService comentarioService;

    @InjectMocks
    private ComentarioController comentarioController;

    private Comentario comentario;
    private EstadoComentarioDTO estadoDTO;
    private Receta receta;

    @BeforeEach
    void setUp() {
        receta = new Receta();
        comentario = new Comentario("usuario_test", "comentario_test", 5, 1, receta);
        comentario.setId(1L);

        estadoDTO = new EstadoComentarioDTO();
        estadoDTO.setEstado(2);
    }

    @Test
    void listarTodos_DebeRetornarListaDeComentarios() {
        // Arrange
        List<Comentario> comentarios = Arrays.asList(comentario);
        when(comentarioService.listarTodos()).thenReturn(comentarios);

        // Act
        List<Comentario> resultado = comentarioController.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(comentarioService).listarTodos();
    }

    @Test
    void obtenerPorId_CuandoExisteId_DebeRetornarComentario() {
        // Arrange
        when(comentarioService.obtenerPorId(1L)).thenReturn(Optional.of(comentario));

        // Act
        ResponseEntity<Comentario> resultado = comentarioController.obtenerPorId(1L);

        // Assert
        assertTrue(resultado.getStatusCode().is2xxSuccessful());
        assertEquals(comentario, resultado.getBody());
        verify(comentarioService).obtenerPorId(1L);
    }

    @Test
    void obtenerPorId_CuandoNoExisteId_DebeRetornarNotFound() {
        // Arrange
        when(comentarioService.obtenerPorId(1L)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<Comentario> resultado = comentarioController.obtenerPorId(1L);

        // Assert
        assertTrue(resultado.getStatusCode().is4xxClientError());
        verify(comentarioService).obtenerPorId(1L);
    }

    @Test
    void listarPorEstado_DebeRetornarComentariosConEstadoEspecifico() {
        // Arrange
        List<Comentario> comentarios = Arrays.asList(comentario);
        when(comentarioService.listarPorEstado(1)).thenReturn(comentarios);

        // Act
        List<Comentario> resultado = comentarioController.listarPorEstado(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(comentarioService).listarPorEstado(1);
    }

    @Test
    void actualizarEstado_CuandoExisteComentario_DebeActualizarEstado() {
        // Arrange
        when(comentarioService.actualizarEstado(1L, 2)).thenReturn(comentario);

        // Act
        ResponseEntity<Comentario> resultado = comentarioController.actualizarEstado(1L, estadoDTO);

        // Assert
        assertTrue(resultado.getStatusCode().is2xxSuccessful());
        assertEquals(comentario, resultado.getBody());
        verify(comentarioService).actualizarEstado(1L, 2);
    }

    @Test
    void actualizarEstado_CuandoNoExisteComentario_DebeRetornarNotFound() {
        // Arrange
        when(comentarioService.actualizarEstado(1L, 2))
            .thenThrow(new IllegalArgumentException("Comentario no encontrado"));

        // Act
        ResponseEntity<Comentario> resultado = comentarioController.actualizarEstado(1L, estadoDTO);

        // Assert
        assertTrue(resultado.getStatusCode().is4xxClientError());
        verify(comentarioService).actualizarEstado(1L, 2);
    }

    @Test
    void eliminarComentario_DebeRetornarNoContent() {
        // Arrange
        doNothing().when(comentarioService).eliminarComentario(1L);

        // Act
        ResponseEntity<Void> resultado = comentarioController.eliminarComentario(1L);

        // Assert
        assertTrue(resultado.getStatusCode().is2xxSuccessful());
        verify(comentarioService).eliminarComentario(1L);
    }
}