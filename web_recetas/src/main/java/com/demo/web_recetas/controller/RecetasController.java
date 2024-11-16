package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.Comentario;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Controller
@RequestMapping("/recetas")
public class RecetasController {

    @Autowired
    private RecetasService recetasService;

    @GetMapping("/{id}")
    public String verDetalleReceta(@PathVariable Long id, Model model) {
        // Obtener la receta envuelta en Optional
        Optional<Receta> recetaOptional = recetasService.obtenerRecetaPorId(id);

        // Obtener el usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String nombreUsuario = authentication.getName(); // Nombre del usuario autenticado

        // Verificar si la receta está presente
        if (recetaOptional.isPresent()) {
            model.addAttribute("receta", recetaOptional.get());
            model.addAttribute("comentarios", recetaOptional.get().getComentarios());
            // Pasar el usuario autenticado al modelo
            model.addAttribute("nombreUsuario", nombreUsuario);
            // Agregar un objeto vacío para los comentarios
            model.addAttribute("nuevoComentario", new Comentario());
            return "recetas"; // Renderizar la vista recetas.html
        } else {
            return "error/404"; // Mostrar página de error si no se encuentra la receta
        }
    }
}