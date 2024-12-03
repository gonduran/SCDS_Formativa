package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.Comentario;
import com.demo.web_recetas.service.ComentariosService;
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
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ComentariosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ComentariosService comentariosService;

    private List<Comentario> comentariosMock;

    @BeforeEach
    void setUp() {
        // Crear datos de prueba
        Comentario comentario1 = new Comentario();
        comentario1.setId(1L);
        comentario1.setUsuario("Usuario 1");
        comentario1.setComentario("Comentario 1");
        comentario1.setEstado(0);

        Comentario comentario2 = new Comentario();
        comentario2.setId(2L);
        comentario2.setUsuario("Usuario 2");
        comentario2.setComentario("Comentario 2");
        comentario2.setEstado(1);

        comentariosMock = Arrays.asList(comentario1, comentario2);
    }

    @Test
    @DisplayName("Listar todos los comentarios")
    @WithMockUser(roles = "ADMIN")
    void listarComentarios_SinFiltro() throws Exception {
        when(comentariosService.listarTodos()).thenReturn(comentariosMock);

        mockMvc.perform(get("/comentarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("comentarios"))
                .andExpect(model().attributeExists("comentarios"))
                .andExpect(model().attribute("comentarios", comentariosMock));
    }

    @Test
    @DisplayName("Listar comentarios filtrados por estado")
    @WithMockUser(roles = "ADMIN")
    void listarComentarios_ConFiltro() throws Exception {
        List<Comentario> comentariosFiltrados = Collections.singletonList(comentariosMock.get(1));
        when(comentariosService.listarPorEstado(1)).thenReturn(comentariosFiltrados);

        mockMvc.perform(get("/comentarios")
                .param("estado", "1"))
                .andExpect(status().isOk())
                .andExpect(view().name("comentarios"))
                .andExpect(model().attributeExists("comentarios"))
                .andExpect(model().attribute("comentarios", comentariosFiltrados));
    }

    @Test
    @DisplayName("Aprobar un comentario")
    @WithMockUser(roles = "ADMIN")
    void aprobarComentario_Exitoso() throws Exception {
        Long comentarioId = 1L;

        mockMvc.perform(post("/comentarios/{id}/aprobar", comentarioId)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comentarios"));

        verify(comentariosService, times(1)).actualizarEstadoComentario(comentarioId, 1);
    }

    @Test
    @DisplayName("Rechazar un comentario")
    @WithMockUser(roles = "ADMIN")
    void rechazarComentario_Exitoso() throws Exception {
        Long comentarioId = 1L;

        mockMvc.perform(post("/comentarios/{id}/rechazar", comentarioId)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comentarios"));

        verify(comentariosService, times(1)).actualizarEstadoComentario(comentarioId, 2);
    }

    @Test
    @DisplayName("Eliminar un comentario")
    @WithMockUser(roles = "ADMIN")
    void eliminarComentario_Exitoso() throws Exception {
        Long comentarioId = 1L;

        mockMvc.perform(post("/comentarios/{id}/eliminar", comentarioId)
                .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/comentarios"));

        verify(comentariosService, times(1)).eliminarComentario(comentarioId);
    }

    @Test
    @DisplayName("Verificar acceso sin autenticación")
    void listarComentarios_SinAutenticacion() throws Exception {
        mockMvc.perform(get("/comentarios"))
                .andExpect(status().is4xxClientError());
    }

    @Test
    @DisplayName("Verificar protección CSRF en aprobar comentario")
    @WithMockUser(roles = "ADMIN")
    void aprobarComentario_SinCSRF() throws Exception {
        mockMvc.perform(post("/comentarios/1/aprobar"))
                .andExpect(status().isFound())          // 302 Found
                .andExpect(redirectedUrl("/accesodenegado"));  // Redirige a acceso denegado
    }

}