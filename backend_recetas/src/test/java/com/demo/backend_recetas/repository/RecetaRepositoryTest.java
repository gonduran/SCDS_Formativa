package com.demo.backend_recetas.repository;

import com.demo.backend_recetas.model.Receta;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class RecetaRepositoryTest {

    @Autowired
    private RecetaRepository recetaRepository;

    @Test
    @DisplayName("Búsqueda por keyword encuentra receta por nombre")
    void buscarPorKeyword_PorNombre() {
        // Crear receta
        Receta receta = new Receta();
        receta.setNombre("Paella Valenciana");
        receta.setDescripcion("Plato típico español");
        receta.setDetallePreparacion("Preparación detallada...");
        receta.setTipoCocina("Española");
        receta.setPaisOrigen("España");
        receta.setDificultad("Media");
        receta.setTiempoCoccion("1 hora");
        receta.setIngredientes(Arrays.asList("arroz", "azafrán", "mariscos"));
        
        // Guardar receta
        recetaRepository.save(receta);

        // Buscar por keyword
        List<Receta> resultado = recetaRepository.buscarPorKeyword("Paella");
        
        // Verificar
        assertFalse(resultado.isEmpty());
        assertEquals("Paella Valenciana", resultado.get(0).getNombre());
    }

    @Test
    @DisplayName("Búsqueda por keyword encuentra receta por ingrediente")
    void buscarPorKeyword_PorIngrediente() {
        // Crear receta
        Receta receta = new Receta();
        receta.setNombre("Paella");
        receta.setIngredientes(Arrays.asList("arroz", "azafrán", "mariscos"));
        
        // Guardar receta
        recetaRepository.save(receta);

        // Buscar por ingrediente
        List<Receta> resultado = recetaRepository.buscarPorKeyword("azafrán");
        
        // Verificar
        assertFalse(resultado.isEmpty());
        assertTrue(resultado.get(0).getIngredientes().contains("azafrán"));
    }

    @Test
    @DisplayName("Búsqueda por keyword encuentra receta por tipo de cocina")
    void buscarPorKeyword_PorTipoCocina() {
        // Crear receta
        Receta receta = new Receta();
        receta.setNombre("Paella");
        receta.setTipoCocina("Española");
        receta.setIngredientes(Arrays.asList("arroz"));
        
        // Guardar receta
        recetaRepository.save(receta);

        // Buscar por tipo de cocina
        List<Receta> resultado = recetaRepository.buscarPorKeyword("Española");
        
        // Verificar
        assertFalse(resultado.isEmpty());
        assertEquals("Española", resultado.get(0).getTipoCocina());
    }

    @Test
    @DisplayName("Búsqueda por keyword retorna lista vacía cuando no encuentra coincidencias")
    void buscarPorKeyword_NoEncontrado() {
        // Crear receta
        Receta receta = new Receta();
        receta.setNombre("Paella");
        receta.setTipoCocina("Española");
        receta.setIngredientes(Arrays.asList("arroz"));
        
        // Guardar receta
        recetaRepository.save(receta);

        // Buscar algo que no existe
        List<Receta> resultado = recetaRepository.buscarPorKeyword("Sushi");
        
        // Verificar
        assertTrue(resultado.isEmpty());
    }

    @Test
    @DisplayName("Búsqueda por keyword es insensible a mayúsculas/minúsculas")
    void buscarPorKeyword_CaseInsensitive() {
        // Crear receta
        Receta receta = new Receta();
        receta.setNombre("Paella Valenciana");
        receta.setTipoCocina("Española");
        receta.setIngredientes(Arrays.asList("arroz"));
        
        // Guardar receta
        recetaRepository.save(receta);

        // Buscar con diferente capitalización
        List<Receta> resultado = recetaRepository.buscarPorKeyword("paella");
        
        // Verificar
        assertFalse(resultado.isEmpty());
        assertEquals("Paella Valenciana", resultado.get(0).getNombre());
    }
}