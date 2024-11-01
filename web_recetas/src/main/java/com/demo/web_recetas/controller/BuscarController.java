package com.demo.web_recetas.controller;

import com.demo.web_recetas.integration.TokenStore;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class BuscarController {

    private TokenStore tokenStore; 

    public BuscarController(TokenStore tokenStore, RecetasService recetasService) {
        this.tokenStore = tokenStore;
        this.recetasService = recetasService;
    }

    @Autowired
    private RecetasService recetasService;

    @GetMapping("/buscar")
    public String buscarRecetas(@RequestParam(name = "searchQuery", required = false) String searchQuery, Model model) {
        List<Receta> recetasFiltradas = null;

        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // Llama al método del servicio para buscar las recetas en el backend
            recetasFiltradas = recetasService.buscarRecetas(searchQuery);
        }

        model.addAttribute("recetasFiltradas", recetasFiltradas);
        model.addAttribute("searchQuery", searchQuery);
        return "buscar";
    }
}