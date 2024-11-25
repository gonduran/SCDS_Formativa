package com.demo.web_recetas.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    @Test
    void testDefaultConstructor() {
        User user = new User();
        assertNull(user.getId());
        assertNull(user.getUsername());
        assertNull(user.getEmail());
        assertNull(user.getPassword());
        assertNull(user.getNombreCompleto());
        assertNull(user.getUserType());
    }

    @Test
    void testParameterizedConstructor() {
        String username = "testUser";
        String email = "test@example.com";
        String password = "password";
        String nombreCompleto = "Test User";

        User user = new User(username, email, password, nombreCompleto);

        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(nombreCompleto, user.getNombreCompleto());
    }

    @Test
    void testSettersAndGetters() {
        User user = new User();

        Integer id = 1;
        String username = "testUser";
        String email = "test@example.com";
        String password = "password";
        String nombreCompleto = "Test User";
        Integer userType = 0;

        user.setId(id);
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(password);
        user.setNombreCompleto(nombreCompleto);
        user.setUserType(userType);

        assertEquals(id, user.getId());
        assertEquals(username, user.getUsername());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(nombreCompleto, user.getNombreCompleto());
        assertEquals(userType, user.getUserType());
    }
}