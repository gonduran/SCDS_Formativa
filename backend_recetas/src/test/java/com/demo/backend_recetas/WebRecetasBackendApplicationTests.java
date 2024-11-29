package com.demo.backend_recetas;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.SpringApplication;

@SpringBootTest(
    properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.sql.init.mode=always"
    }
)
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application-test.properties")
public class WebRecetasBackendApplicationTests {

    @Test
    @DisplayName("Verifica que el contexto de Spring se carga correctamente")
    void contextLoads() {
        // Verifica que el contexto de Spring se carga correctamente
    }

    @Test
    @DisplayName("Verifica que el método main se ejecuta correctamente")
    void main_SeEjecutaCorrectamente() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            // Arrange
            String[] args = new String[]{};
            
            // Act
            WebRecetasBackendApplication.main(args);
            
            // Assert
            mocked.verify(() -> 
                SpringApplication.run(WebRecetasBackendApplication.class, args),
                Mockito.times(1)
            );
        }
    }

    @Test
    @DisplayName("Verifica que el método main maneja argumentos correctamente")
    void main_ManejaArgumentosCorrectamente() {
        try (MockedStatic<SpringApplication> mocked = Mockito.mockStatic(SpringApplication.class)) {
            // Arrange
            String[] args = new String[]{"--server.port=8081"};
            
            // Act
            WebRecetasBackendApplication.main(args);
            
            // Assert
            mocked.verify(() -> 
                SpringApplication.run(WebRecetasBackendApplication.class, args),
                Mockito.times(1)
            );
        }
    }
}