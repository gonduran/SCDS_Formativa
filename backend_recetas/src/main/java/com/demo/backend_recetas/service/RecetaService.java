package com.demo.backend_recetas.service;

import com.demo.backend_recetas.model.Receta;
import com.demo.backend_recetas.repository.RecetaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RecetaService {

    @Autowired
    private RecetaRepository recetaRepository;

    public Receta guardarReceta(Receta receta) {
        return recetaRepository.save(receta);
    }

    public Receta obtenerRecetaPorId(Long id) {
        return recetaRepository.findById(id).orElse(null);
    }
}