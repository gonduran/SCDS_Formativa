package com.demo.web_recetas.service;

import com.demo.web_recetas.model.Comentario;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.model.User;
import com.demo.web_recetas.integration.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpHeaders;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RecetasService {

    @Value("${backend.url}")
    private String backendUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenStore tokenStore;

    // Método para obtener todas las recetas
    public List<Receta> obtenerRecetas() {
        Receta[] recetasArray = restTemplate.getForObject(backendUrl + "/api/recetas", Receta[].class);
        return Arrays.asList(recetasArray);
    }

    // Método para obtener una receta por ID
    public Optional<Receta> obtenerRecetaPorId(Long id) {
        String url = backendUrl + "/api/recetas_detalle/" + id;

        // Asegurarse de que el token es válido
        String token = tokenStore.getToken();
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token de autenticación no disponible");
        }
        // System.out.println("Token: " + token);

        // Crear encabezados con el token de autenticación
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        // Agregar parámetros a la URL
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("name", id);

        // Hacer la solicitud y obtener la respuesta
        ResponseEntity<Receta> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity,
                Receta.class);

        // Devolver la receta envuelta en Optional si la respuesta es exitosa
        return Optional.ofNullable(response.getBody());
    }

    // Método para buscar una receta por una clave de texto
    public List<Receta> buscarRecetas(String searchQuery) {
        String url = backendUrl + "/api/recetas_buscar?keyword=" + searchQuery;
        Receta[] recetasArray = restTemplate.getForObject(url, Receta[].class);
        return Arrays.asList(recetasArray);
    }

    public String registerUser(User user) throws Exception {
        try {
            String url = backendUrl + "/api/register";
            ResponseEntity<String> response = restTemplate.postForEntity(url, user, String.class);
            return response.getBody();
        } catch (HttpClientErrorException.BadRequest e) {
            // Extrae el mensaje de error en caso de que el usuario ya esté registrado
            throw new Exception("El nombre de usuario ya está en uso.");
        } catch (Exception e) {
            throw new Exception("Error al registrar el usuario: " + e.getMessage());
        }
    }

    public String publicarReceta(Receta receta) {
        String url = backendUrl + "/api/recetas/publicar";

        // Asegurarse de que el token es válido
        String token = tokenStore.getToken();
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token de autenticación no disponible");
        }

        // Crea las cabeceras con el token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);

        // Configura la entidad de la solicitud con las cabeceras y el cuerpo
        HttpEntity<Receta> entity = new HttpEntity<>(receta, headers);

        // Envía la solicitud POST con el token JWT
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, entity, String.class);

        return response.getBody();
    }

    public void agregarComentario(Long recetaId, Comentario comentario) {
        String url = backendUrl + "/api/recetas/" + recetaId + "/comentarios";
    
        // Verificar que el token JWT esté disponible
        String token = tokenStore.getToken();
        if (token == null || token.isEmpty()) {
            throw new RuntimeException("Token de autenticación no disponible");
        }
    
        // Configurar las cabeceras con el token JWT
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
    
        // Crear la entidad de la solicitud con las cabeceras y el cuerpo
        HttpEntity<Comentario> entity = new HttpEntity<>(comentario, headers);
    
        // Enviar la solicitud POST con el token JWT
        restTemplate.exchange(url, HttpMethod.POST, entity, String.class);
    }
}