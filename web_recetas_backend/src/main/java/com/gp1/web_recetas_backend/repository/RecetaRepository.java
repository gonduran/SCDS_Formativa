package com.gp1.web_recetas_backend.repository;

import com.gp1.web_recetas_backend.model.Receta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
}