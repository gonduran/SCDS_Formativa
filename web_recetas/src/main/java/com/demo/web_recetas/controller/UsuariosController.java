package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.User;
import com.demo.web_recetas.service.RecetasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    private RecetasService recetasService;

    /**
     * Muestra la lista de usuarios
     */
    @GetMapping
    public String listarUsuarios(Model model) {
        try {
            List<User> usuarios = recetasService.listarUsuarios();
            model.addAttribute("usuarios", usuarios);
            return "listarusuarios"; // Nombre del template Thymeleaf para listar usuarios
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener la lista de usuarios: " + e.getMessage());
            return "error"; // Página de error
        }
    }

    /**
     * Muestra el formulario para editar un usuario
     */
    @GetMapping("/{id}/editar")
    public String mostrarFormularioEdicion(@PathVariable("id") Long id, Model model) {
        try {
            Optional<User> usuarioOpt = recetasService.obtenerUsuarioPorId(id);
            if (usuarioOpt.isPresent()) {
                model.addAttribute("usuario", usuarioOpt.get());
                return "editarusuario"; // Nombre del template Thymeleaf para editar usuarios
            } else {
                model.addAttribute("error", "Usuario no encontrado");
                return "error"; // Página de error
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al obtener los datos del usuario: " + e.getMessage());
            return "error"; // Página de error
        }
    }

    /**
     * Procesa el formulario de edición de un usuario
     */
    @PostMapping("/{id}/editar")
    public String actualizarUsuario(
            @PathVariable("id") Integer id,
            @ModelAttribute("usuario") User usuario,
            Model model) {
        try {
            usuario.setId(id); // Asegurar que el ID coincide con el usuario que se edita
            String resultado = recetasService.actualizarUsuario(usuario);
            model.addAttribute("mensaje", "Usuario actualizado exitosamente");
            return "redirect:/usuarios"; // Redirigir a la lista de usuarios
        } catch (Exception e) {
            model.addAttribute("error", "Error al actualizar el usuario: " + e.getMessage());
            return "editarusuario"; // Volver a la página de edición en caso de error
        }
    }
}