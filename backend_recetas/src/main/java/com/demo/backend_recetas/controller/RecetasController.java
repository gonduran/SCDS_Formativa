package com.demo.backend_recetas.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.demo.backend_recetas.dto.RecetaBusquedaDTO;
import com.demo.backend_recetas.dto.RecetaDTO;
import com.demo.backend_recetas.model.Receta;
import com.demo.backend_recetas.repository.RecetaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class RecetasController {

    private final RecetaRepository recetaRepository;

    public RecetasController(RecetaRepository recetaRepository) {
        this.recetaRepository = recetaRepository;
    }

    @GetMapping("/recetas")
    public ResponseEntity<List<RecetaDTO>> obtenerRecetas() {
        List<RecetaDTO> recetas = recetaRepository.findAll().stream()
            .map(receta -> new RecetaDTO(
                receta.getId(),
                receta.getNombre(),
                receta.getDescripcion(),
                receta.getImagen(),
                receta.getTiempoCoccion(),
                receta.getDificultad()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(recetas);
    }

    @GetMapping("/recetas_buscar")
    public ResponseEntity<List<RecetaBusquedaDTO>> buscarRecetas(@RequestParam String keyword) {
        List<RecetaBusquedaDTO> recetas = recetaRepository.buscarPorKeyword(keyword).stream()
            .map(receta -> new RecetaBusquedaDTO(
                receta.getId(),
                receta.getNombre(),
                receta.getDescripcion(),
                receta.getImagen(),
                receta.getTipoCocina(),
                receta.getPaisOrigen(),
                receta.getTiempoCoccion(),
                receta.getDificultad()
            ))
            .collect(Collectors.toList());

        return ResponseEntity.ok(recetas);
    }

    @GetMapping("/recetas_detalle/{id}")
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<?> obtenerRecetaDetalle(@PathVariable("id") Long id) {
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