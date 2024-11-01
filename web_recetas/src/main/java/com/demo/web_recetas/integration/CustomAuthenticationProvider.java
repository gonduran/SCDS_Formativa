package com.demo.web_recetas.integration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Value("${backend.url}")
    private String backendUrl;

    private TokenStore tokenStore;

    public CustomAuthenticationProvider(TokenStore tokenStore) {
        super();
        this.tokenStore = tokenStore;
    }

    @Override
    public Authentication authenticate(final Authentication authentication) throws AuthenticationException {

        final String name = authentication.getName();

        final String password = authentication.getCredentials().toString();

        final MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("user", name);
        requestBody.add("encryptedPass", password);

        final var restTemplate = new RestTemplate();
        String url = backendUrl + "/login";
        //final var responseEntity = restTemplate.postForEntity(url, requestBody, String.class);

        // Ejecuta la solicitud de login y obtiene la respuesta
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(url, requestBody, LoginResponse.class);

        // Extrae el token de la respuesta y lo guarda en TokenStore
        if (response.getBody() != null) {
            String token = response.getBody().getToken();
            tokenStore.setToken(token); // Guarda solo el token
        }

        if (response.getStatusCode() != HttpStatus.OK) {
            throw new BadCredentialsException("Nombre usuario o contraseña no válidos");
        }

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));

        Authentication authenticatedToken = new UsernamePasswordAuthenticationToken(name, password,
                authorities);

        return authenticatedToken;

    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
