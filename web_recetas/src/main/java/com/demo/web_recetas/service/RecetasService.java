package com.demo.web_recetas.service;

import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.integration.TokenStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
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
        //System.out.println("Token: " + token); 
        
        // Crear encabezados con el token de autenticación
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>("parameters", headers); 

        // Agregar parámetros a la URL 
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url) 
            .queryParam("name", id); 


        // Hacer la solicitud y obtener la respuesta
        ResponseEntity<Receta> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, entity, Receta.class);

        // Devolver la receta envuelta en Optional si la respuesta es exitosa
        return Optional.ofNullable(response.getBody());
    }

    // Método para buscar una receta por una clave de texto
    public List<Receta> buscarRecetas(String searchQuery) {
        String url = backendUrl + "/api/recetas_buscar?keyword=" + searchQuery;
        Receta[] recetasArray = restTemplate.getForObject(url, Receta[].class);
        return Arrays.asList(recetasArray);
    }
}