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
import static org.mockito.ArgumentMatchers.contains;

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

    @Test
    void testValidarToken_TokenNull() {
        // Configuración
        when(tokenStore.getToken()).thenReturn(null);

        // Verificación
        TokenNotFoundException exception = assertThrows(TokenNotFoundException.class, () -> {
            recetasService.obtenerRecetaPorId(1L);
        });

        assertEquals("Token de autenticación no disponible", exception.getMessage());
    }

    @Test
    void testValidarToken_TokenEmpty() {
        // Configuración
        when(tokenStore.getToken()).thenReturn("");
        
        // Verificación
        TokenNotFoundException exception = assertThrows(TokenNotFoundException.class, () -> {
            recetasService.obtenerRecetaPorId(1L);
        });
        
        assertEquals("Token de autenticación no disponible", exception.getMessage());
    }

    @Test
    void testObtenerRecetaPorId() {
        // Configurar token válido
        when(tokenStore.getToken()).thenReturn("valid-token");
        
        // Crear receta simulada
        Receta receta = new Receta();
        receta.setId(1L);
        receta.setNombre("Test Recipe");
        
        // Configurar respuesta del RestTemplate
        ResponseEntity<Receta> mockResponse = new ResponseEntity<>(receta, HttpStatus.OK);
        when(restTemplate.exchange(
            contains("/api/recetas_detalle/1"), 
            eq(HttpMethod.GET), 
            any(HttpEntity.class), 
            eq(Receta.class)
        )).thenReturn(mockResponse);
        
        // Ejecutar y verificar
        Optional<Receta> resultado = recetasService.obtenerRecetaPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("Test Recipe", resultado.get().getNombre());
    }    

    @Test
    void testObtenerRecetaPorId_RecetaNoEncontrada() {
        // Configurar token válido
        when(tokenStore.getToken()).thenReturn("valid-token");
        
        // Configurar respuesta vacía
        ResponseEntity<Receta> mockResponse = new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        when(restTemplate.exchange(
            contains("/api/recetas_detalle/999"), 
            eq(HttpMethod.GET), 
            any(HttpEntity.class), 
            eq(Receta.class)
        )).thenReturn(mockResponse);
        
        // Ejecutar y verificar
        Optional<Receta> resultado = recetasService.obtenerRecetaPorId(999L);
        assertFalse(resultado.isPresent());
    }

    @Test
    void testRegistrarUsuario_ErrorGeneral() {
        // Configuración
        User user = new User();
        user.setUsername("testUser");
        
        when(restTemplate.postForEntity(
            eq(MOCK_BACKEND_URL + "/api/register"), 
            eq(user), 
            eq(String.class)
        )).thenThrow(new RuntimeException("Error de conexión"));
        
        // Verificación
        Exception exception = assertThrows(Exception.class, () -> {
            recetasService.registerUser(user);
        });
        
        assertTrue(exception.getMessage().contains("Error al registrar el usuario"));
    }

    @Test
    void testRegistrarUsuario_UsuarioYaExiste() {
        // Configuración
        User user = new User("existingUser", "test@test.com", "password", "Test User");
        user.setUserType(1); // usuario normal
        
        // Simular error de usuario existente
        when(restTemplate.postForEntity(
            eq(MOCK_BACKEND_URL + "/api/register"), 
            eq(user), 
            eq(String.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST, "400 BAD REQUEST"));
        
        // Verificación
        Exception exception = assertThrows(Exception.class, () -> {
            recetasService.registerUser(user);
        });
        
        // Verificar el mensaje exacto que devuelve el servicio
        assertEquals("Error al registrar el usuario: 400 400 BAD REQUEST", exception.getMessage());
    }

    @Test
    void testListarUsuarios() {
        // Configurar token válido
        when(tokenStore.getToken()).thenReturn("valid-token");
        
        // Crear usuarios de prueba
        User user1 = new User("user1", "user1@test.com", "password1", "User One");
        user1.setId(1);
        user1.setUserType(0); // admin
    
        User user2 = new User("user2", "user2@test.com", "password2", "User Two");
        user2.setId(2);
        user2.setUserType(1); // usuario normal
        
        User[] users = { user1, user2 };
        
        // Configurar respuesta
        ResponseEntity<User[]> mockResponse = new ResponseEntity<>(users, HttpStatus.OK);
        when(restTemplate.exchange(
            eq(MOCK_BACKEND_URL + "/api/admin/users"),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(User[].class)
        )).thenReturn(mockResponse);
        
        // Ejecutar y verificar
        List<User> resultado = recetasService.listarUsuarios();
        assertEquals(2, resultado.size());
        assertEquals("user1", resultado.get(0).getUsername());
        assertEquals("user2", resultado.get(1).getUsername());
    }

    @Test
    void testObtenerUsuarioPorId() {
        // Configurar token válido
        when(tokenStore.getToken()).thenReturn("valid-token");
        
        // Crear usuario de prueba
        User user = new User("testUser", "test@test.com", "password", "Test User");
        user.setId(1);
        user.setUserType(1); // usuario normal
        
        // Configurar respuesta
        ResponseEntity<User> mockResponse = new ResponseEntity<>(user, HttpStatus.OK);
        when(restTemplate.exchange(
            eq(MOCK_BACKEND_URL + "/api/admin/users/1"),
            eq(HttpMethod.GET),
            any(HttpEntity.class),
            eq(User.class)
        )).thenReturn(mockResponse);
        
        // Ejecutar y verificar
        Optional<User> resultado = recetasService.obtenerUsuarioPorId(1L);
        assertTrue(resultado.isPresent());
        assertEquals("testUser", resultado.get().getUsername());
    }

    @Test
    void testActualizarUsuario() {
        // Configurar token válido
        when(tokenStore.getToken()).thenReturn("valid-token");
        
        // Crear usuario a actualizar
        User user = new User("updatedUser", "updated@test.com", "newPassword", "Updated User");
        user.setId(1);
        user.setUserType(0); // admin
        
        // Configurar respuesta
        ResponseEntity<String> mockResponse = new ResponseEntity<>("Usuario actualizado", HttpStatus.OK);
        when(restTemplate.exchange(
            eq(MOCK_BACKEND_URL + "/api/admin/users/1"),
            eq(HttpMethod.PUT),
            any(HttpEntity.class),
            eq(String.class)
        )).thenReturn(mockResponse);
        
        // Ejecutar y verificar
        String resultado = recetasService.actualizarUsuario(user);
        assertEquals("Usuario actualizado", resultado);
    }

    @Test
    void testRegistrarUsuario_ErrorGenerico() {
        User user = new User("testUser", "test@test.com", "password", "Test User");
        user.setUserType(1);
        
        when(restTemplate.postForEntity(
            eq(MOCK_BACKEND_URL + "/api/register"), 
            eq(user), 
            eq(String.class)
        )).thenThrow(new RuntimeException("Error de conexión"));
        
        Exception exception = assertThrows(Exception.class, () -> {
            recetasService.registerUser(user);
        });
        
        assertTrue(exception.getMessage().contains("Error al registrar el usuario"));
    }

    @Test
    void testRegistrarUsuario_Exitoso() {
        // Configuración
        User user = new User("newUser", "test@test.com", "password", "Test User");
        user.setUserType(1);
        
        String mensajeExito = "Usuario registrado exitosamente";
        when(restTemplate.postForEntity(
            eq(MOCK_BACKEND_URL + "/api/register"), 
            eq(user), 
            eq(String.class)
        )).thenReturn(new ResponseEntity<>(mensajeExito, HttpStatus.OK));
        
        // Verificación
        assertDoesNotThrow(() -> {
            String resultado = recetasService.registerUser(user);
            assertEquals(mensajeExito, resultado);
        });
    }

    @Test
    void testRegistrarUsuario_ErrorGeneral2() {
        // Configuración
        User user = new User("testUser", "test@test.com", "password", "Test User");
        user.setUserType(1);
        
        String mensajeError = "Error inesperado";
        when(restTemplate.postForEntity(
            eq(MOCK_BACKEND_URL + "/api/register"), 
            eq(user), 
            eq(String.class)
        )).thenThrow(new RuntimeException(mensajeError));
        
        // Verificación
        Exception exception = assertThrows(Exception.class, () -> {
            recetasService.registerUser(user);
        });
        
        assertEquals("Error al registrar el usuario: " + mensajeError, exception.getMessage());
    }

    @Test
    void testRegistrarUsuario_BadRequest() {
        // Configuración
        User user = new User("existingUser", "test@test.com", "password", "Test User");
        user.setUserType(1);
        
        // Simular específicamente HttpClientErrorException.BadRequest
        when(restTemplate.postForEntity(
            eq(MOCK_BACKEND_URL + "/api/register"), 
            eq(user), 
            eq(String.class)
        )).thenThrow(new HttpClientErrorException(HttpStatus.BAD_REQUEST));
        
        // Verificación
        Exception exception = assertThrows(Exception.class, () -> {
            recetasService.registerUser(user);
        });
        
        assertEquals("Error al registrar el usuario: 400 BAD_REQUEST", exception.getMessage());
    }

}