package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.ArrayList;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.doNothing;

@SpringBootTest
@AutoConfigureMockMvc
class PublicarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    private Receta recetaValida;

    @BeforeEach
    void setUp() {
        recetaValida = new Receta();
        recetaValida.setNombre("Paella Valenciana");
        recetaValida.setDescripcion("Auténtica paella valenciana");
        recetaValida.setTipoCocina("Española");
        recetaValida.setIngredientes(Arrays.asList("Arroz", "Azafrán", "Pollo", "Conejo"));
        recetaValida.setPaisOrigen("España");
        recetaValida.setDetallePreparacion("1. Sofreír...");
        recetaValida.setTiempoCoccion("10:30");
        recetaValida.setDificultad("Media");
        recetaValida.setFotos(new ArrayList<>());
        recetaValida.setVideos(new ArrayList<>());
        recetaValida.setComentarios(new ArrayList<>());
    }

    @Test
    @DisplayName("Mostrar formulario de publicación")
    @WithMockUser
    void showPublicarForm() throws Exception {
        mockMvc.perform(get("/publicar"))
                .andExpect(status().isOk())
                .andExpect(view().name("publicar"))
                .andExpect(model().attributeExists("receta"));
    }

    @Test
    @DisplayName("Error al publicar - Tiempo de cocción inválido")
    @WithMockUser
    void publicarReceta_InvalidTiempoCoccion() throws Exception {
        recetaValida.setTiempoCoccion("invalido");

        mockMvc.perform(post("/publicar")
                .with(csrf())
                .flashAttr("receta", recetaValida))
                .andExpect(status().isOk())
                .andExpect(view().name("publicar"))
                .andExpect(model().attribute("error", "El formato del tiempo de cocción debe ser HH:MM"));
    }

    @Test
    @DisplayName("Error al publicar - Sin autenticación")
    void publicarReceta_Unauthorized() throws Exception {
        mockMvc.perform(post("/publicar")
                .with(csrf())
                .flashAttr("receta", recetaValida))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("Error al publicar - Sin token CSRF")
    @WithMockUser
    void publicarReceta_NoCsrf() throws Exception {
        mockMvc.perform(post("/publicar")
                .flashAttr("receta", recetaValida))
                .andExpect(status().is3xxRedirection());
    }

    @Test
    @DisplayName("Mostrar formulario - Sin autenticación")
    void showPublicarForm_Unauthorized() throws Exception {
        mockMvc.perform(get("/publicar"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("Error al publicar - Dificultad inválida")
    @WithMockUser
    void publicarReceta_InvalidDificultad() throws Exception {
        Receta recetaInvalida = new Receta();
        recetaInvalida.setNombre("Paella Valenciana");
        recetaInvalida.setDescripcion("Receta de prueba");
        recetaInvalida.setTipoCocina("Española");
        recetaInvalida.setIngredientes(Arrays.asList("Ingrediente 1"));
        recetaInvalida.setPaisOrigen("España");
        recetaInvalida.setDetallePreparacion("Preparación");
        recetaInvalida.setTiempoCoccion("20:30"); // Tiempo válido
        recetaInvalida.setDificultad("Imposible"); // Dificultad inválida
        recetaInvalida.setFotos(new ArrayList<>());
        recetaInvalida.setVideos(new ArrayList<>());
        recetaInvalida.setComentarios(new ArrayList<>());
    
        mockMvc.perform(post("/publicar")
                .with(csrf())
                .flashAttr("receta", recetaInvalida))
                .andExpect(status().isOk())
                .andExpect(view().name("publicar"))
                .andExpect(model().attribute("error", "La dificultad debe ser Alta, Media o Baja"));
    }

    @Test
    @DisplayName("Error al publicar - Error del servicio")
    @WithMockUser
    void publicarReceta_ServiceError() throws Exception {
        doThrow(new RuntimeException("Error de servicio"))
                .when(recetasService)
                .publicarReceta(any(Receta.class));

        mockMvc.perform(post("/publicar")
                .with(csrf())
                .flashAttr("receta", recetaValida))
                .andExpect(status().isOk())
                .andExpect(view().name("publicar"))
                .andExpect(model().attribute("error", "Hubo un error al publicar la receta: Error de servicio"));
    }

    @Test
    @DisplayName("Publicar receta exitosamente con redirección")
    @WithMockUser
    void publicarReceta_Success() throws Exception {
        mockMvc.perform(post("/publicar")
                .with(csrf())
                .flashAttr("receta", recetaValida))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/home"));

        verify(recetasService).publicarReceta(recetaValida);
    }
    
}