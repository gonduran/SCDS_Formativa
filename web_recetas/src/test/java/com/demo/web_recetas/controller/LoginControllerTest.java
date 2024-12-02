package com.demo.web_recetas.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("Prueba acceso a login sin parámetros")
    @WithAnonymousUser
    public void testLoginWithoutParams() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeDoesNotExist("errorMessage", "logoutMessage"));
    }

    @Test
    @DisplayName("Prueba login con error de autenticación")
    @WithAnonymousUser
    public void testLoginWithError() throws Exception {
        mockMvc.perform(get("/login")
                .param("error", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attribute("errorMessage", 
                    "Nombre de usuario y contraseña no válidos."));
    }

    @Test
    @DisplayName("Prueba login después de cerrar sesión")
    @WithAnonymousUser
    public void testLoginAfterLogout() throws Exception {
        mockMvc.perform(get("/login")
                .param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("logoutMessage"))
                .andExpect(model().attribute("logoutMessage", 
                    "Se ha cerrado la sesión correctamente."));
    }

    @Test
    @DisplayName("Prueba login con error y logout simultáneamente")
    @WithAnonymousUser
    public void testLoginWithErrorAndLogout() throws Exception {
        mockMvc.perform(get("/login")
                .param("error", "true")
                .param("logout", "true"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attributeExists("errorMessage"))
                .andExpect(model().attributeExists("logoutMessage"))
                .andExpect(model().attribute("errorMessage", 
                    "Nombre de usuario y contraseña no válidos."))
                .andExpect(model().attribute("logoutMessage", 
                    "Se ha cerrado la sesión correctamente."));
    }
}