package com.demo.web_recetas.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.demo.web_recetas.service.RecetasService;

@Controller
public class RecetasController {

    @Autowired
    private RecetasService recetasService;

    @GetMapping("/recetas")
    public String recetas(Model model) {
        model.addAttribute("recetas", recetasService.buscarRecetaPorId(1L));
        return "recetas";
    }
}