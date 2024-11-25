package com.demo.web_recetas.template;

import com.demo.web_recetas.controller.BuscarController;
import com.demo.web_recetas.integration.TokenStore;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(BuscarController.class)
@ActiveProfiles("test")
public class BuscarTemplateMockMvcTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    @MockBean
    private TokenStore tokenStore;

    @Test
    public void testBuscarTemplateSinResultados() throws Exception {
        // Mockear el servicio para devolver una lista vacía
        Mockito.when(recetasService.buscarRecetas(Mockito.anyString())).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/buscar")
                        .param("searchQuery", "pizza")
                        .with(SecurityMockMvcRequestPostProcessors.user("testUser").roles("USER"))) // Simular autenticación
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("No se encontraron recetas que coincidan con la búsqueda.")))
                .andExpect(content().string(containsString("Buscar Recetas")));
    }

    @Test
    public void testBuscarTemplateConResultados() throws Exception {
        // Crear recetas simuladas
        Receta receta = new Receta();
        receta.setId(1L);
        receta.setNombre("Pizza Margherita");
        receta.setDescripcion("Una deliciosa pizza clásica italiana.");
        receta.setTipoCocina("Italiana");
        receta.setPaisOrigen("Italia");
        receta.setImagen("/images/pizza.jpg");

        List<Receta> recetasMock = List.of(receta);

        // Mockear el servicio para devolver recetas simuladas
        Mockito.when(recetasService.buscarRecetas("pizza")).thenReturn(recetasMock);

        mockMvc.perform(get("/buscar")
                        .param("searchQuery", "pizza")
                        .with(SecurityMockMvcRequestPostProcessors.user("testUser").roles("USER"))) // Simular autenticación
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Pizza Margherita")))
                .andExpect(content().string(containsString("Una deliciosa pizza clásica italiana.")))
                .andExpect(content().string(containsString("Italiana")))
                .andExpect(content().string(containsString("Italia")));
    }
}