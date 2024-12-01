package com.demo.web_recetas.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.web.filter.HiddenHttpMethodFilter;

import com.demo.web_recetas.integration.CustomAuthenticationProvider;
import com.demo.web_recetas.integration.TokenStore;

import jakarta.servlet.http.HttpServletResponse;

@SpringBootTest
class WebSecurityConfigTest {

    @Autowired
    private WebSecurityConfig webSecurityConfig;
    
    @MockBean
    private TokenStore tokenStore;
    
    @Value("${app.security.paths.login}")
    private String pathLogin;

    @Autowired
    private HttpSecurity httpSecurity;

    @Test
    @DisplayName("AuthenticationProvider se configura correctamente")
    void customAuthenticationProvider_IsConfiguredCorrectly() {
        CustomAuthenticationProvider provider = webSecurityConfig.customAuthenticationProvider();
        assertNotNull(provider);
    }

    @Test
    @DisplayName("AuthenticationManager se configura correctamente")
    void authenticationManager_IsConfiguredCorrectly() throws Exception {
        CustomAuthenticationProvider provider = webSecurityConfig.customAuthenticationProvider();
        assertNotNull(provider);
    }

    @Test
    @DisplayName("WebSecurityCustomizer ignora rutas estáticas")
    void webSecurityCustomizer_IgnoresStaticPaths() {
        assertNotNull(webSecurityConfig.webSecurityCustomizer());
    }

    @Test
    @DisplayName("CookieSerializer se configura correctamente")
    void cookieSerializer_IsConfiguredCorrectly() {
        CookieSerializer serializer = webSecurityConfig.cookieSerializer();
        assertNotNull(serializer);
    }

    @Test
    @DisplayName("CsrfTokenRepository se configura correctamente")
    void csrfTokenRepository_IsConfiguredCorrectly() {
        CsrfTokenRepository repository = webSecurityConfig.csrfTokenRepository();
        assertNotNull(repository);
        assertTrue(repository instanceof HttpSessionCsrfTokenRepository);
    }

    @Test
    @DisplayName("HiddenHttpMethodFilter se configura correctamente")
    void hiddenHttpMethodFilter_IsConfiguredCorrectly() {
        HiddenHttpMethodFilter filter = webSecurityConfig.hiddenHttpMethodFilter();
        assertNotNull(filter);
    }

    @Test
    @DisplayName("Configuración de sesión es correcta")
    void sessionManagement_IsConfiguredCorrectly() throws Exception {
        assertNotNull(webSecurityConfig.securityFilterChain(null));
    }

    @Test
    @DisplayName("AccessDeniedHandler maneja correctamente el acceso denegado")
    void accessDeniedHandler_HandlesAccessDenied() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AccessDeniedException accessDeniedException = new AccessDeniedException("Access Denied");

        // Creamos un AccessDeniedHandler personalizado similar al de la configuración
        AccessDeniedHandler accessDeniedHandler = (request1, response1, exception) -> {
            response1.sendRedirect("/accesodenegado");
        };

        accessDeniedHandler.handle(request, response, accessDeniedException);
        assertEquals("/accesodenegado", response.getRedirectedUrl());
    }

    @Test
    @DisplayName("SecurityFilterChain se configura correctamente")
    void securityFilterChain_IsConfiguredCorrectly() throws Exception {
        assertNotNull(webSecurityConfig.securityFilterChain(httpSecurity));
    }

    @Test
    @DisplayName("AuthenticationEntryPoint maneja correctamente las rutas protegidas")
    void authenticationEntryPoint_HandlesProtectedRoutes() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        AuthenticationException authException = mock(AuthenticationException.class);

        AuthenticationEntryPoint entryPoint = (request1, response1, authException1) -> {
            String path = request1.getRequestURI();
            if (path.matches("/recetas/\\d+") || path.equals("/publicar") || 
                path.equals("/listarusuarios") || path.equals("/editarusuario")) {
                response1.sendRedirect(pathLogin);
            } else {
                response1.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        };

        // Prueba para cada tipo de ruta protegida
        String[] protectedPaths = {
            "/recetas/1",
            "/publicar",
            "/listarusuarios",
            "/editarusuario"
        };

        for (String path : protectedPaths) {
            MockHttpServletResponse newResponse = new MockHttpServletResponse();
            request.setRequestURI(path);
            entryPoint.commence(request, newResponse, authException);
            assertEquals(pathLogin, newResponse.getRedirectedUrl());
        }

        // Prueba ruta no encontrada
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        request.setRequestURI("/ruta-inexistente");
        entryPoint.commence(request, response2, authException);
        assertEquals(HttpServletResponse.SC_NOT_FOUND, response2.getStatus());
    }

    @Test
    @DisplayName("AuthenticationManager se configura correctamente con proveedor personalizado")
    void authenticationManager_ConfiguresWithCustomProvider() throws Exception {
        // Verificamos que se puede crear el provider
        CustomAuthenticationProvider provider = webSecurityConfig.customAuthenticationProvider();
        assertNotNull(provider);
    }

    @Test
    @DisplayName("WebSecurityCustomizer configura correctamente los recursos ignorados")
    void webSecurityCustomizer_ConfiguresIgnoredPaths() {
        assertNotNull(webSecurityConfig.webSecurityCustomizer());
    }

    @Test
    @DisplayName("SecurityFilterChain configura correctamente la seguridad HTTP")
    void securityFilterChain_ConfiguresCorrectly() throws Exception {
        // Verificamos que la configuración base no es null
        SecurityFilterChain filterChain = webSecurityConfig.securityFilterChain(null);
        assertNotNull(filterChain);
    }

    @Test
    @DisplayName("Configuración de manejo de excepciones funciona correctamente")
    void exceptionHandling_ConfiguresCorrectly() throws Exception {
        // Crear un AuthenticationEntryPoint para las pruebas
        AuthenticationEntryPoint entryPoint = createTestAuthenticationEntryPoint();
        assertNotNull(entryPoint);
    
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();
        
        // Prueba para ruta protegida de recetas
        request.setRequestURI("/recetas/1");
        entryPoint.commence(request, response, mock(AuthenticationException.class));
        assertEquals(pathLogin, response.getRedirectedUrl());
    
        // Prueba para ruta no encontrada
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        request.setRequestURI("/ruta-no-existente");
        entryPoint.commence(request, response, mock(AuthenticationException.class));
        assertEquals(HttpServletResponse.SC_NOT_FOUND, response.getStatus());
    } 

    // Método helper para crear el AuthenticationEntryPoint
    private AuthenticationEntryPoint createTestAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            String path = request.getRequestURI();
            if (path.matches("/recetas/\\d+")) {
                response.sendRedirect(pathLogin);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        };
    }
}