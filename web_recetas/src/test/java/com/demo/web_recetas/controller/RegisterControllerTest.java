package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.User;
import com.demo.web_recetas.service.RecetasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class RegisterControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    private User validUser;

    @BeforeEach
    void setUp() {
        validUser = new User();
        validUser.setUsername("testUser");
        validUser.setNombreCompleto("Test User");
        validUser.setEmail("test@example.com");
        validUser.setPassword("password123");
    }

    @Test
    @DisplayName("Mostrar formulario de registro")
    void showRegisterForm() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attributeExists("user"));
    }

    @Test
    @DisplayName("Registro exitoso de usuario")
    void registerUser_Success() throws Exception {
        when(recetasService.registerUser(any(User.class)))
                .thenReturn("Usuario registrado exitosamente");

        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", validUser.getUsername())
                .param("nombreCompleto", validUser.getNombreCompleto())
                .param("email", validUser.getEmail())
                .param("password", validUser.getPassword())
                .param("confirmPassword", validUser.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("login"))
                .andExpect(model().attribute("message", "Usuario registrado exitosamente"));
    }

    @Test
    @DisplayName("Registro fallido - Nombre completo vacío")
    void registerUser_EmptyName() throws Exception {
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", validUser.getUsername())
                .param("nombreCompleto", "")
                .param("email", validUser.getEmail())
                .param("password", validUser.getPassword())
                .param("confirmPassword", validUser.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", "El nombre completo es requerido."));
    }

    @Test
    @DisplayName("Registro fallido - Nombre completo null")
    void registerUser_NullName() throws Exception {
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", validUser.getUsername())
                .param("email", validUser.getEmail())
                .param("password", validUser.getPassword())
                .param("confirmPassword", validUser.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", "El nombre completo es requerido."));
    }

    @Test
    @DisplayName("Registro fallido - Contraseñas no coinciden")
    void registerUser_PasswordMismatch() throws Exception {
        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", validUser.getUsername())
                .param("nombreCompleto", validUser.getNombreCompleto())
                .param("email", validUser.getEmail())
                .param("password", validUser.getPassword())
                .param("confirmPassword", "diferentPassword"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", "Las contraseñas no coinciden."));
    }

    @Test
    @DisplayName("Registro fallido - Error en el servicio")
    void registerUser_ServiceError() throws Exception {
        when(recetasService.registerUser(any(User.class)))
                .thenThrow(new RuntimeException("Error en el registro"));

        mockMvc.perform(post("/register")
                .with(csrf())
                .param("username", validUser.getUsername())
                .param("nombreCompleto", validUser.getNombreCompleto())
                .param("email", validUser.getEmail())
                .param("password", validUser.getPassword())
                .param("confirmPassword", validUser.getPassword()))
                .andExpect(status().isOk())
                .andExpect(view().name("register"))
                .andExpect(model().attribute("error", "Error en el registro"));
    }

}