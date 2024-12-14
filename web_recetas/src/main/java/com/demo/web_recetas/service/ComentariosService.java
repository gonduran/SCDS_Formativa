package com.demo.web_recetas.service;

import com.demo.web_recetas.exception.TokenNotFoundException;
import com.demo.web_recetas.integration.TokenStore;
import com.demo.web_recetas.model.Comentario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ComentariosService {

    private static final String PATH_TOKEN_EXCEPTION = "Token de autenticación no disponible";
    private static final String HEADER_AUTH = "Authorization";
    private static final String HEADER_PARAM = "parameters";
    private static final String PARAM_ESTADO = "estado";
    private static final String PATH_BACKEND = "/api/admin/comentarios";
    private static final String PATH_BACKEND_COMENTARIO = "/api/admin/comentarios/";
    private static final String PATH_BACKEND_ESTADO = "/api/admin/comentarios/estado/";
    private static final String PATH_BACKEND_UPD_ESTADO = "/estado";

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
        headers.set(HEADER_AUTH, "Bearer " + token);
        return headers;
    }

    /**
     * Obtiene todos los comentarios.
     */
    public List<Comentario> listarTodos() {
        String url = backendUrl + PATH_BACKEND;

        HttpHeaders headers = crearHeadersConToken();
        HttpEntity<String> entity = new HttpEntity<>(HEADER_PARAM, headers);

        ResponseEntity<Comentario[]> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Comentario[].class
        );

        return Arrays.asList(response.getBody());
    }

    /**
     * Obtiene comentarios por estado.
     */
    public List<Comentario> listarPorEstado(Integer estado) {
        String url = backendUrl + PATH_BACKEND_ESTADO + estado;

        HttpHeaders headers = crearHeadersConToken();
        HttpEntity<String> entity = new HttpEntity<>(HEADER_PARAM, headers);

        ResponseEntity<Comentario[]> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Comentario[].class
        );

        return Arrays.asList(response.getBody());
    }

    /**
     * Obtiene un comentario por su ID.
     */
    public Optional<Comentario> obtenerPorId(Long id) {
        String url = backendUrl + PATH_BACKEND_COMENTARIO + id;

        HttpHeaders headers = crearHeadersConToken();
        HttpEntity<String> entity = new HttpEntity<>(HEADER_PARAM, headers);

        ResponseEntity<Comentario> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            entity,
            Comentario.class
        );

        return Optional.ofNullable(response.getBody());
    }

    /**
     * Actualiza el estado de un comentario.
     */
    public void actualizarEstadoComentario(Long id, Integer estado) {
        String url = backendUrl + PATH_BACKEND_COMENTARIO + id + PATH_BACKEND_UPD_ESTADO;

        Map<String, Integer> requestBody = new HashMap<>();
        requestBody.put(PARAM_ESTADO, estado);  // Cambio de "nuevoEstado" a "estado" para coincidir con EstadoComentarioDTO

        HttpHeaders headers = crearHeadersConToken();
        HttpEntity<Map<String, Integer>> entity = new HttpEntity<>(requestBody, headers);

        restTemplate.exchange(url, HttpMethod.PUT, entity, Void.class);
    }

    /**
     * Elimina un comentario por su ID.
     */
    public void eliminarComentario(Long id) {
        String url = backendUrl + PATH_BACKEND_COMENTARIO + id;

        HttpHeaders headers = crearHeadersConToken();
        HttpEntity<String> entity = new HttpEntity<>(HEADER_PARAM, headers);

        restTemplate.exchange(url, HttpMethod.DELETE, entity, Void.class);
    }
}