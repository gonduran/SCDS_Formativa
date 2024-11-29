package com.demo.web_recetas.controller;

import com.demo.web_recetas.config.WebSecurityConfig;
import com.demo.web_recetas.model.User;
import com.demo.web_recetas.service.RecetasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(RegisterController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(WebSecurityConfig.class)
public class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    private User user;

    @BeforeEach
    public void setUp() {
        // Mocking a user object
        user = new User();
        user.setUsername("testUser");
        user.setNombreCompleto("Juan Pérez");
        user.setEmail("juan.perez@example.com");
        user.setPassword("password123");
    }

    @Test
    @WithAnonymousUser
    public void testShowRegisterForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @WithAnonymousUser
    public void testRegisterUser_Success() throws Exception {
        // Mocking successful registration
        when(recetasService.registerUser(Mockito.any(User.class))).thenReturn("Registro exitoso.");

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", user.getUsername())
                        .param("nombreCompleto", user.getNombreCompleto())
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("confirmPassword", user.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("message", "Registro exitoso."));
    }

    @Test
    @WithAnonymousUser
    public void testRegisterUser_MissingName() throws Exception {
        // Name is empty
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", user.getUsername())
                        .param("nombreCompleto", "")
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("confirmPassword", user.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "El nombre completo es requerido."));
    }

    @Test
    @WithAnonymousUser
    public void testRegisterUser_PasswordMismatch() throws Exception {
        // Password mismatch
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", user.getUsername())
                        .param("nombreCompleto", user.getNombreCompleto())
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("confirmPassword", "differentPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Las contraseñas no coinciden."));
    }

    @Test
    @WithAnonymousUser
    public void testRegisterUser_ServiceException() throws Exception {
        // Mocking a service exception
        when(recetasService.registerUser(Mockito.any(User.class))).thenThrow(new Exception("Error al registrar el usuario."));

        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("username", user.getUsername())
                        .param("nombreCompleto", user.getNombreCompleto())
                        .param("email", user.getEmail())
                        .param("password", user.getPassword())
                        .param("confirmPassword", user.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("error"))
                .andExpect(model().attribute("error", "Error al registrar el usuario."));
    }
}