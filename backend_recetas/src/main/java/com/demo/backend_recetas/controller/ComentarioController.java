package com.demo.backend_recetas.controller;

import com.demo.backend_recetas.model.Comentario;
import com.demo.backend_recetas.service.ComentarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/comentarios")
public class ComentarioController {

    @Autowired
    private ComentarioService comentarioService;

    @GetMapping
    public List<Comentario> listarTodos() {
        return comentarioService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Comentario> obtenerPorId(@PathVariable Long id) {
        return comentarioService.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/estado/{estado}")
    public List<Comentario> listarPorEstado(@PathVariable Integer estado) {
        return comentarioService.listarPorEstado(estado);
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<Comentario> actualizarEstado(
            @PathVariable Long id,
            @RequestParam Integer nuevoEstado) {
        try {
            return ResponseEntity.ok(comentarioService.actualizarEstado(id, nuevoEstado));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarComentario(@PathVariable Long id) {
        comentarioService.eliminarComentario(id);
        return ResponseEntity.noContent().build();
    }
}