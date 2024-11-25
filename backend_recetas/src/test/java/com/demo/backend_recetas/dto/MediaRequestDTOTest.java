package com.demo.backend_recetas.dto;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class MediaRequestDTOTest {

    @Test
    void constructor_InitializesCorrectly() {
        // Arrange
        List<String> fotos = Arrays.asList("foto1.jpg", "foto2.jpg");
        List<String> videos = Arrays.asList("video1.mp4");

        // Act
        MediaRequestDTO mediaRequest = new MediaRequestDTO(fotos, videos);

        // Assert
        assertEquals(fotos, mediaRequest.getFotos());
        assertEquals(videos, mediaRequest.getVideos());
    }

    @Test
    void settersAndGetters_WorkCorrectly() {
        // Arrange
        MediaRequestDTO mediaRequest = new MediaRequestDTO(Arrays.asList(), Arrays.asList());
        List<String> newFotos = Arrays.asList("nueva1.jpg", "nueva2.jpg");
        List<String> newVideos = Arrays.asList("nuevo1.mp4");

        // Act
        mediaRequest.setFotos(newFotos);
        mediaRequest.setVideos(newVideos);

        // Assert
        assertEquals(newFotos, mediaRequest.getFotos());
        assertEquals(newVideos, mediaRequest.getVideos());
    }
}