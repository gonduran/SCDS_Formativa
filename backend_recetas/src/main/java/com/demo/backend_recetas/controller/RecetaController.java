package com.demo.backend_recetas.controller;

import com.demo.backend_recetas.model.Comentario;
import com.demo.backend_recetas.model.Receta;
import com.demo.backend_recetas.service.RecetaService;
import com.demo.backend_recetas.dto.MediaRequestDTO;
import java.util.List;
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

    @PostMapping("/{id}/media")
    public ResponseEntity<String> agregarMedia(@PathVariable Long id, @RequestBody MediaRequestDTO mediaRequest) {
        Receta receta = recetaService.obtenerRecetaPorId(id);
        if (receta == null) {
            return ResponseEntity.badRequest().body("Receta no encontrada.");
        }

        // Agregar fotos y videos desde el request
        receta.getFotos().addAll(mediaRequest.getFotos());
        receta.getVideos().addAll(mediaRequest.getVideos());
        recetaService.guardarReceta(receta);

        return ResponseEntity.ok("Media agregada exitosamente.");
    }

    @PostMapping("/{id}/comentarios")
    public ResponseEntity<String> agregarComentario(@PathVariable Long id, @RequestBody Comentario comentario) {
        Receta receta = recetaService.obtenerRecetaPorId(id);
        if (receta == null) {
            return ResponseEntity.badRequest().body("Receta no encontrada.");
        }

        // Agregar comentario a la receta
        receta.getComentarios().add(comentario);

        // Recalcular la valoración promedio
        receta.setValoracionPromedio(calcularValoracionPromedio(receta.getComentarios()));

        recetaService.guardarReceta(receta);

        return ResponseEntity.ok("Comentario agregado exitosamente.");
    }

    private Double calcularValoracionPromedio(List<Comentario> comentarios) {
        return comentarios.stream()
                .mapToInt(Comentario::getValoracion)
                .average()
                .orElse(0.0);
    }
}