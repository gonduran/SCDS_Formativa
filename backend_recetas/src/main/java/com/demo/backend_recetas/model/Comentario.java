package com.demo.backend_recetas.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "comentarios")
public class Comentario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usuario;

    @Column(length = 2000)
    private String comentario;

    private Integer valoracion; // 1 a 5 estrellas

    private Integer estado; // 0 = nuevo, 1 = aprobado, 2 = rechazado

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receta_id", nullable = false)
    @JsonBackReference
    private Receta receta;

    /**
     * Constructor vacío requerido por JPA.
     * JPA necesita este constructor para crear instancias de la entidad
     * durante la carga desde la base de datos.
     * No debe usarse directamente en el código de la aplicación.
     */
    protected Comentario() {
        // Constructor vacío requerido por JPA
        // Se marca como protected para desalentar su uso directo
    }

    /**
     * Constructor principal para crear nuevos comentarios.
     * @param usuario El nombre del usuario que hace el comentario
     * @param comentario El texto del comentario
     * @param valoracion La valoración (1-5 estrellas)
     * @param receta La receta a la que pertenece el comentario
     */
    public Comentario(String usuario, String comentario, Integer valoracion, Integer estado, Receta receta) {
        this.usuario = usuario;
        this.comentario = comentario;
        this.valoracion = valoracion;
        this.estado = estado;
        this.receta = receta;
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

    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }
}