package com.demo.web_recetas.config;

import org.junit.jupiter.api.Test;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ThymeleafConfigTest {

    @Test
    void testSpringSecurityDialect() {
        // Arrange
        ThymeleafConfig thymeleafConfig = new ThymeleafConfig();

        // Act
        SpringSecurityDialect dialect = thymeleafConfig.springSecurityDialect();

        // Assert
        assertNotNull(dialect, "SpringSecurityDialect bean should not be null");
    }
}