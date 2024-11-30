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
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    @Value("${backend.url}")
    private String backendUrl;

    private final TokenStore tokenStore;

    public CustomAuthenticationProvider(TokenStore tokenStore) {
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

        try {
            // Realiza la solicitud de login al backend
            ResponseEntity<LoginResponse> response = restTemplate.postForEntity(url, requestBody, LoginResponse.class);

            // Valida la respuesta
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                // Extrae el token y lo guarda en TokenStore
                String token = response.getBody().getToken();
                tokenStore.setToken(token);
                Integer userType = response.getBody().getUserType();
                // Define las autoridades y retorna el token autenticado
                List<GrantedAuthority> authorities = new ArrayList<>();
                // Tipo 1 es usuario normal, solo tiene ROLE_USER
                if (userType == 1) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                }
                // Tipo 0 es administrador, tiene ambos roles
                else if (userType == 0) {
                    authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                    authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                }

                return new UsernamePasswordAuthenticationToken(name, password, authorities);
            } else {
                throw new BadCredentialsException("Nombre de usuario o contraseña no válidos");
            }

        } catch (HttpClientErrorException e) {
            // Captura el mensaje de error del backend en caso de un 401
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new BadCredentialsException("Credenciales inválidas: " + e.getResponseBodyAsString());
            }
            throw new BadCredentialsException("Error inesperado en la autenticación");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    public void clearAuthentication() {
        tokenStore.clearToken();
    }
}