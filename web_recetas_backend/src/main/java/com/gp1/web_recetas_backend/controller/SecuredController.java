package com.gp1.web_recetas_backend.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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

    @GetMapping("/greetings")
    @PreAuthorize("hasRole('ROLE_USER')")
    public String greetings(@RequestParam(value="name", required=false) String name) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        if (name != null && !name.isEmpty()) {
            return "Hello " + name + "! (authenticated as " + username + ")";
        } else {
            return "Hello " + username + "!";
        }
    }
}