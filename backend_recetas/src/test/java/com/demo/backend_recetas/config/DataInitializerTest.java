package com.demo.backend_recetas.config;

import com.demo.backend_recetas.model.Receta;
import com.demo.backend_recetas.model.User;
import com.demo.backend_recetas.repository.RecetaRepository;
import com.demo.backend_recetas.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DataInitializerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RecetaRepository recetaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private DataInitializer dataInitializer;

    @Test
    void run_CuandoNoHayUsuarios_CreaUsuariosIniciales() throws Exception {
        // Arrange
        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // Act
        dataInitializer.run();

        // Assert
        verify(userRepository, times(4)).save(any(User.class));
        verify(passwordEncoder, times(4)).encode(anyString());
    }

    @Test
    void run_CuandoYaExistenUsuarios_NoCreaNuevosUsuarios() throws Exception {
        // Arrange
        when(userRepository.count()).thenReturn(4L);

        // Act
        dataInitializer.run();

        // Assert
        verify(userRepository, never()).save(any(User.class));
        verify(passwordEncoder, never()).encode(anyString());
    }

    @Test
    void run_CuandoNoHayRecetas_CreaRecetasIniciales() throws Exception {
        // Arrange
        when(recetaRepository.count()).thenReturn(0L);

        // Act
        dataInitializer.run();

        // Assert
        verify(recetaRepository, times(6)).save(any(Receta.class));
    }

    @Test
    void run_CuandoYaExistenRecetas_NoCreaNuevasRecetas() throws Exception {
        // Arrange
        when(recetaRepository.count()).thenReturn(6L);

        // Act
        dataInitializer.run();

        // Assert
        verify(recetaRepository, never()).save(any(Receta.class));
    }

    @Test
    void createUser_CreaUsuarioCorrectamente() throws Exception {
        // Arrange
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.count()).thenReturn(0L);

        // Act
        dataInitializer.run();

        // Assert
        verify(userRepository, times(4)).save(argThat(user -> 
            user.getPassword().equals("encodedPassword") &&
            user.isEnabled() &&
            user.isAccountNonExpired() &&
            user.isAccountNonLocked() &&
            user.isCredentialsNonExpired()
        ));
    }
}