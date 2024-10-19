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
            Arrays.asList("2 cucharadas de aceite vegetal", "8 presas de pollo, sin piel", "Pimienta Negra Molida", "Sal de Mar", "1 cucharadita de Orégano Entero", 
            "1 cebolla picada finamente", "3/4 taza (180 ml) de coñac", "2 tazas (400 ml) de vino blanco", "500 ml de Caldo de Pollo Casero"),
            "Francia",
            "Precalentar una olla amplia a fuego medio-alto. Agregar el aceite y las presas de pollo y dorar por todos los lados hasta dorar. Trabajar en tandas para que se pueda dorar bien. Salpimentar.\r\n" + //
            "Añadir la cebolla y el orégano, y más aceite en caso de ser necesario. Cocinar por 5 minutos o hasta que esté translúcida.\r\n" + //
            "Agregar el coñac, una vez caliente, encender con un fósforo y flambear * hasta que se apague el fuego. Se debe ser muy cuidadoso (sobre todo no hay que tener prendida la campana), ya que se enciende la preparación.\r\n" + //
            "Verter el vino y dejar que evapore hasta la mitad.\r\n" + //
            "Agregar el caldo, el pollo y esperar que hierva a fuego medio. Desde ese momento, reducir el fuego hasta tener un hervor ligero y tapar.\r\n" + //
            "Cocinar por 20-30 min o hasta que esté cocido el pollo.\r\n" + //
            "Servir caliente acompañado de papas fritas o arroz.\r\n" + //
            "*Flambear: Tipo de cocción donde se rocía la preparación con un alcohol y una vez que esté caliente se enciende con fuego para evaporar el alcohol dejando el aroma y sabor en el plato.",
            "/images/receta1.jpg"
        ));
        recetas.add(new Receta(
            2L, 
            "Charquicán", 
            "Delicioso guiso tradicional de la gastronomía de Chile.", 
            "Chilena", 
            Arrays.asList("1 kilo de asiento o carne molida", "2 ½ cdas de aceite", "1 cebolla picada fina", "2 dientes de ajo picados fino", "6 papas en cubos de 1,5 cm",
            "400g de zapallo camote en cubos de 1,5 cm", "1,5 cdtas de Ají de Color", "½ cdta de Comino Molido", "2,5 cdtas de Orégano Entero ", "600ml de agua", 
            "1 sobre de Caldo en Polvo de Carne", "2 tazas de choclo", "2 tazas de porotos verdes cortados a lo largo"),
            "Chile",
            "Cortar la carne en cubos de 1 cm (si se usa carne molida, saltar este paso).\r\n" + //
            "En una olla grande, calentar el aceite a fuego medio. Agregar la carne y cocinar por 5 minutos. Agregar la cebolla y el ajo y cocinar hasta que la cebolla esté blanda y transparente.\r\n" + //
            "Agregar las papas y zapallo, revolver bien. Incorporar el Ají de Color, Comino Molido y Orégano Entero, revolver hasta integrar. Agregar el agua y el Caldo en Polvo de Carne. Dejar hervir y luego reducir el fuego y cocinar por 25 minutos o hasta que las verduras estén blandas.\r\n" + //
            "Por último, agregar el choclo y los porotos verdes, cocinar por 5 minutos o hasta que estén blandos.\r\n" + //
            "Con la ayuda de una cuchara de palo, aplastar levemente las papas y zapallo y servir.",
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
            Arrays.asList("Cebolla", "Tomates", "Perejil", "Comino", "Maicena"),
            "México",
            "Preparar la salsa, preparar el relleno y servir con salsa.",
            "/images/receta6.jpg"
        ));
    }

    public List<Receta> obtenerRecetas() {
        return recetas;
    }

    public Optional<Receta> obtenerRecetaPorId(Long id) {
        return recetas.stream()
            .filter(receta -> receta.getId().equals(id))
            .findFirst();
    }
}