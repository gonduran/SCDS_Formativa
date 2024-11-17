package com.demo.web_recetas.service;

import com.demo.web_recetas.model.Comentario;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.model.User;
import com.demo.web_recetas.integration.TokenStore;
import com.demo.web_recetas.exception.TokenNotFoundException;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class RecetasService {

    private static final String PATH_TOKEN_EXCEPTION = "Token de autenticación no disponible";
    private static final String HEADER_AUTH = "Authorization";

    @Value("${backend.url}")
    private String backendUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenStore tokenStore;

    /**
     * Valida el token y lanza una excepción específica si no es válido
     */
    private void validarToken(String token) {
        if (token == null || token.isEmpty()) {
            throw new TokenNotFoundException(PATH_TOKEN_EXCEPTION);
        }
    }

    /**
     * Crea headers HTTP con el token de autorización
     */
    private HttpHeaders crearHeadersConToken() {
        String token = tokenStore.getToken();
        validarToken(token);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HEADER_AUTH, token);
        return headers;
    }

    // Método para obtener todas las recetas
    public List<Receta> obtenerRecetas() {
        Receta[] recetasArray = restTemplate.getForObject(backendUrl + "/api/recetas", Receta[].class);
        return Arrays.asList(recetasArray);
    }

    // Método para obtener una receta por ID
    public Optional<Receta> obtenerRecetaPorId(Long id) {
        String url = backendUrl + "/api/recetas_detalle/" + id;
        HttpHeaders headers = crearHeadersConToken();
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers);

        // Agregar parámetros a la URL
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("name", id);

        // Hacer la solicitud y obtener la respuesta
        ResponseEntity<Receta> response = restTemplate.exchange(
            builder.toUriString(), 
            HttpMethod.GET, 
            entity,
            Receta.class
        );

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
        HttpHeaders headers = crearHeadersConToken();
        HttpEntity<Receta> entity = new HttpEntity<>(receta, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url, 
            HttpMethod.POST, 
            entity, 
            String.class
        );

        return response.getBody();
    }

    public void agregarComentario(Long recetaId, Comentario comentario) {
        String url = backendUrl + "/api/recetas/" + recetaId + "/comentarios";
        HttpHeaders headers = crearHeadersConToken();
        HttpEntity<Comentario> entity = new HttpEntity<>(comentario, headers);

        restTemplate.exchange(
            url, 
            HttpMethod.POST, 
            entity, 
            String.class
        );
    }

    public void agregarMedia(Long recetaId, String fotos, String videos) {
        String url = backendUrl + "/api/recetas/" + recetaId + "/media";

        // Construir las listas a partir de las cadenas separadas por comas
        List<String> listaFotos = Arrays.asList(fotos.split(",\\s*"));
        List<String> listaVideos = Arrays.asList(videos.split(",\\s*"));

        HttpHeaders headers = crearHeadersConToken();

        // Cuerpo de la solicitud
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("fotos", listaFotos);
        requestBody.put("videos", listaVideos);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(
            url, 
            HttpMethod.POST, 
            entity, 
            String.class
        );
    }
}