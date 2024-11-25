package com.demo.web_recetas.template;

import com.demo.web_recetas.controller.HomeController;
import com.demo.web_recetas.integration.TokenStore;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import java.util.Collections;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;

@WebMvcTest(HomeController.class)
@ActiveProfiles("test") // Habilitar perfil de test
public class HomeTemplateMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    @MockBean
    private TokenStore tokenStore;

    @Test
    public void testHomeTemplate() throws Exception {
        // Mockear el método del servicio
        Mockito.when(recetasService.buscarRecetas(Mockito.anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/home").with(user("testUser").roles("USER"))) // Simular autenticación
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Bienvenido a Recetas")));
    }

    @Test
    public void testHomeTemplateWithRecipes() throws Exception {
        // Crea una instancia de Receta utilizando un constructor válido
        Receta receta = new Receta();
        receta.setId(1L);
        receta.setNombre("Tarta de Manzana");
        receta.setDescripcion("Una deliciosa tarta");
        receta.setImagen("/images/tarta.jpg");

        // Simula recetas del servicio
        Mockito.when(recetasService.obtenerRecetas()).thenReturn(Collections.singletonList(receta));

        mockMvc.perform(get("/home").with(user("testUser").roles("USER")))
                .andExpect(status().isOk())
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Tarta de Manzana")));
    }
}