package com.demo.backend_recetas.service;

import com.demo.backend_recetas.model.Comentario;
import com.demo.backend_recetas.model.Receta;
import com.demo.backend_recetas.repository.ComentarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentarioServiceTest {

    @Mock
    private ComentarioRepository comentarioRepository;

    @InjectMocks
    private ComentarioService comentarioService;

    private Comentario comentario;
    private Receta receta;

    @BeforeEach
    void setUp() {
        receta = new Receta();
        comentario = new Comentario("usuario_test", "comentario_test", 5, 1, receta);
        comentario.setId(1L);
    }

    @Test
    void listarTodos_DebeRetornarListaDeComentarios() {
        // Arrange
        List<Comentario> comentarios = Arrays.asList(comentario);
        when(comentarioRepository.findAll()).thenReturn(comentarios);

        // Act
        List<Comentario> resultado = comentarioService.listarTodos();

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        verify(comentarioRepository).findAll();
    }

    @Test
    void obtenerPorId_CuandoExisteId_DebeRetornarComentario() {
        // Arrange
        when(comentarioRepository.findById(1L)).thenReturn(Optional.of(comentario));

        // Act
        Optional<Comentario> resultado = comentarioService.obtenerPorId(1L);

        // Assert
        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
        verify(comentarioRepository).findById(1L);
    }

    @Test
    void listarPorEstado_DebeRetornarComentariosConEstadoEspecifico() {
        // Arrange
        List<Comentario> comentarios = Arrays.asList(comentario);
        when(comentarioRepository.findByEstado(1)).thenReturn(comentarios);

        // Act
        List<Comentario> resultado = comentarioService.listarPorEstado(1);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(1, resultado.get(0).getEstado());
        verify(comentarioRepository).findByEstado(1);
    }

    @Test
    void actualizarEstado_CuandoExisteComentario_DebeActualizarEstado() {
        // Arrange
        when(comentarioRepository.findById(1L)).thenReturn(Optional.of(comentario));
        when(comentarioRepository.save(any(Comentario.class))).thenReturn(comentario);

        // Act
        Comentario resultado = comentarioService.actualizarEstado(1L, 2);

        // Assert
        assertNotNull(resultado);
        assertEquals(2, resultado.getEstado());
        verify(comentarioRepository).findById(1L);
        verify(comentarioRepository).save(any(Comentario.class));
    }

    @Test
    void actualizarEstado_CuandoNoExisteComentario_DebeLanzarExcepcion() {
        // Arrange
        when(comentarioRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, 
            () -> comentarioService.actualizarEstado(1L, 2),
            "Debería lanzar IllegalArgumentException cuando el comentario no existe"
        );
        verify(comentarioRepository).findById(1L);
        verify(comentarioRepository, never()).save(any(Comentario.class));
    }

    @Test
    void eliminarComentario_DebeEliminarComentarioPorId() {
        // Arrange
        Long id = 1L;
        doNothing().when(comentarioRepository).deleteById(id);

        // Act
        comentarioService.eliminarComentario(id);

        // Assert
        verify(comentarioRepository).deleteById(id);
    }
}