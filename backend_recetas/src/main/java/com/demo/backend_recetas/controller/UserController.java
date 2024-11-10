package com.demo.backend_recetas.controller;

import com.demo.backend_recetas.model.User;
import com.demo.backend_recetas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // Endpoint para registrar nuevos usuarios
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // Verificar si el nombre de usuario ya existe
        if (userRepository.findByUsername(user.getUsername()) != null) {
            return ResponseEntity.badRequest().body("Error: El nombre de usuario ya está en uso.");
        }

        // Verificar que el nombre completo no esté vacío
        if (user.getNombreCompleto() == null || user.getNombreCompleto().trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Error: El nombre completo es requerido.");
        }

        // Configurar el rol de usuario normal
        user.setUserType(1);  // 1 para usuario normal
        user.setPassword(passwordEncoder.encode(user.getPassword()));  // Encriptar la contraseña

        // Guardar el nuevo usuario en la base de datos
        userRepository.save(user);
        return ResponseEntity.ok("Usuario registrado exitosamente");
    }
}