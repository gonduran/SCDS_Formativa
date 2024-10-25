package com.gp1.web_recetas_backend.model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "recetas")
public class Receta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String tipoCocina;

    @ElementCollection
    @CollectionTable(name = "receta_ingredientes", joinColumns = @JoinColumn(name = "receta_id"))
    @Column(name = "ingrediente")
    private List<String> ingredientes;

    private String paisOrigen;

    @Column(length = 1000)
    private String detallePreparacion;
    private String imagen;

    // Constructor vacío necesario para JPA
    public Receta() {
    }

    // Constructor con todos los campos
    public Receta(Long id, String nombre, String descripcion, String tipoCocina,
            List<String> ingredientes, String paisOrigen,
            String detallePreparacion, String imagen) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoCocina = tipoCocina;
        this.ingredientes = ingredientes;
        this.paisOrigen = paisOrigen;
        this.detallePreparacion = detallePreparacion;
        this.imagen = imagen;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getTipoCocina() {
        return tipoCocina;
    }

    public void setTipoCocina(String tipoCocina) {
        this.tipoCocina = tipoCocina;
    }

    public List<String> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<String> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
    }

    public String getDetallePreparacion() {
        return detallePreparacion;
    }

    public void setDetallePreparacion(String detallePreparacion) {
        this.detallePreparacion = detallePreparacion;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}