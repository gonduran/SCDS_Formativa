package com.demo.web_recetas.service;

import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Arrays;

@Service
public class RecetasService {

    public List<String> getRecetas() {
        return Arrays.asList(
            "Paella - Ingredientes: arroz, mariscos - País: España",
            "Tacos - Ingredientes: tortilla, carne - País: México",
            "Sushi - Ingredientes: arroz, pescado - País: Japón"
        );
    }
}