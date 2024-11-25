package com.demo.backend_recetas.service;

import com.demo.backend_recetas.model.Receta;
import com.demo.backend_recetas.repository.RecetaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class) // Usamos esto en lugar de @SpringBootTest
public class RecetaServiceTest {

    @Mock
    private RecetaRepository recetaRepository;

    @InjectMocks
    private RecetaService recetaService;

    private Receta recetaTest;

    @BeforeEach
    void setUp() {
        // Crear una receta para pruebas
        recetaTest = new Receta();
        recetaTest.setId(1L);
        recetaTest.setNombre("Pastel de Chocolate");
        recetaTest.setDescripcion("Un delicioso pastel de chocolate");
        recetaTest.setDificultad("Media");
        recetaTest.setTiempoCoccion("45 minutos");
    }

    @Test
    void guardarReceta_Success() {
        // Preparar
        when(recetaRepository.save(any(Receta.class))).thenReturn(recetaTest);

        // Ejecutar
        Receta recetaGuardada = recetaService.guardarReceta(recetaTest);

        // Verificar
        assertNotNull(recetaGuardada);
        assertEquals("Pastel de Chocolate", recetaGuardada.getNombre());
        assertEquals("Un delicioso pastel de chocolate", recetaGuardada.getDescripcion());
        assertEquals("Media", recetaGuardada.getDificultad());
        assertEquals("45 minutos", recetaGuardada.getTiempoCoccion());
    }

    @Test
    void obtenerRecetaPorId_Success() {
        // Preparar
        when(recetaRepository.findById(1L)).thenReturn(Optional.of(recetaTest));

        // Ejecutar
        Receta recetaEncontrada = recetaService.obtenerRecetaPorId(1L);

        // Verificar
        assertNotNull(recetaEncontrada);
        assertEquals(1L, recetaEncontrada.getId());
        assertEquals("Pastel de Chocolate", recetaEncontrada.getNombre());
    }

    @Test
    void obtenerRecetaPorId_NotFound() {
        // Preparar
        when(recetaRepository.findById(999L)).thenReturn(Optional.empty());

        // Ejecutar
        Receta recetaNoEncontrada = recetaService.obtenerRecetaPorId(999L);

        // Verificar
        assertNull(recetaNoEncontrada);
    }
}