package com.demo.web_recetas.controller;

import com.demo.web_recetas.integration.TokenStore;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(BuscarController.class)
public class BuscarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenStore tokenStore;

    @MockBean
    private RecetasService recetasService;

    private Receta recetaMock1;
    private Receta recetaMock2;

    @BeforeEach
    public void setUp() {
        List<String> ingredientes = Collections.singletonList("Ingrediente de prueba");

        recetaMock1 = new Receta(1L, "Tarta de Manzana", "Deliciosa tarta con manzanas frescas", "Postres",
                ingredientes, "España", "Pasos de preparación", "imagen1.jpg", "30 minutos",
                "Fácil", null, null, null, 4.5);

        recetaMock2 = new Receta(2L, "Paella", "Arroz con mariscos tradicional", "Plato principal",
                ingredientes, "España", "Pasos de preparación", "imagen2.jpg", "1 hora",
                "Media", null, null, null, 4.7);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testBuscarRecetasConResultados() throws Exception {
        // Configurar mock del servicio para devolver recetas
        when(recetasService.buscarRecetas("Tarta")).thenReturn(Arrays.asList(recetaMock1));

        // Realizar la petición y verificar el modelo y la vista
        mockMvc.perform(get("/buscar").param("searchQuery", "Tarta"))
                .andExpect(status().isOk())
                .andExpect(view().name("buscar"))
                .andExpect(model().attributeExists("recetasFiltradas", "searchQuery"))
                .andExpect(model().attribute("recetasFiltradas", Arrays.asList(recetaMock1)))
                .andExpect(model().attribute("searchQuery", "Tarta"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testBuscarRecetasSinResultados() throws Exception {
        // Configurar mock del servicio para devolver una lista vacía
        when(recetasService.buscarRecetas("Inexistente")).thenReturn(Collections.emptyList());

        // Realizar la petición y verificar el modelo y la vista
        mockMvc.perform(get("/buscar").param("searchQuery", "Inexistente"))
                .andExpect(status().isOk())
                .andExpect(view().name("buscar"))
                .andExpect(model().attributeExists("recetasFiltradas", "searchQuery"))
                .andExpect(model().attribute("recetasFiltradas", Collections.emptyList()))
                .andExpect(model().attribute("searchQuery", "Inexistente"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testBuscarRecetasConBusquedaInvalida() throws Exception {
        // Realizar la petición con un patrón inválido y verificar el modelo y la vista
        mockMvc.perform(get("/buscar").param("searchQuery", "<script>alert('XSS')</script>"))
                .andExpect(status().isOk())
                .andExpect(view().name("buscar"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Solo se permiten letras, números y espacios en la búsqueda."));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testBuscarRecetasSinCriterioDeBusqueda() throws Exception {
        // Realizar la petición sin un criterio de búsqueda
        mockMvc.perform(get("/buscar"))
                .andExpect(status().isOk())
                .andExpect(view().name("buscar"))
                .andExpect(model().attributeExists("message"))
                .andExpect(model().attribute("message", "Por favor, ingresa un criterio de búsqueda."))
                .andExpect(model().attributeDoesNotExist("recetasFiltradas"));
    }
}
