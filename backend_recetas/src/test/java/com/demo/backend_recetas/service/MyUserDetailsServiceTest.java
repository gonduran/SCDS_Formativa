package com.demo.backend_recetas.service;

import com.demo.backend_recetas.model.User;
import com.demo.backend_recetas.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Collection;

@ExtendWith(MockitoExtension.class)
public class MyUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private MyUserDetailsService userDetailsService;

    // Objetos para usar en las pruebas
    private User normalUser;
    private User adminUser;

    @BeforeEach
    void setUp() {
        // Usuario normal
        normalUser = new User();
        normalUser.setId(1);
        normalUser.setUsername("user");
        normalUser.setPassword("password123");
        normalUser.setEnabled(true);
        normalUser.setUserType(1);

        // Usuario administrador
        adminUser = new User();
        adminUser.setId(2);
        adminUser.setUsername("admin");
        adminUser.setPassword("admin123");
        adminUser.setEnabled(true);
        adminUser.setUserType(0);
    }

    @Test
    @DisplayName("Usuario normal se carga correctamente con roles apropiados")
    void loadUserByUsername_NormalUserSuccess() {
        when(userRepository.findByUsername("user")).thenReturn(normalUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername("user");

        assertNotNull(userDetails);
        assertEquals("user", userDetails.getUsername());
        assertEquals("password123", userDetails.getPassword());
        assertTrue(userDetails.isEnabled());
        
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertTrue(authorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertFalse(authorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Usuario administrador se carga correctamente con roles apropiados")
    void loadUserByUsername_AdminUserSuccess() {
        when(userRepository.findByUsername("admin")).thenReturn(adminUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername("admin");

        assertNotNull(userDetails);
        assertEquals("admin", userDetails.getUsername());
        
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        assertTrue(authorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_USER")));
        assertTrue(authorities.stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")));
    }

    @Test
    @DisplayName("Lanza excepción cuando el usuario no existe")
    void loadUserByUsername_UserNotFound() {
        when(userRepository.findByUsername("nonexistent")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("nonexistent");
        });
    }

    @Test
    @DisplayName("Usuario deshabilitado mantiene su estado correctamente")
    void loadUserByUsername_DisabledUser() {
        normalUser.setEnabled(false);
        when(userRepository.findByUsername("user")).thenReturn(normalUser);

        UserDetails userDetails = userDetailsService.loadUserByUsername("user");

        assertFalse(userDetails.isEnabled());
    }
}