package com.demo.web_recetas.controller;

import com.demo.web_recetas.integration.TokenStore;
import com.demo.web_recetas.model.Receta;
import com.demo.web_recetas.service.RecetasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.regex.Pattern;

@Controller
public class BuscarController {

    private TokenStore tokenStore; 

    public BuscarController(TokenStore tokenStore, RecetasService recetasService) {
        this.tokenStore = tokenStore;
        this.recetasService = recetasService;
    }

    @Autowired
    private RecetasService recetasService;

    private static final Pattern SEARCH_QUERY_PATTERN = Pattern.compile("^[A-Za-z0-9 ]+$");

    @GetMapping("/buscar")
    public String buscarRecetas(@RequestParam(name = "searchQuery", required = false) String searchQuery, Model model) {
        List<Receta> recetasFiltradas = null;
        
        if (searchQuery != null && !searchQuery.trim().isEmpty()) {
            // Validación personalizada del patrón
            if (!SEARCH_QUERY_PATTERN.matcher(searchQuery).matches()) {
                model.addAttribute("message", "Solo se permiten letras, números y espacios en la búsqueda.");
                return "buscar"; // Regresa la vista sin ejecutar la búsqueda si el patrón no es válido
            }
            // Sanea el valor de searchQuery para evitar XSS
            String sanitizedQuery = HtmlUtils.htmlEscape(searchQuery);
            
            // Llama al método del servicio con la consulta saneada
            recetasFiltradas = recetasService.buscarRecetas(sanitizedQuery);
            
            model.addAttribute("searchQuery", sanitizedQuery);
        } else {
            model.addAttribute("message", "Por favor, ingresa un criterio de búsqueda.");
        }

        model.addAttribute("recetasFiltradas", recetasFiltradas);
        return "buscar";
    }
}