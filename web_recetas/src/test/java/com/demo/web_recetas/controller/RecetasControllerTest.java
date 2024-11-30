package com.demo.web_recetas.controller;

import com.demo.web_recetas.config.WebSecurityConfig;
import com.demo.web_recetas.model.Comentario;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecetasController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(WebSecurityConfig.class)
public class RecetasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    private Receta receta;

    @BeforeEach
    public void setUp() {
        Comentario comentario1 = new Comentario(1L, "usuario1", "Muy buena receta", 5, 0);
        Comentario comentario2 = new Comentario(2L, "usuario2", "Me encantó", 4, 0);

        receta = new Receta();
        receta.setId(1L);
        receta.setNombre("Paella");
        receta.setDescripcion("Deliciosa paella con mariscos");
        receta.setTipoCocina("Española");
        receta.setIngredientes(Arrays.asList("Arroz", "Mariscos", "Azafrán"));
        receta.setPaisOrigen("España");
        receta.setDetallePreparacion("Preparar todos los ingredientes y cocinar.");
        receta.setImagen("paella.jpg");
        receta.setTiempoCoccion("40:00");
        receta.setDificultad("Media");
        receta.setComentarios(Arrays.asList(comentario1, comentario2));
        receta.setFotos(Arrays.asList("foto1.jpg", "foto2.jpg"));
        receta.setVideos(Arrays.asList("video1.mp4", "video2.mp4"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testVerDetalleReceta_Success() throws Exception {
        when(recetasService.obtenerRecetaPorId(1L)).thenReturn(Optional.of(receta));

        mockMvc.perform(get("/recetas/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("recetas"))
                .andExpect(model().attributeExists("receta"))
                .andExpect(model().attribute("receta", receta))
                .andExpect(model().attributeExists("comentarios", "fotos", "videos", "nombreUsuario"));
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testVerDetalleReceta_NotFound() throws Exception {
        when(recetasService.obtenerRecetaPorId(1L)).thenReturn(Optional.empty());
    
        mockMvc.perform(get("/recetas/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("error/404"));
    }

    /*@Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testAgregarMedia_Success() throws Exception {
        mockMvc.perform(post("/recetas/1/media")
                        .with(csrf())
                        .param("fotos", "foto1.jpg,foto2.jpg")
                        .param("videos", "video1.mp4,video2.mp4"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recetas/1"))
                .andExpect(flash().attributeExists("message"));
    }*/

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testAgregarMedia_Error() throws Exception {
        doThrow(new RuntimeException("Error interno"))
                .when(recetasService).agregarMedia(Mockito.eq(1L), Mockito.anyString(), Mockito.anyString());

        mockMvc.perform(post("/recetas/1/media")
                        .with(csrf())
                        .param("fotos", "foto1.jpg,foto2.jpg")
                        .param("videos", "video1.mp4,video2.mp4"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Error al agregar media: Error interno"));
    }
}
