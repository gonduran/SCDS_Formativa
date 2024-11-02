package com.demo.web_recetas.config;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration; 
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity; 
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import com.demo.web_recetas.integration.CustomAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
 
@Configuration 
@EnableWebSecurity(debug = true) 
public class WebSecurityConfig {

    @Autowired 
    private CustomAuthenticationProvider authProvider; 
 

    @Bean 
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception { 
        AuthenticationManagerBuilder authenticationManagerBuilder =  
            http.getSharedObject(AuthenticationManagerBuilder.class); 
        authenticationManagerBuilder.authenticationProvider(authProvider); 
        return authenticationManagerBuilder.build(); 
    } 
 
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers("/", "/home", "/login", "/buscar").permitAll() 
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                .anyRequest().authenticated()
            )
            .formLogin((form) -> form
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/home", true)
                .permitAll()
            )
            .logout((logout) -> logout
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .clearAuthentication(true)
                .permitAll()
            )
            .sessionManagement(session -> session
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
            )
            .headers(headers -> headers
                .frameOptions(frame -> frame.deny())
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; " +
                        "script-src 'self' 'unsafe-inline'; " +
                        "style-src 'self' 'unsafe-inline'; " +
                        "img-src 'self' data:; " +
                        "font-src 'self'; " +
                        "form-action 'self'; " +
                        "frame-ancestors 'none'")
                )
            )
            .csrf(csrf -> csrf.ignoringRequestMatchers("/login", "/logout"));

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
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
}