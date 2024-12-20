package com.demo.backend_recetas.controller;

import com.demo.backend_recetas.model.User;
import com.demo.backend_recetas.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

    //Endpoint para listar todos los usuarios (solo admin)
    @GetMapping("/admin/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userRepository.findAll();
        return ResponseEntity.ok(users);
    }

    //Endpoint para obtener un usuario específico por ID (solo admin)
    @GetMapping("/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable Integer id) {
        Optional<User> user = userRepository.findById(id);
        return user.map(ResponseEntity::ok)
                  .orElseGet(() -> ResponseEntity.notFound().build());
    }   

    //Endpoint para actualización administrativa de usuarios
    @PutMapping("/admin/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminUpdateUser(@PathVariable Integer id, @RequestBody User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        
        if (optionalUser.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        User user = optionalUser.get();
        
        // Validar username único si se está cambiando
        if (!user.getUsername().equals(userDetails.getUsername())) {
            User existingUser = userRepository.findByUsername(userDetails.getUsername());
            if (existingUser != null) {
                return ResponseEntity.badRequest()
                    .body("Error: El nombre de usuario ya está en uso.");
            }
        }

        // Actualizar todos los campos
        user.setUsername(userDetails.getUsername());
        user.setEmail(userDetails.getEmail());
        user.setNombreCompleto(userDetails.getNombreCompleto());
        user.setUserType(userDetails.getUserType());
        user.setEnabled(userDetails.isEnabled());

        // Actualizar contraseña solo si se proporciona una nueva
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }

        userRepository.save(user);
        return ResponseEntity.ok("Usuario actualizado exitosamente");
    }
}