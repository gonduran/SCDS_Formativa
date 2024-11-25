package com.demo.web_recetas.config;

import org.apache.catalina.connector.Connector;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;

public class CustomTomcatConfigTest {

    @Test
    void testCustomizeTomcat() {
        // Arrange
        CustomTomcatConfig customTomcatConfig = new CustomTomcatConfig();
        TomcatServletWebServerFactory factory = Mockito.mock(TomcatServletWebServerFactory.class);

        // Act
        WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizer = customTomcatConfig.customizeTomcat();

        // Assert
        assertNotNull(customizer, "The customizer should not be null");

        // Act again to apply customization
        customizer.customize(factory);

        // Verify
        verify(factory).addConnectorCustomizers(Mockito.any());
    }

    @Test
    void testCustomizeConnector() throws Exception {
        // Arrange
        CustomTomcatConfig customTomcatConfig = new CustomTomcatConfig();
        Connector connector = new Connector();

        // Access the private method using reflection
        var method = CustomTomcatConfig.class.getDeclaredMethod("customizeConnector", Connector.class);
        method.setAccessible(true);

        // Act
        method.invoke(customTomcatConfig, connector);

        // Assert
        String serverProperty = (String) connector.getProperty("server");
        assertNotNull(serverProperty, "The server property should not be null");
        assertEquals("", serverProperty, "The server property should be empty to remove the Server header");
    }
}