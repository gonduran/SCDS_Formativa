package com.demo.backend_recetas.model;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;

public class UserTest {

    @Test
    @DisplayName("Constructor vacío crea instancia con valores por defecto")
    void constructorVacio_CreaInstanciaCorrecta() {
        // Act
        User user = new User();

        // Assert
        assertNotNull(user);
        assertEquals(1, user.getUserType()); // Verifica que por defecto es usuario normal
        assertTrue(user.isEnabled());
        assertTrue(user.isAccountNonExpired());
        assertTrue(user.isAccountNonLocked());
        assertTrue(user.isCredentialsNonExpired());
    }

    @Test
    @DisplayName("Constructor con parámetros inicializa todos los campos")
    void constructorParametrizado_CreaInstanciaCorrecta() {
        // Arrange
        String username = "testuser";
        String email = "test@test.com";
        String password = "password123";
        String nombreCompleto = "Test User";
        Integer userType = 1;

        // Act
        User user = new User(username, email, password, nombreCompleto, userType);

        // Assert
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(nombreCompleto, user.getNombreCompleto());
        assertEquals(userType, user.getUserType());
    }

    @Test
    @DisplayName("Usuario normal tiene solo rol de usuario")
    void getAuthorities_UsuarioNormal_RetornaRolCorrecto() {
        // Arrange
        User user = new User();
        user.setUserType(1);

        // Act & Assert
        assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertFalse(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Usuario admin tiene roles de admin y usuario")
    void getAuthorities_UsuarioAdmin_RetornaRolesCorrecto() {
        // Arrange
        User user = new User();
        user.setUserType(0);

        // Act & Assert
        assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_USER")));
        assertTrue(user.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Método isAdmin retorna valor correcto según tipo de usuario")
    void isAdmin_RetornaValorCorrecto() {
        // Arrange
        User userAdmin = new User();
        userAdmin.setUserType(0);

        User userNormal = new User();
        userNormal.setUserType(1);

        // Assert
        assertTrue(userAdmin.isAdmin());
        assertFalse(userNormal.isAdmin());
    }

    @Test
    @DisplayName("Setters y Getters funcionan correctamente para todos los campos")
    void settersAndGetters_FuncionanCorrectamente() {
        // Arrange
        User user = new User();

        // Act & Assert
        Integer id = 1;
        user.setId(id);
        assertEquals(id, user.getId());

        String username = "testuser";
        user.setUsername(username);
        assertEquals(username, user.getUsername());

        String email = "test@test.com";
        user.setEmail(email);
        assertEquals(email, user.getEmail());

        String password = "password123";
        user.setPassword(password);
        assertEquals(password, user.getPassword());

        String nombreCompleto = "Test User";
        user.setNombreCompleto(nombreCompleto);
        assertEquals(nombreCompleto, user.getNombreCompleto());

        Integer userType = 0;
        user.setUserType(userType);
        assertEquals(userType, user.getUserType());

        user.setEnabled(false);
        assertFalse(user.isEnabled());
    }
}