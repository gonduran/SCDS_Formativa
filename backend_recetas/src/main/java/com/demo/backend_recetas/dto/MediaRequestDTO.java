package com.demo.backend_recetas.dto;

import java.util.List;

public class MediaRequestDTO {

    private List<String> fotos;
    private List<String> videos;

    public MediaRequestDTO(List<String> fotos, List<String> videos) {
        this.fotos = fotos;
        this.videos = videos;
    }

    // Getters y Setters
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
}