package com.demo.web_recetas.service;

import com.demo.web_recetas.exception.TokenNotFoundException;
import com.demo.web_recetas.integration.TokenStore;
import com.demo.web_recetas.model.Comentario;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ComentariosServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TokenStore tokenStore;

    @InjectMocks
    private ComentariosService comentariosService;

    private static final String BACKEND_URL = "http://localhost:8080";
    private static final String TOKEN_VALIDO = "token-valido";
    private Comentario comentario1;
    private Comentario comentario2;

    @BeforeEach
    void setUp() {
        comentariosService.setBackendUrl(BACKEND_URL);
        
        // Configurar comentarios de prueba
        comentario1 = new Comentario();
        comentario1.setId(1L);
        comentario1.setComentario("Comentario 1");
        comentario1.setEstado(0);

        comentario2 = new Comentario();
        comentario2.setId(2L);
        comentario2.setComentario("Comentario 2");
        comentario2.setEstado(1);

        // Configurar token por defecto
        when(tokenStore.getToken()).thenReturn(TOKEN_VALIDO);
    }

    @Test
    @DisplayName("Listar todos los comentarios exitosamente")
    void listarTodos_Success() {
        // Arrange
        Comentario[] comentarios = {comentario1, comentario2};
        when(restTemplate.exchange(
            eq(BACKEND_URL + "/api/admin/comentarios"),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Comentario[].class)
        )).thenReturn(new ResponseEntity<>(comentarios, HttpStatus.OK));

        // Act
        List<Comentario> result = comentariosService.listarTodos();

        // Assert
        assertEquals(2, result.size());
        assertEquals(comentario1.getId(), result.get(0).getId());
        assertEquals(comentario2.getId(), result.get(1).getId());
    }

    @Test
    @DisplayName("Error al listar comentarios - Token no disponible")
    void listarTodos_TokenNotFound() {
        // Arrange
        when(tokenStore.getToken()).thenReturn(null);

        // Act & Assert
        assertThrows(TokenNotFoundException.class, () -> comentariosService.listarTodos());
    }

    @Test
    @DisplayName("Listar comentarios por estado exitosamente")
    void listarPorEstado_Success() {
        // Arrange
        Comentario[] comentarios = {comentario1};
        when(restTemplate.exchange(
            eq(BACKEND_URL + "/api/admin/comentarios/estado/0"),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Comentario[].class)
        )).thenReturn(new ResponseEntity<>(comentarios, HttpStatus.OK));

        // Act
        List<Comentario> result = comentariosService.listarPorEstado(0);

        // Assert
        assertEquals(1, result.size());
        assertEquals(0, result.get(0).getEstado());
    }

    @Test
    @DisplayName("Obtener comentario por ID exitosamente")
    void obtenerPorId_Success() {
        // Arrange
        when(restTemplate.exchange(
            eq(BACKEND_URL + "/api/admin/comentarios/1"),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Comentario.class)
        )).thenReturn(new ResponseEntity<>(comentario1, HttpStatus.OK));

        // Act
        Optional<Comentario> result = comentariosService.obtenerPorId(1L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(comentario1.getId(), result.get().getId());
    }

    @Test
    @DisplayName("Comentario no encontrado por ID")
    void obtenerPorId_NotFound() {
        // Arrange
        when(restTemplate.exchange(
            anyString(),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(Comentario.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.NOT_FOUND));

        // Act & Assert
        assertThrows(HttpClientErrorException.class, () -> comentariosService.obtenerPorId(999L));
    }

    @Test
    @DisplayName("Actualizar estado de comentario exitosamente")
    void actualizarEstadoComentario_Success() {
        // Arrange
        when(restTemplate.exchange(
            eq(BACKEND_URL + "/api/admin/comentarios/1/estado"),
            eq(HttpMethod.PUT),
            any(HttpEntity.class),
            eq(Void.class)
        )).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act & Assert
        assertDoesNotThrow(() -> comentariosService.actualizarEstadoComentario(1L, 1));
    }

    @Test
    @DisplayName("Eliminar comentario exitosamente")
    void eliminarComentario_Success() {
        // Arrange
        when(restTemplate.exchange(
            eq(BACKEND_URL + "/api/admin/comentarios/1"),
            eq(HttpMethod.DELETE),
            any(HttpEntity.class),
            eq(Void.class)
        )).thenReturn(new ResponseEntity<>(HttpStatus.OK));

        // Act & Assert
        assertDoesNotThrow(() -> comentariosService.eliminarComentario(1L));
    }

    @Test
    @DisplayName("Error al eliminar comentario - Token vacío")
    void eliminarComentario_EmptyToken() {
        // Arrange
        when(tokenStore.getToken()).thenReturn("");

        // Act & Assert
        assertThrows(TokenNotFoundException.class, () -> comentariosService.eliminarComentario(1L));
    }

    @Test
    @DisplayName("Error al actualizar estado - Token inválido")
    void actualizarEstadoComentario_InvalidToken() {
        // Arrange
        when(tokenStore.getToken()).thenReturn("");

        // Act & Assert
        assertThrows(TokenNotFoundException.class, 
            () -> comentariosService.actualizarEstadoComentario(1L, 1));
    }

    @Test
    @DisplayName("Error al listar por estado - Error de conexión")
    void listarPorEstado_ConnectionError() {
        // Arrange
        when(restTemplate.exchange(
            anyString(),
            any(HttpMethod.class),
            any(HttpEntity.class),
            eq(Comentario[].class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.INTERNAL_SERVER_ERROR));

        // Act & Assert
        assertThrows(HttpClientErrorException.class, 
            () -> comentariosService.listarPorEstado(0));
    }
}