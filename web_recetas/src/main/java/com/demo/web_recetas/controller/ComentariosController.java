package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.Comentario;
import com.demo.web_recetas.service.ComentariosService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/comentarios")
public class ComentariosController {
    @Autowired
    private ComentariosService comentariosService;

    @GetMapping
    public String listarComentarios(@RequestParam(value = "estado", required = false) Integer estado, Model model) {
        List<Comentario> comentarios;
        if (estado != null) {
            comentarios = comentariosService.listarPorEstado(estado);
        } else {
            comentarios = comentariosService.listarTodos();
        }
        model.addAttribute("comentarios", comentarios);
        return "comentarios";
    }

    @PostMapping("/{id}/aprobar")
    public String aprobarComentario(@PathVariable Long id) {
        comentariosService.actualizarEstadoComentario(id, 1); // Aprobado
        return "redirect:/comentarios";
    }

    @PostMapping("/{id}/rechazar")
    public String rechazarComentario(@PathVariable Long id) {
        comentariosService.actualizarEstadoComentario(id, 2); // Rechazado
        return "redirect:/comentarios";
    }

    @PostMapping("/{id}/eliminar")
    public String eliminarComentario(@PathVariable Long id) {
        comentariosService.eliminarComentario(id);
        return "redirect:/comentarios";
    }
}