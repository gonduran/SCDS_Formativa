package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.Comentario;
import com.demo.web_recetas.service.RecetasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/recetas")
public class ComentarioController {

    @Autowired
    private RecetasService recetasService;

    @PostMapping("/{recetaId}/comentarios")
    public String agregarComentario(@PathVariable Long recetaId, @ModelAttribute Comentario comentario, 
                                RedirectAttributes redirectAttributes) {
        try {
            recetasService.agregarComentario(recetaId, comentario);
            return "redirect:/recetas/" + recetaId;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Hubo un error al agregar el comentario.");
            return "redirect:/recetas/" + recetaId;
        }
    }
}