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

    @Autowired
    private RecetasService recetasService;

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(
            @ModelAttribute("user") User user, 
            @RequestParam("confirmPassword") String confirmPassword, 
            Model model) {
        // Verificación de coincidencia de contraseñas
        if (!user.getPassword().equals(confirmPassword)) {
            model.addAttribute("error", "Las contraseñas no coinciden.");
            return "register";
        }
        try {
            String response = recetasService.registerUser(user);
            model.addAttribute("message", response);
            return "login"; // Redirige al login después de registrarse
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "register";
        }
    }
}