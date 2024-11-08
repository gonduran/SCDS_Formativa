package com.demo.backend_recetas.dto;

public class RecetaBusquedaDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String imagen;
    private String tipoCocina;
    private String paisOrigen;
    private String tiempoCoccion;
    private String dificultad;

    // Constructor
    public RecetaBusquedaDTO(Long id, String nombre, String descripcion, String imagen, String tipoCocina, String paisOrigen, String tiempoCoccion, String dificultad) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.tipoCocina = tipoCocina;
        this.paisOrigen = paisOrigen;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getTipoCocina() {
        return tipoCocina;
    }

    public void setTipoCocina(String tipoCocina) {
        this.tipoCocina = tipoCocina;
    }

    public String getPaisOrigen() {
        return paisOrigen;
    }

    public void setPaisOrigen(String paisOrigen) {
        this.paisOrigen = paisOrigen;
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