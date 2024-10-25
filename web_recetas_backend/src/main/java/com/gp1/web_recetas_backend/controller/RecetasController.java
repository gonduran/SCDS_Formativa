package com.gp1.web_recetas_backend.controller;

import com.gp1.web_recetas_backend.dto.RecetaDTO;
import com.gp1.web_recetas_backend.model.Receta;
import com.gp1.web_recetas_backend.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RecetasController {

    @Autowired
    private RecetaRepository recetaRepository;

    @GetMapping("/recetas")
    public ResponseEntity<List<RecetaDTO>> obtenerRecetas() {
        List<RecetaDTO> recetas = recetaRepository.findAll().stream()
            .map(receta -> new RecetaDTO(
                receta.getId(),
                receta.getNombre(),
                receta.getDescripcion(),
                receta.getImagen()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(recetas);
    }

    @GetMapping("/recetas_detalle/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> obtenerRecetaDetalle(@PathVariable Long id) {
        Optional<Receta> receta = recetaRepository.findById(id);
        
        if (receta.isPresent()) {
            return ResponseEntity.ok(receta.get());
        } else {
            return ResponseEntity
                .notFound()
                .build();
        }
    }
}