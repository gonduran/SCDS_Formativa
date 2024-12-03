package com.demo.backend_recetas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.backend_recetas.model.Comentario;

import java.util.List;

@Repository
public interface ComentarioRepository extends JpaRepository<Comentario, Long> {
    List<Comentario> findByEstado(Integer estado);

    List<Comentario> findByRecetaId(Long recetaId);
}