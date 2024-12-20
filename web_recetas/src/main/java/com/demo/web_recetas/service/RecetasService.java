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
    private static final String HEADER_PARAM = "parameters";

    @Value("${backend.url}")
    private String backendUrl;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private TokenStore tokenStore;

    // Setters para pruebas
    public void setBackendUrl(String backendUrl) {
        this.backendUrl = backendUrl;
    }

    public void setRestTemplate(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public void setTokenStore(TokenStore tokenStore) {
        this.tokenStore = tokenStore;
    }

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
        HttpEntity<String> entity = new HttpEntity<>(HEADER_PARAM, headers);

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

    /**
     * Método para listar todos los usuarios
     */
    public List<User> listarUsuarios() {
        String url = backendUrl + "/api/admin/users";
        HttpHeaders headers = crearHeadersConToken();
        HttpEntity<String> entity = new HttpEntity<>(HEADER_PARAM, headers);

        ResponseEntity<User[]> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            User[].class
        );

        return Arrays.asList(response.getBody());
    }

    /**
     * Método para obtener un usuario por ID
     */
    public Optional<User> obtenerUsuarioPorId(Long userId) {
        String url = backendUrl + "/api/admin/users/" + userId;
        HttpHeaders headers = crearHeadersConToken();
        HttpEntity<String> entity = new HttpEntity<>(HEADER_PARAM, headers);

        ResponseEntity<User> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            User.class
        );

        return Optional.ofNullable(response.getBody());
    }

    /**
     * Método para actualizar un usuario
     */
    public String actualizarUsuario(User user) {
        String url = backendUrl + "/api/admin/users/" + user.getId();
        HttpHeaders headers = crearHeadersConToken();
        HttpEntity<User> entity = new HttpEntity<>(user, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            url,
            HttpMethod.PUT,
            entity,
            String.class
        );

        return response.getBody();
    }
}