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
    private List<String> fotos;  // Nuevas fotos de la receta
    private List<String> videos; // Nuevos videos de la receta
    private List<Comentario> comentarios; // Nuevos comentarios asociados
    private Double valoracionPromedio; // Nueva valoración promedio de la receta

    // Constructor sin parámetros
    public Receta() {
    }

    // Constructor con todos los campos
    public Receta(Long id, String nombre, String descripcion, String tipoCocina, List<String> ingredientes,
                  String paisOrigen, String detallePreparacion, String imagen, String tiempoCoccion,
                  String dificultad, List<String> fotos, List<String> videos, List<Comentario> comentarios,
                  Double valoracionPromedio) {
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
        this.fotos = fotos;
        this.videos = videos;
        this.comentarios = comentarios;
        this.valoracionPromedio = valoracionPromedio;
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

    public List<String> getFotos() {
        return fotos;
    }

    public void setFotos(List<String> fotos) {
        this.fotos = fotos;
    }

    public List<String> getVideos() {
        return videos;
    }

    public void setVideos(List<String> videos) {
        this.videos = videos;
    }

    public List<Comentario> getComentarios() {
        return comentarios;
    }

    public void setComentarios(List<Comentario> comentarios) {
        this.comentarios = comentarios;
    }

    public Double getValoracionPromedio() {
        return valoracionPromedio;
    }

    public void setValoracionPromedio(Double valoracionPromedio) {
        this.valoracionPromedio = valoracionPromedio;
    }
}