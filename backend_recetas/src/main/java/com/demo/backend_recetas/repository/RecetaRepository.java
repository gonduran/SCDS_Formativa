package com.demo.backend_recetas.repository;

//import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.demo.backend_recetas.model.Receta;
import java.util.List;

@Repository
public interface RecetaRepository extends JpaRepository<Receta, Long> {
    @Query("SELECT r FROM Receta r WHERE " +
           "LOWER(r.nombre) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.descripcion) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.detallePreparacion) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.tipoCocina) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.paisOrigen) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.tiempoCoccion) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "LOWER(r.dificultad) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
           "EXISTS (SELECT i FROM r.ingredientes i WHERE LOWER(i) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Receta> buscarPorKeyword(@Param("keyword") String keyword);
}