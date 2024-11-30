package com.demo.backend_recetas.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.demo.backend_recetas.config.TestSecurityConfig;

@WebMvcTest(SecuredController.class)
@Import(TestSecurityConfig.class)
class SecuredControllerTest {

    @Autowired
    private WebApplicationContext context;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
            .webAppContextSetup(context)
            .apply(SecurityMockMvcConfigurers.springSecurity())
            .build();
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Endpoint público es accesible sin autenticación")
    void publicEndpoint_IsAccessible() throws Exception {
        mockMvc.perform(get("/api/public"))
            .andExpect(status().isOk())
            .andExpect(content().string("Este endpoint es público"));
    }

    @Test
    @DisplayName("Usuario normal puede acceder al endpoint de usuario")
    @WithMockUser(roles = "USER")
    void userEndpoint_ShouldBeAccessibleWithUserRole() throws Exception {
        mockMvc.perform(get("/api/user"))
            .andExpect(status().isOk())
            .andExpect(content().string("Este endpoint es solo para usuarios autenticados"));
    }

    @Test
    @DisplayName("Usuario sin autenticar no puede acceder al endpoint de usuario")
    void userEndpoint_ShouldNotBeAccessibleWithoutAuth() throws Exception {
        mockMvc.perform(get("/api/user"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Admin puede acceder al endpoint de admin")
    @WithMockUser(roles = "ADMIN")
    void adminEndpoint_ShouldBeAccessibleWithAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin"))
            .andExpect(status().isOk())
            .andExpect(content().string("Este endpoint es solo para administradores"));
    }

    @Test
    @DisplayName("Greetings con nombre muestra saludo personalizado")
    @WithMockUser(username = "testuser", roles = "USER")
    void greetings_WithNameShouldShowCustomGreeting() throws Exception {
        mockMvc.perform(get("/api/greetings")
            .param("name", "John"))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello John! (authenticated as testuser, roles: [USER])"));
    }

    @Test
    @DisplayName("Greetings sin nombre muestra saludo por defecto")
    @WithMockUser(username = "testuser", roles = "USER")
    void greetings_WithoutNameShouldShowDefaultGreeting() throws Exception {
        mockMvc.perform(get("/api/greetings"))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello testuser! (roles: [USER])"));
    }

    @Test
    @DisplayName("Greetings muestra múltiples roles correctamente")
    @WithMockUser(username = "admin", roles = {"USER", "ADMIN"})
    void greetings_ShouldShowMultipleRoles() throws Exception {
        mockMvc.perform(get("/api/greetings"))
            .andExpect(status().isOk())
            .andExpect(content().string("Hello admin! (roles: [ADMIN, USER])"));
    }
}