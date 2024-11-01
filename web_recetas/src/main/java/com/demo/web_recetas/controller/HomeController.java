package com.demo.web_recetas.controller;

import com.demo.web_recetas.integration.TokenStore;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

import java.util.HashMap; 
import java.util.Map; 
 
import org.springframework.http.HttpEntity; 
import org.springframework.http.HttpHeaders; 
import org.springframework.http.HttpMethod; 
import org.springframework.http.ResponseEntity; 
import org.springframework.ui.Model; 
import org.springframework.util.LinkedMultiValueMap; 
import org.springframework.util.MultiValueMap; 
import org.springframework.web.bind.annotation.RequestParam; 
import org.springframework.web.client.RestTemplate; 
import org.springframework.web.util.UriComponentsBuilder;  


@Controller
public class HomeController {

    private TokenStore tokenStore; 
 
    public HomeController(TokenStore tokenStore, RecetasService recetasService) {
        this.tokenStore = tokenStore;
        this.recetasService = recetasService;
    }

    @Autowired
    private RecetasService recetasService;

    @GetMapping("/home")
    public String home(Model model) {
        // Obtener las recetas desde el servicio
        List<Receta> recetas = recetasService.obtenerRecetas();
        
        // Pasar las recetas al modelo
        model.addAttribute("recetas", recetas);
        return "home";
    }

    @GetMapping("/")
    public String root(Model model) {
        // Obtener las recetas desde el servicio
        List<Receta> recetas = recetasService.obtenerRecetas();
        
        // Pasar las recetas al modelo
        model.addAttribute("recetas", recetas);
        return "home";
    }
}