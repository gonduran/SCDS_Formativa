package com.demo.web_recetas.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(ErrorController.class)
public class ErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser(username = "testUser", roles = "USER")
    public void testAccessDeniedView() throws Exception {
        // Realizar la petición y verificar que se muestra la vista correcta
        mockMvc.perform(get("/accesodenegado"))
                .andExpect(status().isOk())
                .andExpect(view().name("accesodenegado"));
    }

    @Test
    @WithMockUser // Simula un usuario autenticado sin roles específicos
    public void testAccessDeniedWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/accesodenegado"))
                .andExpect(status().isOk())
                .andExpect(view().name("accesodenegado"));
    }
}
