package com.demo.web_recetas.template;

import com.demo.web_recetas.controller.LoginController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LoginController.class)
@ActiveProfiles("test")
public class LoginTemplateMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    public void testLoginTemplate() throws Exception {
        mockMvc.perform(get("/login"))
            .andExpect(status().isOk());
    }
}