package com.demo.web_recetas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ErrorController {

    @GetMapping("/accesodenegado")
    public String accessDenied() {
        return "accesodenegado"; // Nombre del archivo accesodenegado.html
    }
}