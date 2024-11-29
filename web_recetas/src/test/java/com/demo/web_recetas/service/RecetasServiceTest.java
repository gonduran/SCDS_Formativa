package com.demo.web_recetas.service;

import com.demo.web_recetas.integration.TokenStore;
import com.demo.web_recetas.model.Receta;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class RecetasServiceTest {

    private RecetasService recetasService;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private TokenStore tokenStore;

    private static final String MOCK_BACKEND_URL = "http://localhost:8080";

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        recetasService = new RecetasService();
        recetasService.setBackendUrl(MOCK_BACKEND_URL); // Configurar URL simulada
        recetasService.setRestTemplate(restTemplate);  // Inyectar RestTemplate mock
        recetasService.setTokenStore(tokenStore);      // Inyectar TokenStore mock
    }

    @Test
    void testObtenerRecetas() {
        // Crear datos simulados
        Receta receta1 = new Receta(1L, "Tarta de Manzana", "Deliciosa tarta de manzana", "Postres",
                Arrays.asList("Manzana", "Harina", "Azúcar"), "España", "Detalles de preparación",
                "imagen1.jpg", "30 minutos", "Fácil", Arrays.asList("foto1.jpg"), Arrays.asList("video1.mp4"),
                null, 4.5);

        Receta receta2 = new Receta(2L, "Paella", "Clásico plato español", "Platos principales",
                Arrays.asList("Arroz", "Mariscos"), "España", "Detalles de preparación",
                "imagen2.jpg", "1 hora", "Media", Arrays.asList("foto2.jpg"), Arrays.asList("video2.mp4"),
                null, 4.7);

        Receta[] recetasArray = {receta1, receta2};

        // Simular comportamiento del RestTemplate
        when(restTemplate.getForObject(MOCK_BACKEND_URL + "/api/recetas", Receta[].class))
                .thenReturn(recetasArray);

        // Ejecutar método a probar
        List<Receta> recetas = recetasService.obtenerRecetas();

        // Validar resultados
        assertEquals(2, recetas.size());
        assertEquals("Tarta de Manzana", recetas.get(0).getNombre());
        assertEquals("Paella", recetas.get(1).getNombre());
    }
}