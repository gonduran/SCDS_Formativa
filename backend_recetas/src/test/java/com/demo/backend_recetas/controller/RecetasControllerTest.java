package com.demo.backend_recetas.controller;

import com.demo.backend_recetas.dto.RecetaBusquedaDTO;
import com.demo.backend_recetas.dto.RecetaDTO;
import com.demo.backend_recetas.model.Receta;
import com.demo.backend_recetas.repository.RecetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RecetasControllerTest {

    @Mock
    private RecetaRepository recetaRepository;

    @InjectMocks
    private RecetasController recetasController;

    private Receta testReceta1;
    private Receta testReceta2;

    @BeforeEach
    void setUp() {
        testReceta1 = new Receta();
        testReceta1.setId(1L);
        testReceta1.setNombre("Paella Valenciana");
        testReceta1.setDescripcion("Deliciosa receta tradicional española.");
        testReceta1.setTiempoCoccion("45 minutos");
        testReceta1.setDificultad("Media");

        testReceta2 = new Receta();
        testReceta2.setId(2L);
        testReceta2.setNombre("Gazpacho");
        testReceta2.setDescripcion("Sopa fría andaluza.");
        testReceta2.setTiempoCoccion("15 minutos");
        testReceta2.setDificultad("Fácil");
    }

    @Test
    void obtenerRecetas_Success() {
        when(recetaRepository.findAll()).thenReturn(Arrays.asList(testReceta1, testReceta2));

        ResponseEntity<List<RecetaDTO>> response = recetasController.obtenerRecetas();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
        assertEquals("Paella Valenciana", response.getBody().get(0).getNombre());
        verify(recetaRepository).findAll();
    }

    @Test
    void buscarRecetas_KeywordEncontrado() {
        when(recetaRepository.buscarPorKeyword("Paella")).thenReturn(Arrays.asList(testReceta1));

        ResponseEntity<List<RecetaBusquedaDTO>> response = recetasController.buscarRecetas("Paella");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Paella Valenciana", response.getBody().get(0).getNombre());
        verify(recetaRepository).buscarPorKeyword("Paella");
    }

    @Test
    void buscarRecetas_KeywordNoEncontrado() {
        when(recetaRepository.buscarPorKeyword("Sushi")).thenReturn(Arrays.asList());

        ResponseEntity<List<RecetaBusquedaDTO>> response = recetasController.buscarRecetas("Sushi");

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().isEmpty());
        verify(recetaRepository).buscarPorKeyword("Sushi");
    }

    @Test
    void obtenerRecetaDetalle_RecetaExistente() {
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(testReceta1));

        ResponseEntity<?> response = recetasController.obtenerRecetaDetalle(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof Receta);
        assertEquals("Paella Valenciana", ((Receta) response.getBody()).getNombre());
        verify(recetaRepository).findById(1L);
    }

    @Test
    void obtenerRecetaDetalle_RecetaNoExistente() {
        when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

        ResponseEntity<?> response = recetasController.obtenerRecetaDetalle(999L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(recetaRepository).findById(999L);
    }
}
