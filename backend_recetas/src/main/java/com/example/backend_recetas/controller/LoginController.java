package com.example.backend_recetas.controller;

import com.example.backend_recetas.model.User;
import com.example.backend_recetas.jwt.JWTAuthtenticationConfig;
import com.example.backend_recetas.service.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoginController {

    @Autowired
    JWTAuthtenticationConfig jwtAuthtenticationConfig;

    @Autowired
    private MyUserDetailsService userDetailsService;

    @PostMapping("login")
    public String login(
            @RequestParam("user") String username,
            @RequestParam("encryptedPass") String encryptedPass) {

        /**
         * En el ejemplo no se realiza la correcta validación del usuario
         */

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        if (!userDetails.getPassword().equals(encryptedPass)) {
            throw new RuntimeException("Invalid login");
        }

        String token = jwtAuthtenticationConfig.getJWTToken(username);

        return token;

    }

}