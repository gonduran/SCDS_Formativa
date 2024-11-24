package com.demo.backend_recetas.controller;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class SecuredController {

    @GetMapping("/public")
    public String publicEndpoint() {
        return "Este endpoint es público";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String userEndpoint() {
        return "Este endpoint es solo para usuarios autenticados";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String adminEndpoint() {
        return "Este endpoint es solo para administradores";
    }

    // Método modificado para incluir los roles del usuario
    @GetMapping("/greetings")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String greetings(@RequestParam(value="name", required=false) String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        // Obtener y formatear los roles del usuario
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String roles = authorities.stream()
            .map(GrantedAuthority::getAuthority)
            .map(role -> role.replace("ROLE_", "")) // Eliminar el prefijo ROLE_ para mejor presentación
            .collect(Collectors.joining(", "));
        
        if (name != null && !name.isEmpty()) {
            return String.format("Hello %s! (authenticated as %s, roles: [%s])", name, username, roles);
        } else {
            return String.format("Hello %s! (roles: [%s])", username, roles);
        }
    }
}