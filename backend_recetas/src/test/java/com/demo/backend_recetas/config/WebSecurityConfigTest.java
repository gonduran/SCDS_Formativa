package com.demo.backend_recetas.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.demo.backend_recetas.security.jwt.JWTAuthorizationFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(WebSecurityConfig.class) // Carga solo la configuración de seguridad
public class WebSecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JWTAuthorizationFilter jwtAuthorizationFilter; // Mock del filtro JWT

    @Test
    public void testPublicEndpoints() throws Exception {
        mockMvc.perform(get("/api/public"))
               .andExpect(status().isOk()); // Asegura que el endpoint público está accesible
    }
}
