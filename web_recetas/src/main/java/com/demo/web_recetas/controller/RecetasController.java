package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@Controller
public class RecetasController {

    @Autowired
    private RecetasService recetasService;

    @GetMapping("/recetas/{id}")
    public String verDetalleReceta(@PathVariable Long id, Model model) {
        // Obtener la receta envuelta en Optional
        Optional<Receta> recetaOptional = recetasService.obtenerRecetaPorId(id);

        // Verificar si la receta está presente
        if (recetaOptional.isPresent()) {
            model.addAttribute("receta", recetaOptional.get());
            return "recetas"; // Renderizar la vista recetas.html
        } else {
            return "error/404"; // Mostrar página de error si no se encuentra la receta
        }
    }
}