package com.demo.web_recetas.config;

import org.springframework.beans.factory.annotation.Autowired; 
import org.springframework.context.annotation.Bean; 
import org.springframework.context.annotation.Configuration; 
import org.springframework.security.authentication.AuthenticationManager; 
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder; 
import org.springframework.security.config.annotation.web.builders.HttpSecurity; 
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User; 
import org.springframework.security.core.userdetails.UserDetails; 
import org.springframework.security.core.userdetails.UserDetailsService; 
import org.springframework.security.provisioning.InMemoryUserDetailsManager; 
import org.springframework.security.web.SecurityFilterChain;
import com.demo.web_recetas.integration.CustomAuthenticationProvider;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; 
import org.springframework.context.annotation.Description;
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
                .permitAll()
            )
            // Configuración de la cabecera Content Security Policy
            .headers(headers -> headers
                .contentSecurityPolicy(csp -> csp
                    .policyDirectives("default-src 'self'; script-src 'self' 'unsafe-inline'; img-src 'self' data:; style-src 'self' 'unsafe-inline'")
                )
            );

        return http.build();
    }
/*
    @Bean
    @Description("In memory Userdetails service registered since DB doesn't have user table ")
    public UserDetailsService userDetailsService() {
        UserDetails user1 = User.builder()
                .username("user1")
                .password(passwordEncoder().encode("cl4v3web"))
                .roles("USER")
                .build();
        
        UserDetails user2 = User.builder()
                .username("user2")
                .password(passwordEncoder().encode("cl4v3web"))
                .roles("USER")
                .build();

        UserDetails user3 = User.builder()
                .username("user3")
                .password(passwordEncoder().encode("cl4v3web"))
                .roles("USER")
                .build();
        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("cl4v3web"))
                .roles("USER", "ADMIN")
                .build();
        return new InMemoryUserDetailsManager(user1, user2, user3, admin);
    }
 */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CookieSerializer cookieSerializer() {
        DefaultCookieSerializer serializer = new DefaultCookieSerializer();
        // Usamos solo domainName para desarrollo local
        serializer.setDomainName("localhost");
        serializer.setSameSite("strict");
        serializer.setUseSecureCookie(false); // false para desarrollo local, true para producción
        serializer.setCookiePath("/");
        serializer.setCookieName("JSESSIONID");
        serializer.setCookieMaxAge(3600); // 1 hora
        return serializer;
    }
}