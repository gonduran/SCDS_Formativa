package com.demo.backend_recetas.repository;

import com.demo.backend_recetas.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void buscarPorUsername() {
        // Crear usuario
        User user = new User();
        user.setUsername("juanperez");
        user.setEmail("juan.perez@example.com");
        user.setUserType(1);
        
        // Guardar usuario
        userRepository.save(user);

        // Buscar por username
        User resultado = userRepository.findByUsername("juanperez");

        // Verificar
        assertNotNull(resultado);
        assertEquals("juanperez", resultado.getUsername());
    }

    @Test
    void buscarPorUserType() {
        // Crear usuario
        User user1 = new User();
        user1.setUsername("juanperez");
        user1.setEmail("juan.perez@example.com");
        user1.setUserType(1);

        User user2 = new User();
        user2.setUsername("mariagarcia");
        user2.setEmail("maria.garcia@example.com");
        user2.setUserType(2);

        // Guardar usuarios
        userRepository.save(user1);
        userRepository.save(user2);

        // Buscar por tipo de usuario
        List<User> resultado = userRepository.findByUserType(1);

        // Verificar
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.get(0).getUserType());
    }

    @Test
    void verificarExistenciaPorUsername() {
        // Crear usuario
        User user = new User();
        user.setUsername("juanperez");
        user.setEmail("juan.perez@example.com");
        user.setUserType(1);

        // Guardar usuario
        userRepository.save(user);

        // Verificar existencia por username
        boolean existe = userRepository.existsByUsername("juanperez");

        // Verificar
        assertTrue(existe);
    }

    @Test
    void verificarExistenciaPorEmail() {
        // Crear usuario
        User user = new User();
        user.setUsername("juanperez");
        user.setEmail("juan.perez@example.com");
        user.setUserType(1);

        // Guardar usuario
        userRepository.save(user);

        // Verificar existencia por email
        boolean existe = userRepository.existsByEmail("juan.perez@example.com");

        // Verificar
        assertTrue(existe);
    }
    
    @Test
    void buscarPorUsername_NoEncontrado() {
        // Buscar usuario que no existe
        User resultado = userRepository.findByUsername("usuarioInexistente");

        // Verificar que no se encontró el usuario
        assertNull(resultado);
    }
}
