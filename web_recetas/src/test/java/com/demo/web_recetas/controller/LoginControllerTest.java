package com.demo.web_recetas.controller;

import com.demo.web_recetas.config.WebSecurityConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(WebSecurityConfig.class)
public class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // @Test
    // @WithAnonymousUser
    // public void testLogin_NoErrorOrLogout() throws Exception {
    //     mockMvc.perform(get("/login"))
    //             .andExpect(status().isOk()) // Página cargada correctamente
    //             .andExpect(view().name("login")) // Nombre de la vista es "login"
    //             .andExpect(model().attributeDoesNotExist("errorMessage", "logoutMessage")); // No hay mensajes en el modelo
    // }

    // @Test
    // @WithAnonymousUser
    // public void testLogin_WithError() throws Exception {
    //     mockMvc.perform(get("/login").param("error", "true"))
    //             .andExpect(status().isOk()) // Página cargada correctamente
    //             .andExpect(view().name("login")) // Nombre de la vista es "login"
    //             .andExpect(model().attributeExists("errorMessage")) // Mensaje de error existe
    //             .andExpect(model().attribute("errorMessage", "Nombre de usuario y contraseña no válidos.")); // Verificar contenido del mensaje
    // }

    // @Test
    // @WithAnonymousUser
    // public void testLogin_WithLogout() throws Exception {
    //     mockMvc.perform(get("/login").param("logout", "true"))
    //             .andExpect(status().isOk()) // Página cargada correctamente
    //             .andExpect(view().name("login")) // Nombre de la vista es "login"
    //             .andExpect(model().attributeExists("logoutMessage")) // Mensaje de logout existe
    //             .andExpect(model().attribute("logoutMessage", "Se ha cerrado la sesión correctamente.")); // Verificar contenido del mensaje
    // }

    // @Test
    // @WithAnonymousUser
    // public void testLogin_WithErrorAndLogout() throws Exception {
    //     mockMvc.perform(get("/login").param("error", "true").param("logout", "true"))
    //             .andExpect(status().isOk()) // Página cargada correctamente
    //             .andExpect(view().name("login")) // Nombre de la vista es "login"
    //             .andExpect(model().attributeExists("errorMessage", "logoutMessage")) // Ambos mensajes existen
    //             .andExpect(model().attribute("errorMessage", "Nombre de usuario y contraseña no válidos.")) // Verificar mensaje de error
    //             .andExpect(model().attribute("logoutMessage", "Se ha cerrado la sesión correctamente.")); // Verificar mensaje de logout
    // }
}