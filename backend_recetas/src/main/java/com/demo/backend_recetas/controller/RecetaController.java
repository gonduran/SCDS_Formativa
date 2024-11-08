package com.demo.backend_recetas.controller;

import com.demo.backend_recetas.model.Receta;
import com.demo.backend_recetas.service.RecetaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recetas")
public class RecetaController {

    @Autowired
    private RecetaService recetaService;

    @PostMapping("/publicar")
    public ResponseEntity<String> publicarReceta(@RequestBody Receta receta) {
        recetaService.guardarReceta(receta);
        return ResponseEntity.ok("Receta publicada exitosamente.");
    }
}