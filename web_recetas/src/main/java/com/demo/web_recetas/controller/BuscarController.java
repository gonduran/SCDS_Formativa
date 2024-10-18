package com.demo.web_recetas.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BuscarController {

    @GetMapping("/buscar")
    public String buscar() {
        return "buscar";
    }
}