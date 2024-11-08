package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
public class PublicarController {

    @Autowired
    private RecetasService recetasService;

    @GetMapping("/publicar")
    public String showPublicarForm(Model model) {
        model.addAttribute("receta", new Receta());
        return "publicar";
    }

    @PostMapping("/publicar")
    public String publicarReceta(@ModelAttribute Receta receta, Model model) {
        try {
            // Validar formato de tiempo de cocción
            if (!receta.getTiempoCoccion().matches("^([0-9]|[1-9][0-9]):([0-5][0-9])$")) {
                model.addAttribute("error", "El formato del tiempo de cocción debe ser HH:MM");
                return "publicar";
            }

            // Validar dificultad
            if (!Arrays.asList("Alta", "Media", "Baja").contains(receta.getDificultad())) {
                model.addAttribute("error", "La dificultad debe ser Alta, Media o Baja");
                return "publicar";
            }

            recetasService.publicarReceta(receta);
            model.addAttribute("message", "Receta publicada exitosamente.");
            return "redirect:/home"; // Redirige al inicio después de publicar
        } catch (Exception e) {
            model.addAttribute("error", "Hubo un error al publicar la receta: " + e.getMessage());
            return "publicar";
        }
    }
}