package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.Comentario;
import com.demo.web_recetas.service.RecetasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/recetas")
public class ComentarioController {

    @Autowired
    private RecetasService recetasService;

    @PostMapping("/{recetaId}/comentarios")
    public String agregarComentario(@PathVariable Long recetaId, @ModelAttribute Comentario comentario, Model model) {
        try {
            recetasService.agregarComentario(recetaId, comentario);
            return "redirect:/recetas/" + recetaId; // Redirige a la página de la receta
        } catch (Exception e) {
            model.addAttribute("errorMessage", "Hubo un error al agregar el comentario.");
            return "comentarios";
        }
    }
}