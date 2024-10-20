package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class BuscarController {

    @Autowired
    private RecetasService recetasService;

    @GetMapping("/buscar")
    public String buscarRecetas(@RequestParam(name = "searchQuery", required = false) String searchQuery, Model model) {
        List<Receta> recetasFiltradas = null;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // Convertir el término de búsqueda a minúsculas para búsqueda insensible a mayúsculas/minúsculas
            String searchLower = searchQuery.toLowerCase();
            
            // Filtrar recetas por varios campos: nombre, descripción, tipo de cocina, país de origen, y otros campos
            recetasFiltradas = recetasService.obtenerRecetas().stream()
                .filter(receta -> 
                    receta.getNombre().toLowerCase().contains(searchLower) ||
                    receta.getDescripcion().toLowerCase().contains(searchLower) ||
                    receta.getTipoCocina().toLowerCase().contains(searchLower) ||
                    receta.getPaisOrigen().toLowerCase().contains(searchLower)
                )
                .collect(Collectors.toList());
        }

        // Añadir recetas filtradas al modelo (si existen)
        model.addAttribute("recetasFiltradas", recetasFiltradas);
        model.addAttribute("searchQuery", searchQuery); // Para mantener el valor en el input
        return "buscar";
    }
}