package com.demo.web_recetas.model;

public class Comentario {

    private Long id; // Identificador único del comentario
    private String usuario; // Nombre del usuario que realiza el comentario
    private String comentario; // Texto del comentario
    private Integer valoracion; // Valoración de 1 a 5 estrellas
    private Integer estado; // 0 = nuevo, 1 = aprobado, 2 = rechazado

    // Constructor vacío
    public Comentario() {
    }

    // Constructor con todos los campos
    public Comentario(Long id, String usuario, String comentario, Integer valoracion, Integer estado) {
        this.id = id;
        this.usuario = usuario;
        this.comentario = comentario;
        this.valoracion = valoracion;
        this.estado = estado;
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

    public Integer getEstado() {
        return estado;
    }

    public void setEstado(Integer estado) {
        this.estado = estado;
    }
}