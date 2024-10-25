package com.gp1.web_recetas_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.gp1.web_recetas_backend.model.User;
import com.gp1.web_recetas_backend.model.Receta;
import com.gp1.web_recetas_backend.repository.UserRepository;
import com.gp1.web_recetas_backend.repository.RecetaRepository;

import java.util.Arrays;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RecetaRepository recetaRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar usuarios si no existen
        if (userRepository.count() == 0) {
            createUser("user1", "user1@example.com", "cl4v3web", 1);
            createUser("user2", "user2@example.com", "cl4v3web", 1);
            createUser("user3", "user3@example.com", "cl4v3web", 1);
            createUser("admin", "admin@example.com", "cl4v3web", 0);
            System.out.println("✅ Usuarios iniciales creados exitosamente");
        }

        // Inicializar recetas si no existen
        if (recetaRepository.count() == 0) {
            recetaRepository.save(new Receta(
                1L, 
                "Pollo al Coñac",
                "Delicioso pollo al coñac con arroz blanco y ensalada fresca.",
                "Francesa",
                Arrays.asList("Arroz", "Mariscos", "Azafrán", "Pimiento", "Caldo de pescado"),
                "Francia",
                "Preparar el caldo de pescado, sofreír los ingredientes, agregar arroz y cocinar lentamente.",
                "/images/receta1.jpg"
            ));

            recetaRepository.save(new Receta(
                2L, 
                "Charquicán",
                "Delicioso guiso tradicional de la gastronomía de Chile.",
                "Chilena",
                Arrays.asList("Tortilla de maíz", "Carne asada", "Guacamole", "Cebolla", "Cilantro"),
                "Chile",
                "Asar la carne, preparar el guacamole, calentar las tortillas y servir.",
                "/images/receta2.jpg"
            ));

            recetaRepository.save(new Receta(
                3L,
                "Sushi Japonés",
                "Sushi tradicional japonés con pescado fresco y arroz.",
                "Japonesa",
                Arrays.asList("Arroz", "Pescado fresco", "Alga nori", "Vinagre de arroz"),
                "Japón",
                "Cocinar el arroz, preparar los rollos con alga nori y el pescado, cortar y servir.",
                "/images/receta3.jpg"
            ));

            recetaRepository.save(new Receta(
                4L,
                "Pizza Italiana",
                "Pizza con masa fina, tomate, mozzarella y albahaca fresca.",
                "Italiana",
                Arrays.asList("Harina", "Agua", "Levadura", "Tomate", "Mozzarella", "Albahaca"),
                "Italia",
                "Preparar la masa, añadir salsa de tomate, queso y hornear hasta dorar.",
                "/images/receta4.jpg"
            ));

            recetaRepository.save(new Receta(
                5L,
                "Falafel",
                "Bolas crujientes de garbanzo con una salsa de yogur.",
                "Medio Oriente",
                Arrays.asList("Garbanzos", "Ajo", "Perejil", "Cebolla", "Especias"),
                "Líbano",
                "Preparar la mezcla con garbanzos, freír las bolitas y servir con salsa de yogur.",
                "/images/receta5.jpg"
            ));

            recetaRepository.save(new Receta(
                6L,
                "Enchiladas Verdes",
                "Enchiladas rellenas de pollo con una salsa verde casera.",
                "Mexicana",
                Arrays.asList("Tortillas de maíz", "Pollo", "Tomatillos", "Crema", "Queso"),
                "México",
                "Preparar la salsa verde, rellenar las tortillas con pollo, bañar con la salsa y decorar con crema y queso.",
                "/images/receta6.jpg"
            ));

            System.out.println("✅ Recetas iniciales creadas exitosamente");
        }
    }

    private void createUser(String username, String email, String password, Integer userType) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setUserType(userType);
        userRepository.save(user);
    }
}