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

    private static final String PATH_PUBLICAR = "publicar";
    private static final String VIEW_PUBLICAR = "publicar";  // Para el nombre de la vista
    private static final String REDIRECT_HOME = "redirect:/home"; 
    private static final String ATTR_ERROR = "error";   
    private static final String ATTR_RECETA = "receta";
    private static final String ATTR_MESSAGE = "message";

    @Autowired
    private RecetasService recetasService;

    @GetMapping("/" + PATH_PUBLICAR)
    public String showPublicarForm(Model model) {
        model.addAttribute(ATTR_RECETA, new Receta());
        return VIEW_PUBLICAR;
    }

    @PostMapping("/" + PATH_PUBLICAR)
    public String publicarReceta(@ModelAttribute Receta receta, Model model) {
        try {
            // Validar formato de tiempo de cocción
            if (!receta.getTiempoCoccion().matches("^([0-9]|[1-9][0-9]):([0-5][0-9])$")) {
                model.addAttribute(ATTR_ERROR, "El formato del tiempo de cocción debe ser HH:MM");
                return VIEW_PUBLICAR;
            }

            // Validar dificultad
            if (!Arrays.asList("Alta", "Media", "Baja").contains(receta.getDificultad())) {
                model.addAttribute(ATTR_ERROR, "La dificultad debe ser Alta, Media o Baja");
                return VIEW_PUBLICAR;
            }

            recetasService.publicarReceta(receta);
            model.addAttribute(ATTR_MESSAGE, "Receta publicada exitosamente.");
            return REDIRECT_HOME; // Redirige al inicio después de publicar
        } catch (Exception e) {
            model.addAttribute(ATTR_ERROR, "Hubo un error al publicar la receta: " + e.getMessage());
            return VIEW_PUBLICAR;
        }
    }
}