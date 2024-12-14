package com.demo.web_recetas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity; 
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import com.demo.web_recetas.integration.CustomAuthenticationProvider;
import com.demo.web_recetas.integration.TokenStore;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;
 
@Configuration 
@EnableWebSecurity(debug = true) 
public class WebSecurityConfig {

    @Value("${app.security.paths.login}")
    private String pathLogin;

    @Value("${app.security.paths.home}")
    private String pathHome;

    @Value("${app.security.paths.buscar}")
    private String pathBuscar;

    @Value("${app.security.paths.register}")
    private String pathRegister;

    @Value("${app.security.paths.recetas}")
    private String pathRecetas;

    @Value("${app.security.paths.publicar}")
    private String pathPublicar;

    @Value("${app.security.paths.comentarios}")
    private String pathComentarios;

    @Value("${app.security.paths.media}")
    private String pathMedia;

    @Value("${app.security.paths.listarusuarios}")
    private String pathListarUsuarios;

    @Value("${app.security.paths.editarusuario}")
    private String pathEditarUsuario;

    @Value("${app.security.paths.accesodenegado}")
    private String pathAccesoDenegado;

    @Value("${app.security.paths.404}")
    private String path404;

    @Value("${app.security.paths.listarcomentarios}")
    private String pathListarComentarios;

    @Value("${app.security.paths.editarcomentario}")
    private String pathEditarComentario;

    @Value("${app.security.paths.comentariosmethod}")
    private String pathComentarioMethod;

    @Value("${app.security.paths.roladmin}")
    private String pathRolAdmin;

    @Autowired
    private TokenStore tokenStore;

    @Bean 
    public CustomAuthenticationProvider customAuthenticationProvider() {
        return new CustomAuthenticationProvider(tokenStore);
    }

    @Bean 
    public AuthenticationManager authenticationManager(
            HttpSecurity http, 
            CustomAuthenticationProvider authProvider) throws Exception { 
        AuthenticationManagerBuilder authenticationManagerBuilder =  
            http.getSharedObject(AuthenticationManagerBuilder.class); 
        authenticationManagerBuilder.authenticationProvider(authProvider); 
        return authenticationManagerBuilder.build(); 
    }
 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                // Bloquear explícitamente accesos a sistemas de control de versiones
                .requestMatchers(
                    "/_darcs",
                    "/_darcs/*",
                    "/.bzr",
                    "/.bzr/*",
                    "/.hg",
                    "/.hg/*",
                    "/BitKeeper",
                    "/BitKeeper/*",
                    "/.git",
                    "/.git/*",
                    "/.svn",
                    "/.svn/*"
                ).denyAll()
                // Solo permitir acceso a la raíz
                .requestMatchers("/").permitAll()                
                // Permitir recursos estáticos
                .requestMatchers("/css/**", "/js/**", "/images/**", "/webjars/**").permitAll()
                // Endpoints públicos específicos
                .requestMatchers(pathLogin).permitAll()
                .requestMatchers(pathHome).permitAll()
                .requestMatchers(pathBuscar).permitAll()
                .requestMatchers(pathRegister).permitAll()
                .requestMatchers(pathAccesoDenegado).permitAll()
                .requestMatchers(path404).permitAll()
                // Endpoints privados que requiere autenticación
                .requestMatchers(pathRecetas).authenticated()
                .requestMatchers(pathPublicar).authenticated()
                .requestMatchers(pathComentarios).authenticated()
                .requestMatchers(pathMedia).authenticated()
                // Endpoints privados para administración de usuarios
                .requestMatchers(pathListarUsuarios).hasRole(pathRolAdmin) // Listar usuarios
                .requestMatchers(pathEditarUsuario).hasRole(pathRolAdmin) // Editar usuario
                // Endpoints privados para administración de comentarios
                .requestMatchers(pathListarComentarios).hasRole(pathRolAdmin) // Listar comentarios
                .requestMatchers(pathEditarComentario).hasRole(pathRolAdmin) // Editar comentario
                .requestMatchers(HttpMethod.GET, pathComentarioMethod).hasRole(pathRolAdmin)
                .requestMatchers(HttpMethod.POST, pathComentarioMethod).hasRole(pathRolAdmin)
                .requestMatchers(HttpMethod.PUT, pathComentarioMethod).hasRole(pathRolAdmin)
                .requestMatchers(HttpMethod.DELETE, pathComentarioMethod).hasRole(pathRolAdmin)
                
                // Cualquier otra ruta será denegada
                .anyRequest().denyAll()
            )
            .formLogin((form) -> form
                .loginPage(pathLogin)
                .failureUrl(pathLogin + "?error=true")
                .defaultSuccessUrl(pathHome, true)
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl(pathLogin + "?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
            )
            .exceptionHandling(exceptions -> exceptions
                .accessDeniedHandler((request, response, accessDeniedException) -> {
                    response.sendRedirect("/accesodenegado"); // Redirige a tu página personalizada
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    String path = request.getRequestURI();
                    if (path.matches("/recetas/\\d+") || path.equals(pathPublicar) || path.equals(pathListarUsuarios) || path.equals(pathEditarUsuario)) {
                        response.sendRedirect(pathLogin);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                })
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(false)
            )
            .csrf(csrf -> csrf
                .csrfTokenRepository(csrfTokenRepository())
            )
            .headers(headers -> headers
                .xssProtection(xss -> xss
                    .headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK)
                )
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives(
                        "default-src 'self';" +
                        "script-src 'self';" +
                        "style-src 'self';" +
                        "img-src 'self' data: https:;" +
                        "font-src 'self';" +
                        "object-src 'none';" +
                        "base-uri 'self';" +
                        "form-action 'self';" +
                        "frame-src 'self' https://www.youtube.com;" + // Permitir iframes de YouTube
                        "media-src 'self' https://www.youtube.com;" + // Permitir medios desde YouTube
                        "upgrade-insecure-requests;" +
                        "block-all-mixed-content;"
                    )
                )
                .frameOptions(frame -> frame.deny())
                .httpStrictTransportSecurity(hsts -> hsts
                    .includeSubDomains(true)
                    .preload(true)
                    .maxAgeInSeconds(31536000)
                )
                .referrerPolicy(referrer -> referrer
                    .policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.STRICT_ORIGIN_WHEN_CROSS_ORIGIN)
                )
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
            .requestMatchers(
                "/css/**",
                "/js/**",
                "/images/**",
                "/webjars/**",
                "/favicon.ico"
            );
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        serializer.setDomainName("localhost");
        serializer.setSameSite("Strict");
        serializer.setUseSecureCookie(true);
        serializer.setCookiePath("/");
        serializer.setCookieName("JSESSIONID");
        serializer.setCookieMaxAge(3600);
        return serializer;
    }

    @Bean
    public CsrfTokenRepository csrfTokenRepository() {
        HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
        repository.setHeaderName("X-CSRF-TOKEN");
        return repository;
    }

    @Bean
    public HiddenHttpMethodFilter hiddenHttpMethodFilter() {
        return new HiddenHttpMethodFilter();
    }
}