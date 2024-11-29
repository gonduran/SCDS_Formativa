package com.demo.backend_recetas.controller;

import com.demo.backend_recetas.model.User;
import com.demo.backend_recetas.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.HttpStatus;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserController userController;

    // Objetos para usar en las pruebas
    private User testUser;
    private User adminUser;

    // Se ejecuta antes de cada test
    @BeforeEach
    void setUp() {
        // Usuario normal de prueba
        testUser = new User();
        testUser.setId(1);
        testUser.setUsername("testUser");
        testUser.setEmail("test@test.com");
        testUser.setPassword("password123");
        testUser.setNombreCompleto("Test User");
        testUser.setUserType(1);
        testUser.setEnabled(true);

        // Usuario administrador de prueba
        adminUser = new User();
        adminUser.setId(2);
        adminUser.setUsername("admin");
        adminUser.setEmail("admin@test.com");
        adminUser.setPassword("admin123");
        adminUser.setNombreCompleto("Admin User");
        adminUser.setUserType(0);
        adminUser.setEnabled(true);
    }

    @Test
    @DisplayName("Registro de usuario exitoso")
    void registerUser_Success() {
        when(userRepository.findByUsername("testUser")).thenReturn(null);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        ResponseEntity<String> response = userController.registerUser(testUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Usuario registrado exitosamente", response.getBody());
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode(any());
    }

    @Test
    @DisplayName("Registro falla cuando el nombre de usuario ya existe")
    void registerUser_UsernameExists() {
        when(userRepository.findByUsername("testUser")).thenReturn(testUser);

        ResponseEntity<String> response = userController.registerUser(testUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: El nombre de usuario ya está en uso.", response.getBody());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Registro falla cuando el nombre completo está vacío")
    void registerUser_EmptyName() {
        testUser.setNombreCompleto("");

        ResponseEntity<String> response = userController.registerUser(testUser);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error: El nombre completo es requerido.", response.getBody());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Actualización de usuario por admin exitosa")
    void adminUpdateUser_Success() {
        when(userRepository.findById(1)).thenReturn(java.util.Optional.of(testUser));
        when(userRepository.findByUsername("updatedUser")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updatedUser = new User();
        updatedUser.setUsername("updatedUser");
        updatedUser.setEmail("updated@test.com");
        updatedUser.setNombreCompleto("Updated User");
        updatedUser.setUserType(1);

        ResponseEntity<?> response = userController.adminUpdateUser(1, updatedUser);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("Actualización falla cuando el usuario no existe")
    void adminUpdateUser_UserNotFound() {
        when(userRepository.findById(999)).thenReturn(java.util.Optional.empty());

        ResponseEntity<?> response = userController.adminUpdateUser(999, testUser);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Obtener lista de todos los usuarios")
    void getAllUsers_Success() {
        // Arrange
        List<User> userList = Arrays.asList(testUser, adminUser);
        when(userRepository.findAll()).thenReturn(userList);

        // Act
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        verify(userRepository).findAll();
    }

    @Test
    @DisplayName("Obtener usuario por ID exitoso")
    void getUserById_Success() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        // Act
        ResponseEntity<?> response = userController.getUserById(1);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testUser, response.getBody());
    }

    @Test
    @DisplayName("Obtener usuario retorna not found cuando no existe")
    void getUserById_NotFound() {
        // Arrange
        when(userRepository.findById(999)).thenReturn(Optional.empty());

        // Act
        ResponseEntity<?> response = userController.getUserById(999);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Actualización admin falla cuando nuevo username ya existe")
    void adminUpdateUser_NewUsernameExists() {
        // Arrange
        User existingUser = new User();
        existingUser.setUsername("existingUser");
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(userRepository.findByUsername("existingUser")).thenReturn(existingUser);

        User updatedDetails = new User();
        updatedDetails.setUsername("existingUser");

        // Act
        ResponseEntity<?> response = userController.adminUpdateUser(1, updatedDetails);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("nombre de usuario ya está en uso"));
    }

    @Test
    @DisplayName("Actualización admin actualiza contraseña si se proporciona")
    void adminUpdateUser_UpdatesPassword() {
        // Arrange
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(testUser);

        User updatedDetails = new User();
        updatedDetails.setUsername(testUser.getUsername());
        updatedDetails.setPassword("newPassword");

        // Act
        ResponseEntity<?> response = userController.adminUpdateUser(1, updatedDetails);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(passwordEncoder).encode("newPassword");
        verify(userRepository).save(any(User.class));
    }
}