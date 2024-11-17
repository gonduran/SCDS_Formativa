package com.demo.web_recetas.model;

public class Comentario {

    private Long id; // Identificador único del comentario
    private String usuario; // Nombre del usuario que realiza el comentario
    private String comentario; // Texto del comentario
    private Integer valoracion; // Valoración de 1 a 5 estrellas

    // Constructor vacío
    public Comentario() {
    }

    // Constructor con todos los campos
    public Comentario(Long id, String usuario, String comentario, Integer valoracion) {
        this.id = id;
        this.usuario = usuario;
        this.comentario = comentario;
        this.valoracion = valoracion;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public Integer getValoracion() {
        return valoracion;
    }

    public void setValoracion(Integer valoracion) {
        this.valoracion = valoracion;
    }
}