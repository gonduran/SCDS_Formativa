package com.demo.backend_recetas.service;

import com.demo.backend_recetas.model.Comentario;
import com.demo.backend_recetas.repository.ComentarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ComentarioService {

    @Autowired
    private ComentarioRepository comentarioRepository;

    public List<Comentario> listarTodos() {
        return comentarioRepository.findAll();
    }

    public Optional<Comentario> obtenerPorId(Long id) {
        return comentarioRepository.findById(id);
    }

    public List<Comentario> listarPorEstado(Integer estado) {
        return comentarioRepository.findByEstado(estado);
    }

    public Comentario actualizarEstado(Long id, Integer nuevoEstado) {
        Optional<Comentario> comentarioOpt = comentarioRepository.findById(id);
        if (comentarioOpt.isPresent()) {
            Comentario comentario = comentarioOpt.get();
            comentario.setEstado(nuevoEstado);
            return comentarioRepository.save(comentario);
        }
        throw new IllegalArgumentException("Comentario no encontrado");
    }

    public void eliminarComentario(Long id) {
        comentarioRepository.deleteById(id);
    }
}