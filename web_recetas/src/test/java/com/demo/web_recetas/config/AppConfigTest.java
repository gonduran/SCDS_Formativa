package com.demo.web_recetas.config;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.web.client.RestTemplate;

public class AppConfigTest {

    @Test
    void testRestTemplateBeanCreation() {
        // Crear el contexto de la aplicación con AppConfig
        try (AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(AppConfig.class)) {
            // Verificar que el bean RestTemplate esté registrado en el contexto
            assertTrue(context.containsBean("restTemplate"), "El bean restTemplate debería estar registrado");

            // Obtener el bean RestTemplate del contexto
            RestTemplate restTemplate = context.getBean(RestTemplate.class);

            // Verificar que no sea nulo
            assertNotNull(restTemplate, "El bean RestTemplate debería estar inicializado");
        }
    }
}
