package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.Comentario;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RecetasControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    private Receta recetaCompleta;
    private static final Long RECETA_ID = 1L;

    @BeforeEach
    void setUp() {
        // Crear comentarios con diferentes estados
        Comentario comentarioAprobado = new Comentario();
        comentarioAprobado.setId(1L);
        comentarioAprobado.setUsuario("usuario1");
        comentarioAprobado.setComentario("Comentario aprobado");
        comentarioAprobado.setValoracion(5);
        comentarioAprobado.setEstado(1); // Aprobado

        Comentario comentarioPendiente = new Comentario();
        comentarioPendiente.setId(2L);
        comentarioPendiente.setUsuario("usuario2");
        comentarioPendiente.setComentario("Comentario pendiente");
        comentarioPendiente.setValoracion(4);
        comentarioPendiente.setEstado(0); // Pendiente

        // Crear receta completa
        recetaCompleta = new Receta();
        recetaCompleta.setId(RECETA_ID);
        recetaCompleta.setNombre("Paella Valenciana");
        recetaCompleta.setDescripcion("Auténtica paella valenciana");
        recetaCompleta.setTipoCocina("Española");
        recetaCompleta.setIngredientes(Arrays.asList("Arroz", "Azafrán", "Pollo"));
        recetaCompleta.setComentarios(Arrays.asList(comentarioAprobado, comentarioPendiente));
        recetaCompleta.setFotos(Arrays.asList("foto1.jpg", "foto2.jpg"));
        recetaCompleta.setVideos(Arrays.asList("video1.mp4", "video2.mp4"));
    }

    @Test
    @DisplayName("Ver detalle de receta existente con usuario autenticado")
    @WithMockUser(username = "testUser", roles = "USER")
    void verDetalleReceta_Success() throws Exception {
        when(recetasService.obtenerRecetaPorId(RECETA_ID))
                .thenReturn(Optional.of(recetaCompleta));

        // Filtrar solo comentarios aprobados para la verificación
        List<Comentario> comentariosAprobados = recetaCompleta.getComentarios().stream()
                .filter(c -> c.getEstado() != null && c.getEstado() == 1)
                .collect(Collectors.toList());

        mockMvc.perform(get("/recetas/{id}", RECETA_ID))
                .andExpect(status().isOk())
                .andExpect(view().name("recetas"))
                .andExpect(model().attribute("receta", recetaCompleta))
                .andExpect(model().attribute("comentarios", comentariosAprobados))
                .andExpect(model().attribute("fotos", recetaCompleta.getFotos()))
                .andExpect(model().attribute("videos", recetaCompleta.getVideos()))
                .andExpect(model().attribute("nombreUsuario", "testUser"))
                .andExpect(model().attributeExists("nuevoComentario"));
    }

    @Test
    @DisplayName("Ver detalle de receta no existente")
    @WithMockUser
    void verDetalleReceta_NotFound() throws Exception {
        when(recetasService.obtenerRecetaPorId(anyLong()))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/recetas/{id}", 999))
                .andExpect(status().isOk())
                .andExpect(view().name("error/404"));
    }

    @Test
    @DisplayName("Ver detalle de receta sin autenticación")
    void verDetalleReceta_Unauthorized() throws Exception {
        mockMvc.perform(get("/recetas/{id}", RECETA_ID))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // @Test
    // @DisplayName("Agregar media exitosamente")
    // @WithMockUser
    // void agregarMedia_Success() throws Exception {
    //     mockMvc.perform(post("/recetas/{id}/media", RECETA_ID)
    //             .with(csrf())
    //             .param("fotos", "nuevafoto1.jpg,nuevafoto2.jpg")
    //             .param("videos", "nuevovideo1.mp4,nuevovideo2.mp4"))
    //             .andExpect(status().is3xxRedirection())
    //             .andExpect(redirectedUrl("/recetas/" + RECETA_ID))
    //             .andExpect(flash().attributeExists("message"));

    //     verify(recetasService).agregarMedia(
    //             eq(RECETA_ID), 
    //             eq("nuevafoto1.jpg,nuevafoto2.jpg"), 
    //             eq("nuevovideo1.mp4,nuevovideo2.mp4"));
    // }

    @Test
    @DisplayName("Error al agregar media")
    @WithMockUser
    void agregarMedia_Error() throws Exception {
        String errorMessage = "Error al procesar archivos";
        doThrow(new RuntimeException(errorMessage))
                .when(recetasService)
                .agregarMedia(anyLong(), anyString(), anyString());

        mockMvc.perform(post("/recetas/{id}/media", RECETA_ID)
                .with(csrf())
                .param("fotos", "foto.jpg")
                .param("videos", "video.mp4"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("error", "Error al agregar media: " + errorMessage));
    }

    @Test
    @DisplayName("Agregar media sin autenticación")
    void agregarMedia_Unauthorized() throws Exception {
        mockMvc.perform(post("/recetas/{id}/media", RECETA_ID)
                .with(csrf())
                .param("fotos", "foto.jpg")
                .param("videos", "video.mp4"))
                .andExpect(status().is4xxClientError())
                .andExpect(redirectedUrl(null));
    }

    @Test
    @DisplayName("Agregar media sin token CSRF")
    @WithMockUser
    void agregarMedia_NoCsrf() throws Exception {
        mockMvc.perform(post("/recetas/{id}/media", RECETA_ID)
                .param("fotos", "foto.jpg")
                .param("videos", "video.mp4"))
                .andExpect(status().is3xxRedirection());
    }
}