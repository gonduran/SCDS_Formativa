package com.demo.backend_recetas.controller;

import com.demo.backend_recetas.model.Receta;
import com.demo.backend_recetas.model.Comentario;
import com.demo.backend_recetas.service.RecetaService;
import com.demo.backend_recetas.dto.MediaRequestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class RecetaControllerTest {

    @Mock
    private RecetaService recetaService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private RecetaController recetaController;

    private Receta testReceta;
    private MediaRequestDTO testMediaRequest;
    private Comentario testComentario;

    @BeforeEach
    void setUp() {
        testReceta = new Receta();
        testReceta.setId(1L);
        testReceta.setNombre("Paella");
        testReceta.setDescripcion("Receta de paella");
        testReceta.setComentarios(new ArrayList<>());
        testReceta.setFotos(new ArrayList<>());
        testReceta.setVideos(new ArrayList<>());

        List<String> fotos = Arrays.asList("foto1.jpg", "foto2.jpg");
        List<String> videos = Arrays.asList("video1.mp4");
        testMediaRequest = new MediaRequestDTO(fotos, videos);

        testComentario = new Comentario("testuser", "Muy buena receta", 5, 0, testReceta);
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Publicar receta se realiza exitosamente")
    void publicarReceta_Success() {
        when(recetaService.guardarReceta(any(Receta.class))).thenReturn(testReceta);

        ResponseEntity<String> response = recetaController.publicarReceta(testReceta);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Receta publicada exitosamente.", response.getBody());
        verify(recetaService).guardarReceta(any(Receta.class));
    }

    @Test
    @DisplayName("Agregar media a receta existente funciona correctamente")
    void agregarMedia_RecetaExistente() {
        when(recetaService.obtenerRecetaPorId(1L)).thenReturn(testReceta);
        when(recetaService.guardarReceta(any(Receta.class))).thenReturn(testReceta);

        ResponseEntity<String> response = recetaController.agregarMedia(1L, testMediaRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Media agregada exitosamente.", response.getBody());
        verify(recetaService).guardarReceta(testReceta);
    }

    @Test
    @DisplayName("Agregar media a receta inexistente retorna error")
    void agregarMedia_RecetaNoExistente() {
        when(recetaService.obtenerRecetaPorId(999L)).thenReturn(null);

        ResponseEntity<String> response = recetaController.agregarMedia(999L, testMediaRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Receta no encontrada.", response.getBody());
        verify(recetaService, never()).guardarReceta(any(Receta.class));
    }

    @Test
    @DisplayName("Agregar comentario se realiza exitosamente")
    void agregarComentario_Success() {
        // Configurar seguridad
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");

        when(recetaService.obtenerRecetaPorId(1L)).thenReturn(testReceta);
        when(recetaService.guardarReceta(any(Receta.class))).thenReturn(testReceta);

        ResponseEntity<String> response = recetaController.agregarComentario(1L, testComentario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Comentario agregado exitosamente"));
        verify(recetaService).guardarReceta(testReceta);
    }

    @Test
    @DisplayName("Agregar comentario a receta inexistente retorna error")
    void agregarComentario_RecetaNoExistente() {
        when(recetaService.obtenerRecetaPorId(999L)).thenReturn(null);

        ResponseEntity<String> response = recetaController.agregarComentario(999L, testComentario);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Receta no encontrada.", response.getBody());
        verify(recetaService, never()).guardarReceta(any(Receta.class));
    }

    @Test
    @DisplayName("Calcular promedio de valoraciones funciona correctamente")
    void agregarComentario_CalculaPromedioCorrectamente() {
        // Configurar seguridad
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");

        testReceta.setComentarios(new ArrayList<>());
        testReceta.getComentarios().add(createComentario(4));
        
        when(recetaService.obtenerRecetaPorId(1L)).thenReturn(testReceta);
        when(recetaService.guardarReceta(any(Receta.class))).thenReturn(testReceta);

        testComentario.setValoracion(5);
        ResponseEntity<String> response = recetaController.agregarComentario(1L, testComentario);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().contains("Comentario agregado exitosamente"));
        assertEquals(4.5, testReceta.getValoracionPromedio(), 0.01);
        verify(recetaService).guardarReceta(testReceta);
    }

    @Test
    @DisplayName("Manejo de excepción al agregar comentario")
    void agregarComentario_ManejaExcepcion() {
        // Configurar seguridad
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");

        // Simular que recetaService lanza una excepción
        when(recetaService.obtenerRecetaPorId(1L)).thenReturn(testReceta);
        when(recetaService.guardarReceta(any(Receta.class)))
            .thenThrow(new RuntimeException("Error simulado"));

        // Act
        ResponseEntity<String> response = recetaController.agregarComentario(1L, testComentario);

        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Error al agregar el comentario"));
    }    

    @Test
    @DisplayName("Agregar comentario inicializa lista si es null")
    void agregarComentario_InicializaListaComentarios() {
        // Configurar seguridad
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");

        // Crear receta sin lista de comentarios
        testReceta.setComentarios(null);
        
        when(recetaService.obtenerRecetaPorId(1L)).thenReturn(testReceta);
        when(recetaService.guardarReceta(any(Receta.class))).thenReturn(testReceta);

        // Act
        ResponseEntity<String> response = recetaController.agregarComentario(1L, testComentario);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(testReceta.getComentarios());
        assertEquals(1, testReceta.getComentarios().size());
        verify(recetaService).guardarReceta(testReceta);
    }    

    private Comentario createComentario(int valoracion) {
        return new Comentario("testuser", "Comentario de prueba", valoracion, 0, testReceta);
    }
}