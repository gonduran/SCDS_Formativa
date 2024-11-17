package com.demo.web_recetas.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration; 
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

    @Bean 
    public TokenStore tokenStore() {
        return new TokenStore();
    }

    @Bean 
    public CustomAuthenticationProvider customAuthenticationProvider(TokenStore tokenStore) {
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
                // Endpoints privados que requiere autenticación
                .requestMatchers(pathRecetas).authenticated()
                .requestMatchers(pathPublicar).authenticated()
                .requestMatchers(pathComentarios).authenticated()
                .requestMatchers(pathMedia).authenticated()
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
                    response.sendError(HttpServletResponse.SC_NOT_FOUND);
                })
                .authenticationEntryPoint((request, response, authException) -> {
                    String path = request.getRequestURI();
                    if (path.matches("/recetas/\\d+") || path.equals(pathPublicar)) {
                        response.sendRedirect(pathLogin);
                    } else {
                        response.sendError(HttpServletResponse.SC_NOT_FOUND);
                    }
                })
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
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
                        "media-src 'self' https://youtu.be;" + // Permitir medios desde YouTube
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
}