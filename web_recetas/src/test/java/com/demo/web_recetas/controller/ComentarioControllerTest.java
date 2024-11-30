package com.demo.web_recetas.controller;

import com.demo.web_recetas.config.WebSecurityConfig;
import com.demo.web_recetas.model.Comentario;
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

import static org.mockito.Mockito.doThrow;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComentarioController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(WebSecurityConfig.class)
public class ComentarioControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    private Comentario comentario;

    @BeforeEach
    public void setUp() {
        comentario = new Comentario();
        comentario.setId(1L);
        comentario.setUsuario("Juan Pérez");
        comentario.setComentario("¡Excelente receta!");
        comentario.setValoracion(5);
    }

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testAgregarComentario_Success() throws Exception {
        mockMvc.perform(post("/recetas/1/comentarios")
                        .with(csrf())
                        .param("id", comentario.getId().toString())
                        .param("usuario", comentario.getUsuario())
                        .param("comentario", comentario.getComentario())
                        .param("valoracion", comentario.getValoracion().toString()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/recetas/1"));
    }


    /*@Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testAgregarComentario_ServiceException() throws Exception {
        // Configurar el mock para lanzar una excepción
        doThrow(new RuntimeException("Error al agregar el comentario"))
                .when(recetasService).agregarComentario(Mockito.eq(1L), Mockito.any(Comentario.class));

        mockMvc.perform(post("/recetas/1/comentarios")
                        .with(csrf())
                        .param("id", comentario.getId().toString())
                        .param("usuario", comentario.getUsuario())
                        .param("comentario", comentario.getComentario())
                        .param("valoracion", comentario.getValoracion().toString()))
                .andExpect(status().is3xxRedirection()) // Redirección a la página de la receta
                .andExpect(redirectedUrl("/recetas/1"));
    }*/
}