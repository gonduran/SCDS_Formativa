package com.demo.web_recetas.service;

import com.demo.web_recetas.model.Receta;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class RecetasService {

    private List<Receta> recetas;

    public RecetasService() {
        recetas = new ArrayList<>();
        recetas.add(new Receta(
            1L, 
            "Pollo al Coñac", 
            "Delicioso pollo al coñac con arroz blanco y ensalada fresca.", 
            "Francesa", 
            Arrays.asList("Arroz", "Mariscos", "Azafrán", "Pimiento", "Caldo de pescado"),
            "Francia",
            "Preparar el caldo de pescado, sofreír los ingredientes, agregar arroz y cocinar lentamente.",
            "/images/receta1.jpg"
        ));
        recetas.add(new Receta(
            2L, 
            "Charquicán", 
            "Delicioso guiso tradicional de la gastronomía de Chile.", 
            "Chilena", 
            Arrays.asList("Tortilla de maíz", "Carne asada", "Guacamole", "Cebolla", "Cilantro"),
            "Chile",
            "Asar la carne, preparar el guacamole, calentar las tortillas y servir.",
            "/images/receta2.jpg"
        ));
        recetas.add(new Receta(
            3L,
            "Sushi Japonés", 
            "Sushi tradicional japonés con pescado fresco y arroz.", 
            "Japonesa", 
            Arrays.asList("Arroz", "Pescado fresco", "Alga nori", "Vinagre de arroz"),
            "Japón",
            "Cocinar el arroz, preparar los rollos con alga nori y el pescado, cortar y servir.",
            "/images/receta3.jpg"
        ));
        recetas.add(new Receta(
            4L, 
            "Pizza Italiana", 
            "Pizza con masa fina, tomate, mozzarella y albahaca fresca.", 
            "Italiana", 
            Arrays.asList("Harina", "Agua", "Levadura", "Tomate", "Mozzarella", "Albahaca"),
            "Italia",
            "Preparar la masa, añadir salsa de tomate, queso y hornear hasta dorar.",
            "/images/receta4.jpg"
        ));
        recetas.add(new Receta(
            5L,  
            "Falafel", 
            "Bolas crujientes de garbanzo con una salsa de yogur.", 
            "Medio Oriente", 
            Arrays.asList("Garbanzos", "Ajo", "Perejil", "Cebolla", "Especias"),
            "Líbano",
            "Preparar la mezcla con garbanzos, freír las bolitas y servir con salsa de yogur.",
            "/images/receta5.jpg"
        ));
        recetas.add(new Receta(
            6L, 
            "Enchiladas Verdes", 
            "Enchiladas rellenas de pollo con una salsa verde casera.", 
            "Mexicana", 
            Arrays.asList("Garbanzos", "Ajo", "Perejil", "Cebolla", "Especias"),
            "México",
            "Preparar la mezcla con garbanzos, freír las bolitas y servir con salsa de yogur.",
            "/images/receta6.jpg"
        ));
    }

    public List<Receta> obtenerRecetas() {
        return recetas;
    }

    public Optional<Receta> buscarRecetaPorId(Long id) {
        return recetas.stream()
            .filter(receta -> receta.getId().equals(id))
            .findFirst();
    }
}