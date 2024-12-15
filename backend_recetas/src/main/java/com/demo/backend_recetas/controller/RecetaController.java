package com.demo.backend_recetas.controller;

import com.demo.backend_recetas.model.Comentario;
import com.demo.backend_recetas.model.Receta;
import com.demo.backend_recetas.service.RecetaService;
import com.demo.backend_recetas.dto.MediaRequestDTO;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.Authentication;

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
    @Transactional
    public ResponseEntity<String> agregarComentario(@PathVariable Long id, @RequestBody Comentario comentario) {
        try {
            // 1. Obtener la receta
            Receta receta = recetaService.obtenerRecetaPorId(id);
            if (receta == null) {
                return ResponseEntity.badRequest().body("Receta no encontrada.");
            }

            // 2. Obtener el usuario autenticado
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            String username = auth.getName();
            comentario.setUsuario(username);

            // 3. Establecer la relación bidireccional
            comentario.setReceta(receta);
            if (receta.getComentarios() == null) {
                receta.setComentarios(new ArrayList<>());
            }
            receta.getComentarios().add(comentario);

            // 4. Calcular y actualizar el promedio
            double promedio = receta.getComentarios().stream()
                    .mapToInt(Comentario::getValoracion)
                    .average()
                    .orElse(0.0);
            receta.setValoracionPromedio(promedio);

            // 5. Setear estado nuevo
            comentario.setEstado(0);

            // 6. Guardar
            recetaService.guardarReceta(receta);

            return ResponseEntity.ok("Comentario agregado exitosamente. Valoración promedio actualizada a: " + promedio);
        } catch (Exception e) {
            //e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al agregar el comentario: " + e.getMessage());
        }
    }   
}