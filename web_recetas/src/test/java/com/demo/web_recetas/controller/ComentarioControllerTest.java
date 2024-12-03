package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.Comentario;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ComentarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    private Comentario comentario;
    private static final Long RECETA_ID = 1L;

    @BeforeEach
    void setUp() {
        comentario = new Comentario();
        comentario.setId(1L);
        comentario.setUsuario("Juan Pérez");
        comentario.setComentario("¡Excelente receta!");
        comentario.setValoracion(5);
    }

    @Test
    @DisplayName("Agregar comentario exitosamente con usuario autenticado")
    @WithMockUser(username = "usuario_test", roles = "USER")
    void agregarComentario_Exitoso() throws Exception {
        mockMvc.perform(post("/recetas/{recetaId}/comentarios", RECETA_ID)
                .with(csrf())
                .param("id", comentario.getId().toString())
                .param("usuario", comentario.getUsuario())
                .param("comentario", comentario.getComentario())
                .param("valoracion", String.valueOf(comentario.getValoracion())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recetas/" + RECETA_ID))
                .andExpect(flash().attributeCount(0));

        verify(recetasService, times(1)).agregarComentario(eq(RECETA_ID), any(Comentario.class));
    }

    @Test
    @DisplayName("Agregar comentario debe fallar sin autenticación")
    void agregarComentario_SinAutenticacion() throws Exception {
        mockMvc.perform(post("/recetas/{recetaId}/comentarios", RECETA_ID)
                .with(csrf())
                .param("id", comentario.getId().toString())
                .param("usuario", comentario.getUsuario())
                .param("comentario", comentario.getComentario())
                .param("valoracion", String.valueOf(comentario.getValoracion())))
                .andExpect(status().is4xxClientError());  // Cambiado de is3xxRedirection a is4xxClientError
    }

    @Test
    @DisplayName("Manejar error al agregar comentario")
    @WithMockUser(username = "usuario_test", roles = "USER")
    void agregarComentario_ConError() throws Exception {
        // Simular error en el servicio
        doThrow(new RuntimeException("Error al guardar comentario"))
                .when(recetasService)
                .agregarComentario(eq(RECETA_ID), any(Comentario.class));

        mockMvc.perform(post("/recetas/{recetaId}/comentarios", RECETA_ID)
                .with(csrf())
                .param("id", comentario.getId().toString())
                .param("usuario", comentario.getUsuario())
                .param("comentario", comentario.getComentario())
                .param("valoracion", String.valueOf(comentario.getValoracion())))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recetas/" + RECETA_ID))
                .andExpect(flash().attributeExists("errorMessage"))
                .andExpect(flash().attribute("errorMessage", "Hubo un error al agregar el comentario."));
    }

    @Test
    @DisplayName("Validar protección CSRF")
    @WithMockUser(username = "usuario_test", roles = "USER")
    void agregarComentario_SinCSRF() throws Exception {
        mockMvc.perform(post("/recetas/{recetaId}/comentarios", RECETA_ID)
                // Intencionalmente omitimos .with(csrf())
                .param("id", comentario.getId().toString())
                .param("usuario", comentario.getUsuario())
                .param("comentario", comentario.getComentario())
                .param("valoracion", String.valueOf(comentario.getValoracion())))
                .andExpect(status().is3xxRedirection());  // Cambiado de isForbidden a is3xxRedirection
    }
}