package com.demo.web_recetas.model;

import java.util.List;

public class Receta {

    private Long id;  // Campo ID para identificar cada receta
    private String nombre;
    private String descripcion;
    private String tipoCocina;
    private List<String> ingredientes;
    private String paisOrigen;
    private String detallePreparacion;
    private String imagen;
    private String tiempoCoccion;
    private String dificultad;

    // Constructor sin parámetros
    public Receta() {
    }

    // Constructor
    public Receta(Long id, String nombre, String descripcion, String tipoCocina, List<String> ingredientes, String paisOrigen, String detallePreparacion, String imagen, String tiempoCoccion, String dificultad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.tipoCocina = tipoCocina;
        this.ingredientes = ingredientes;
        this.paisOrigen = paisOrigen;
        this.detallePreparacion = detallePreparacion;
        this.imagen = imagen;
        this.tiempoCoccion = tiempoCoccion;
        this.dificultad = dificultad;
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

    public String getTiempoCoccion() {
        return tiempoCoccion;
    }

    public void setTiempoCoccion(String tiempoCoccion) {
        this.tiempoCoccion = tiempoCoccion;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }
}