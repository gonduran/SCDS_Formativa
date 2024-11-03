package com.demo.web_recetas.config;

import org.apache.catalina.connector.Connector;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomTomcatConfig {

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> customizeTomcat() {
        return factory -> factory.addConnectorCustomizers(this::customizeConnector);
    }

    private void customizeConnector(Connector connector) {
        connector.setProperty("server", ""); // Remueve la cabecera "Server"
    }
}