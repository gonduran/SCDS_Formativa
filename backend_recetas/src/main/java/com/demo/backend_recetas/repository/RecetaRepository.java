package com.demo.backend_recetas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.demo.backend_recetas.model.Receta;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    
}