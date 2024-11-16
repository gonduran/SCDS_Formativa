package com.demo.backend_recetas.model;

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

    @Column(length = 3000)
    private String detallePreparacion;
    private String imagen;

    private String tiempoCoccion;
    private String dificultad;

    @ElementCollection
    @CollectionTable(name = "receta_fotos", joinColumns = @JoinColumn(name = "receta_id"))
    @Column(name = "foto_url")
    private List<String> fotos;

    @ElementCollection
    @CollectionTable(name = "receta_videos", joinColumns = @JoinColumn(name = "receta_id"))
    @Column(name = "video_url")
    private List<String> videos;

    @OneToMany(mappedBy = "receta", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comentario> comentarios;

    private Double valoracionPromedio;

    // Constructor vacío necesario para JPA
    public Receta() {
    }

    // Constructor con todos los campos
    public Receta(Long id, String nombre, String descripcion, String tipoCocina,
            List<String> ingredientes, String paisOrigen,
            String detallePreparacion, String imagen,
            String tiempoCoccion, String dificultad,
            List<String> fotos, List<String> videos,
            List<Comentario> comentarios, Double valoracionPromedio) {
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