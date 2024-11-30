package com.demo.web_recetas.service;

import com.demo.web_recetas.exception.TokenNotFoundException;
import com.demo.web_recetas.integration.TokenStore;
import com.demo.web_recetas.model.Comentario;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

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
        recetasService.setRestTemplate(restTemplate); // Inyectar RestTemplate mock
        recetasService.setTokenStore(tokenStore); // Inyectar TokenStore mock
    }

    @Test
    void testObtenerRecetas() {
        Receta receta1 = new Receta(1L, "Tarta de Manzana", "Deliciosa tarta de manzana", "Postres",
                Arrays.asList("Manzana", "Harina", "Azúcar"), "España", "Detalles de preparación",
                "imagen1.jpg", "30 minutos", "Fácil", null, null, null, 4.5);

        Receta receta2 = new Receta(2L, "Paella", "Clásico plato español", "Platos principales",
                Arrays.asList("Arroz", "Mariscos"), "España", "Detalles de preparación",
                "imagen2.jpg", "1 hora", "Media", null, null, null, 4.7);

        Receta[] recetasArray = { receta1, receta2 };

        when(restTemplate.getForObject(MOCK_BACKEND_URL + "/api/recetas", Receta[].class))
                .thenReturn(recetasArray);

        List<Receta> recetas = recetasService.obtenerRecetas();

        assertEquals(2, recetas.size());
        assertEquals("Tarta de Manzana", recetas.get(0).getNombre());
        assertEquals("Paella", recetas.get(1).getNombre());
    }

    @Test
    void testBuscarRecetas() {
        Receta receta = new Receta(1L, "Tarta de Manzana", "Deliciosa tarta de manzana", "Postres",
                Arrays.asList("Manzana", "Harina", "Azúcar"), "España", "Detalles de preparación",
                "imagen1.jpg", "30 minutos", "Fácil", null, null, null, 4.5);

        Receta[] recetasArray = { receta };

        when(restTemplate.getForObject(MOCK_BACKEND_URL + "/api/recetas_buscar?keyword=manzana", Receta[].class))
                .thenReturn(recetasArray);

        List<Receta> recetas = recetasService.buscarRecetas("manzana");

        assertEquals(1, recetas.size());
        assertEquals("Tarta de Manzana", recetas.get(0).getNombre());
    }

    @Test
    void testRegistrarUsuario() throws Exception {
        User user = new User();
        user.setUsername("testUser");

        when(restTemplate.postForEntity(MOCK_BACKEND_URL + "/api/register", user, String.class))
                .thenReturn(new ResponseEntity<>("Usuario registrado", HttpStatus.OK));

        String resultado = recetasService.registerUser(user);

        assertEquals("Usuario registrado", resultado);
    }

    @Test
    void testPublicarReceta() {
        Receta receta = new Receta();
        receta.setNombre("Nueva receta");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "mock-token");
        when(tokenStore.getToken()).thenReturn("mock-token");

        when(restTemplate.exchange(eq(MOCK_BACKEND_URL + "/api/recetas/publicar"), eq(HttpMethod.POST),
                any(HttpEntity.class), eq(String.class)))
                .thenReturn(new ResponseEntity<>("Receta publicada", HttpStatus.OK));

        String resultado = recetasService.publicarReceta(receta);

        assertEquals("Receta publicada", resultado);
    }

    @Test
    void testAgregarComentario() {
        Comentario comentario = new Comentario();
        comentario.setComentario("Excelente receta");

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "mock-token");
        when(tokenStore.getToken()).thenReturn("mock-token");

        ResponseEntity<String> mockResponse = new ResponseEntity<>("Comentario agregado", HttpStatus.OK);
        when(restTemplate.exchange(eq(MOCK_BACKEND_URL + "/api/recetas/1/comentarios"),
                eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        recetasService.agregarComentario(1L, comentario);

        verify(restTemplate, times(1)).exchange(eq(MOCK_BACKEND_URL + "/api/recetas/1/comentarios"),
                eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
    }

    @Test
    void testAgregarMedia() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "mock-token");
        when(tokenStore.getToken()).thenReturn("mock-token");

        ResponseEntity<String> mockResponse = new ResponseEntity<>("Media agregada", HttpStatus.OK);
        when(restTemplate.exchange(eq(MOCK_BACKEND_URL + "/api/recetas/1/media"),
                eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class)))
                .thenReturn(mockResponse);

        recetasService.agregarMedia(1L, "foto1.jpg", "video1.mp4");

        verify(restTemplate, times(1)).exchange(eq(MOCK_BACKEND_URL + "/api/recetas/1/media"),
                eq(HttpMethod.POST), any(HttpEntity.class), eq(String.class));
    }

    /*@Test
    void testObtenerRecetaPorId() {
        // Crear una receta simulada
        Receta receta = new Receta(1L, "Tarta de Manzana", "Deliciosa tarta de manzana", "Postres",
                Arrays.asList("Manzana", "Harina", "Azúcar"), "España", "Detalles de preparación",
                "imagen1.jpg", "30 minutos", "Fácil", null, null, null, 4.5);

        // Configurar headers simulados
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "mock-token");
        when(tokenStore.getToken()).thenReturn("mock-token");

        // Configurar la respuesta simulada del RestTemplate
        ResponseEntity<Receta> mockResponse = new ResponseEntity<>(receta, HttpStatus.OK);

        when(restTemplate.exchange(eq(MOCK_BACKEND_URL + "/api/recetas_detalle/1"),
                eq(HttpMethod.GET), any(HttpEntity.class), eq(Receta.class)))
                .thenReturn(mockResponse);

        // Ejecutar el método a probar
        Optional<Receta> resultado = recetasService.obtenerRecetaPorId(1L);

        // Validar el resultado
        assertTrue(resultado.isPresent());
        assertEquals("Tarta de Manzana", resultado.get().getNombre());
        assertEquals("Postres", resultado.get().getTipoCocina());
    }*/
}