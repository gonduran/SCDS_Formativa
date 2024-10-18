package com.demo.web_recetas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("message", "Bienvenido a la página de recetas más reciente y popular.");
        return "home";
    }

    @GetMapping("/")
    public String root(Model model) {
        model.addAttribute("message", "Bienvenido a la página de recetas más reciente y popular.");
        return "home";
    }
}