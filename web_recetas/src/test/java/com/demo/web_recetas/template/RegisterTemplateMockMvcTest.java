package com.demo.web_recetas.template;

import com.demo.web_recetas.controller.RegisterController;
import com.demo.web_recetas.service.RecetasService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterController.class)
@ActiveProfiles("test")
public class RegisterTemplateMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    @Test
    public void testShowRegisterForm() throws Exception {
        mockMvc.perform(get("/register").with(user("testUser").roles("USER"))) // Simular autenticación
                .andExpect(status().isOk());
    }
}