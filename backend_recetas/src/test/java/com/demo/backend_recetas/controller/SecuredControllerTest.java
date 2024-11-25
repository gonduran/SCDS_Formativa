package com.demo.backend_recetas.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


@WebMvcTest(SecuredController.class)
@AutoConfigureMockMvc
public class SecuredControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private SecuredController securedController;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Configurar SecurityContext para los tests que requieren autenticación
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @WithMockUser(username = "testuser", roles = "USER")
    void userEndpoint_AccessAsUser() throws Exception {
        mockMvc.perform(get("/api/user"))
            .andExpect(status().isOk())
            .andExpect(content().string("Este endpoint es solo para usuarios autenticados"));
    }

    @Test
    void userEndpoint_AccessWithoutUserRole() throws Exception {
        mockMvc.perform(get("/api/user"))
            .andExpect(status().isUnauthorized()); // Asegúrate que esté correcto
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void adminEndpoint_AccessAsAdmin() throws Exception {
        mockMvc.perform(get("/api/admin"))
            .andExpect(status().isOk())
            .andExpect(content().string("Este endpoint es solo para administradores"));
    }

    @Test
    void adminEndpoint_AccessWithoutAdminRole() throws Exception {
        mockMvc.perform(get("/api/admin"))
            .andExpect(status().isUnauthorized()); // Asegúrate que esté correcto
    }
}
