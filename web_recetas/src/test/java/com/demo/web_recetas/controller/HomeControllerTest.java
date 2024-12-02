package com.demo.web_recetas.controller;

import com.demo.web_recetas.integration.TokenStore;
import com.demo.web_recetas.model.Comentario;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(HomeController.class)
public class HomeControllerTest {

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
        List<String> fotos = Collections.singletonList("foto.jpg");
        List<String> videos = Collections.singletonList("video.mp4");
        List<Comentario> comentarios = new ArrayList<>();

        recetaMock1 = new Receta(1L, "Tarta de Manzana", "Deliciosa tarta con manzanas frescas", "Postres", 
                ingredientes, "España", "Pasos de preparación", "imagen1.jpg", "30 minutos", 
                "Fácil", fotos, videos, comentarios, 4.5);

        recetaMock2 = new Receta(2L, "Paella", "Arroz con mariscos tradicional", "Plato principal", 
                ingredientes, "España", "Pasos de preparación", "imagen2.jpg", "1 hora", 
                "Media", fotos, videos, comentarios, 4.7);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testHomeWithRecetas() throws Exception {
        // Configurar mock del servicio para devolver recetas
        when(recetasService.obtenerRecetas()).thenReturn(Arrays.asList(recetaMock1, recetaMock2));

        // Realizar la petición y verificar el modelo y la vista
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("recetas"))
                .andExpect(model().attribute("recetas", Arrays.asList(recetaMock1, recetaMock2)));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testHomeWithoutRecetas() throws Exception {
        // Configurar mock del servicio para devolver una lista vacía
        when(recetasService.obtenerRecetas()).thenReturn(Collections.emptyList());

        // Realizar la petición y verificar el modelo y la vista
        mockMvc.perform(get("/home"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("recetas"))
                .andExpect(model().attribute("recetas", Collections.emptyList()));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testRootWithRecetas() throws Exception {
        // Configurar mock del servicio para devolver recetas
        when(recetasService.obtenerRecetas()).thenReturn(Arrays.asList(recetaMock1, recetaMock2));

        // Realizar la petición y verificar el modelo y la vista
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("recetas"))
                .andExpect(model().attribute("recetas", Arrays.asList(recetaMock1, recetaMock2)));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testRootWithoutRecetas() throws Exception {
        // Configurar mock del servicio para devolver una lista vacía
        when(recetasService.obtenerRecetas()).thenReturn(Collections.emptyList());

        // Realizar la petición y verificar el modelo y la vista
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(model().attributeExists("recetas"))
                .andExpect(model().attribute("recetas", Collections.emptyList()));
    }
}