package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.Comentario;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.model.User;
import com.demo.web_recetas.service.RecetasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User) {
                User user = (User) principal;
                model.addAttribute("nombreUsuario", user.getNombreCompleto());
            } else if (principal instanceof String) {
                model.addAttribute("nombreUsuario", principal); // Para casos simples
            }
            System.out.println("Nombre del usuario autenticado: " + model.getAttribute("nombreUsuario"));
        }

        // Agregar un objeto vacío para los comentarios
        model.addAttribute("nuevoComentario", new Comentario());

        // Verificar si la receta está presente
        if (recetaOptional.isPresent()) {
            model.addAttribute("receta", recetaOptional.get());
            return "recetas"; // Renderizar la vista recetas.html
        } else {
            return "error/404"; // Mostrar página de error si no se encuentra la receta
        }
    }

    @PostMapping("/{id}/comentarios")
    public String agregarComentario(@PathVariable Long id, @ModelAttribute Comentario nuevoComentario) {
        recetasService.agregarComentario(id, nuevoComentario);
        return "redirect:/recetas/" + id;
    }
}