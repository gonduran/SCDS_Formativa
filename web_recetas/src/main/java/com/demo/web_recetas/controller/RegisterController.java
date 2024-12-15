package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.User;
import com.demo.web_recetas.service.RecetasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegisterController {

    private static final String PATH_REGISTER = "register";
    private static final String VIEW_REGISTER = "register";
    private static final String VIEW_LOGIN = "login";
    private static final String ATTR_USER = "user";
    private static final String ATTR_ERROR = "error";
    private static final String ATTR_MESSAGE = "message";

    @Autowired
    private RecetasService recetasService;

    @GetMapping("/" + PATH_REGISTER)
    public String showRegisterForm(Model model) {
        model.addAttribute(ATTR_USER, new User());
        return VIEW_REGISTER;
    }

    @PostMapping("/" + PATH_REGISTER)
    public String registerUser(
            @ModelAttribute(ATTR_USER) User user, 
            @RequestParam("confirmPassword") String confirmPassword, 
            Model model) {

        // Verificación de campos requeridos
        if (user.getNombreCompleto() == null || user.getNombreCompleto().trim().isEmpty()) {
            model.addAttribute(ATTR_ERROR, "El nombre completo es requerido.");
            return VIEW_REGISTER;
        }
        
        // Verificación de coincidencia de contraseñas
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute(ATTR_ERROR, "Las contraseñas no coinciden.");
            return VIEW_REGISTER;
        }

        try {
            String response = recetasService.registerUser(user);
            model.addAttribute(ATTR_MESSAGE, response);
            return VIEW_LOGIN; // Redirige al login después de registrarse
        } catch (Exception e) {
            model.addAttribute(ATTR_ERROR, e.getMessage());
            return VIEW_REGISTER;
        }

    }
}