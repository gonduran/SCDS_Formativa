package com.demo.web_recetas;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class WebRecetasApplicationTests {

    @Test
    @DisplayName("Verifica que el contexto se carga correctamente")
    void contextLoads() {
        // Verifica que Spring Boot inicia correctamente
    }

    @Test
    @DisplayName("Verifica que el método main se ejecuta correctamente")
    void main_ExecutesSuccessfully() {
        // Using mockito to avoid actually starting the application
        try (MockedStatic<SpringApplication> mockedStatic = Mockito.mockStatic(SpringApplication.class)) {
            // Arrange
            String[] args = new String[]{"arg1", "arg2"};
            
            // Act
            WebRecetasApplication.main(args);
            
            // Assert
            mockedStatic.verify(
                () -> SpringApplication.run(
                    WebRecetasApplication.class,
                    args
                ),
                Mockito.times(1)
            );
        }
    }

    @Test
    @DisplayName("Verifica que la aplicación se puede instanciar")
    void constructor_CreatesInstance() {
        // Act
        WebRecetasApplication application = new WebRecetasApplication();
        
        // Assert
        assertNotNull(application);
    }

    @Test
    @DisplayName("Verifica que la aplicación se inicia sin argumentos")
    void main_ExecutesWithoutArgs() {
        try (MockedStatic<SpringApplication> mockedStatic = Mockito.mockStatic(SpringApplication.class)) {
            // Act
            WebRecetasApplication.main(new String[]{});
            
            // Assert
            mockedStatic.verify(
                () -> SpringApplication.run(
                    WebRecetasApplication.class,
                    new String[]{}
                ),
                Mockito.times(1)
            );
        }
    }
}