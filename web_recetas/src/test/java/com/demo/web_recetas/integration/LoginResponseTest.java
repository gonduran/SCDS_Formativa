package com.demo.web_recetas.integration;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class LoginResponseTest {

    @Test
    void testSetAndGetToken() {
        LoginResponse loginResponse = new LoginResponse();
        String testToken = "test-token";

        // Validar que inicialmente el token es nulo
        assertNull(loginResponse.getToken(), "El token debería ser nulo inicialmente");

        // Establecer el token
        loginResponse.setToken(testToken);

        // Validar que el token establecido se devuelve correctamente
        assertEquals(testToken, loginResponse.getToken(), "El token debería coincidir con el valor establecido");
    }

    @Test
    void testSetAndGetUsername() {
        LoginResponse loginResponse = new LoginResponse();
        String testUsername = "test-username";

        // Validar que inicialmente el username es nulo
        assertNull(loginResponse.getUsername(), "El username debería ser nulo inicialmente");

        // Establecer el username
        loginResponse.setUsername(testUsername);

        // Validar que el username establecido se devuelve correctamente
        assertEquals(testUsername, loginResponse.getUsername(), "El username debería coincidir con el valor establecido");
    }

    @Test
    void testSetAndGetUserType() {
        LoginResponse loginResponse = new LoginResponse();
        Integer testUserType = 1;

        // Validar que inicialmente el userType es nulo
        assertNull(loginResponse.getUserType(), "El userType debería ser nulo inicialmente");

        // Establecer el userType
        loginResponse.setUserType(testUserType);

        // Validar que el userType establecido se devuelve correctamente
        assertEquals(testUserType, loginResponse.getUserType(), "El userType debería coincidir con el valor establecido");
    }
}