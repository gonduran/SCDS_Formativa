package com.demo.web_recetas.controller;

import com.demo.web_recetas.config.WebSecurityConfig;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(PublicarController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(WebSecurityConfig.class)
public class PublicarControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    private Receta receta;

    @BeforeEach
    public void setUp() {
        receta = new Receta();
        receta.setNombre("Paella");
        receta.setDescripcion("Deliciosa paella con mariscos");
        receta.setTipoCocina("Española");
        receta.setIngredientes(Arrays.asList("Arroz", "Mariscos", "Azafrán"));
        receta.setPaisOrigen("España");
        receta.setDetallePreparacion("Preparar todos los ingredientes y cocinar.");
        receta.setImagen("paella.jpg");
        receta.setTiempoCoccion("40:00");
        receta.setDificultad("Media");
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testShowPublicarForm() throws Exception {
        mockMvc.perform(get("/publicar"))
                .andExpect(status().isOk())
                .andExpect(view().name("publicar"))
                .andExpect(model().attributeExists("receta"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testPublicarReceta_Success() throws Exception {
        mockMvc.perform(post("/publicar")
                        .with(csrf())
                        .param("id", "1") // ID válido como String
                        .param("nombre", "Tarta de Manzana")
                        .param("descripcion", "Deliciosa tarta de manzana")
                        .param("tipoCocina", "Postres")
                        .param("tiempoCoccion", "40:00") // Formato HH:MM válido
                        .param("dificultad", "Media") // Valor permitido por la validación
                        .param("ingredientes", "Manzana, Harina, Azúcar") // Cadena de ejemplo
                        .param("paisOrigen", "España")
                        .param("detallePreparacion", "Mezclar todo y hornear.")
                        .param("imagen", "imagen.jpg")
                        .param("fotos", "foto1.jpg,foto2.jpg")
                        .param("videos", "video1.mp4,video2.mp4"))
                .andExpect(status().is3xxRedirection()) // Redirección esperada
                .andExpect(view().name("redirect:/home"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testPublicarReceta_InvalidTiempoCoccion() throws Exception {
        receta.setTiempoCoccion("invalid");

        mockMvc.perform(post("/publicar")
                        .with(csrf())
                        .flashAttr("receta", receta))
                .andExpect(status().isOk())
                .andExpect(view().name("publicar"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "El formato del tiempo de cocción debe ser HH:MM"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testPublicarReceta_InvalidDificultad() throws Exception {
        receta.setTiempoCoccion("40:00");
        receta.setDificultad("Invalida");

        mockMvc.perform(post("/publicar")
                        .with(csrf())
                        .flashAttr("receta", receta))
                .andExpect(status().isOk())
                .andExpect(view().name("publicar"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "La dificultad debe ser Alta, Media o Baja"));
    }

    @Test
    @DisplayName("Publicar receta con error de servicio")
    @WithMockUser(username = "testUser", roles = "USER")
    public void testPublicarReceta_ServiceError() throws Exception {
        // Configurar el mock para lanzar excepción
        doThrow(new RuntimeException("Error de servicio"))
            .when(recetasService).publicarReceta(any(Receta.class));

        mockMvc.perform(post("/publicar")
                .with(csrf())
                .param("nombre", "Tarta")
                .param("descripcion", "Descripción")
                .param("tipoCocina", "Postres")
                .param("tiempoCoccion", "40:00")
                .param("dificultad", "Media")
                .param("ingredientes", "Ingredientes")
                .param("paisOrigen", "España")
                .param("detallePreparacion", "Preparación")
                .param("imagen", "imagen.jpg"))
            .andExpect(status().isOk())
            .andExpect(view().name("publicar"))
            .andExpect(model().attributeExists("error"))
            .andExpect(model().attribute("error", 
                containsString("Hubo un error al publicar la receta")));
    }

    @Test
    @DisplayName("Acceso a formulario de publicación sin autenticación")
    public void testShowPublicarForm_SinAutenticacion() throws Exception {
        mockMvc.perform(get("/publicar"))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("Intento de publicación sin autenticación")
    public void testPublicarReceta_SinAutenticacion() throws Exception {
        mockMvc.perform(post("/publicar")
                .with(csrf())
                .flashAttr("receta", receta))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/login"));
    }

    @Test
    @DisplayName("Publicar receta con campos vacíos")
    @WithMockUser(username = "testUser", roles = "USER")
    public void testPublicarReceta_CamposVacios() throws Exception {
        Receta recetaVacia = new Receta();
        recetaVacia.setTiempoCoccion("40:00");
        recetaVacia.setDificultad("Media");
    
        mockMvc.perform(post("/publicar")
                .with(csrf())
                .flashAttr("receta", recetaVacia))
            .andExpect(status().isFound());
    }

    @Test
    @DisplayName("Publicar receta sin token CSRF")
    @WithMockUser(username = "testUser", roles = "USER")
    public void testPublicarReceta_SinCSRF() throws Exception {
        mockMvc.perform(post("/publicar")
                .flashAttr("receta", receta))
            .andExpect(status().isFound());
    }

}
