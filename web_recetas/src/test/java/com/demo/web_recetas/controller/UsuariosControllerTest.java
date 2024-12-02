package com.demo.web_recetas.controller;

import com.demo.web_recetas.model.User;
import com.demo.web_recetas.service.RecetasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UsuariosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    private User usuario;

    @BeforeEach
    void setUp() {
        usuario = new User();
        usuario.setId(1);
        usuario.setUsername("testUser");
        usuario.setNombreCompleto("Test User");
        usuario.setEmail("test@example.com");
    }

    @Test
    @DisplayName("Lista usuarios exitosamente")
    @WithMockUser(roles = "ADMIN")
    void listarUsuarios_Success() throws Exception {
        when(recetasService.listarUsuarios())
                .thenReturn(Arrays.asList(usuario));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("listarusuarios"))
                .andExpect(model().attributeExists("usuarios"))
                .andExpect(model().attribute("usuarios", Arrays.asList(usuario)));
    }

    @Test
    @DisplayName("Error al listar usuarios")
    @WithMockUser(roles = "ADMIN")
    void listarUsuarios_Error() throws Exception {
        when(recetasService.listarUsuarios())
                .thenThrow(new RuntimeException("Error de base de datos"));

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("error", "Error al obtener la lista de usuarios: Error de base de datos"));
    }

    @Test
    @DisplayName("Lista usuarios vacía")
    @WithMockUser(roles = "ADMIN")
    void listarUsuarios_Empty() throws Exception {
        when(recetasService.listarUsuarios())
                .thenReturn(Collections.emptyList());

        mockMvc.perform(get("/usuarios"))
                .andExpect(status().isOk())
                .andExpect(view().name("listarusuarios"))
                .andExpect(model().attribute("usuarios", Collections.emptyList()));
    }

    @Test
    @DisplayName("Mostrar formulario de edición exitoso")
    @WithMockUser(roles = "ADMIN")
    void mostrarFormularioEdicion_Success() throws Exception {
        when(recetasService.obtenerUsuarioPorId(1L))
                .thenReturn(Optional.of(usuario));

        mockMvc.perform(get("/usuarios/1/editar"))
                .andExpect(status().isOk())
                .andExpect(view().name("editarusuario"))
                .andExpect(model().attribute("usuario", usuario));
    }

    @Test
    @DisplayName("Usuario no encontrado al editar")
    @WithMockUser(roles = "ADMIN")
    void mostrarFormularioEdicion_NotFound() throws Exception {
        when(recetasService.obtenerUsuarioPorId(1L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/usuarios/1/editar"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("error", "Usuario no encontrado"));
    }

    @Test
    @DisplayName("Error al obtener usuario para editar")
    @WithMockUser(roles = "ADMIN")
    void mostrarFormularioEdicion_Error() throws Exception {
        when(recetasService.obtenerUsuarioPorId(1L))
                .thenThrow(new RuntimeException("Error de base de datos"));

        mockMvc.perform(get("/usuarios/1/editar"))
                .andExpect(status().isOk())
                .andExpect(view().name("error"))
                .andExpect(model().attribute("error", "Error al obtener los datos del usuario: Error de base de datos"));
    }

    @Test
    @DisplayName("Actualizar usuario exitosamente")
    @WithMockUser(roles = "ADMIN")
    void actualizarUsuario_Success() throws Exception {
        when(recetasService.actualizarUsuario(any(User.class)))
                .thenReturn("Usuario actualizado con éxito");

        mockMvc.perform(post("/usuarios/1/editar")
                .with(csrf())
                .flashAttr("usuario", usuario))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/usuarios"));

        verify(recetasService).actualizarUsuario(any(User.class));
    }

    @Test
    @DisplayName("Error al actualizar usuario")
    @WithMockUser(roles = "ADMIN")
    void actualizarUsuario_Error() throws Exception {
        when(recetasService.actualizarUsuario(any(User.class)))
                .thenThrow(new RuntimeException("Error de actualización"));

        mockMvc.perform(post("/usuarios/1/editar")
                .with(csrf())
                .flashAttr("usuario", usuario))
                .andExpect(status().isOk())
                .andExpect(view().name("editarusuario"))
                .andExpect(model().attribute("error", "Error al actualizar el usuario: Error de actualización"));
    }

    @Test
    @DisplayName("Acceso denegado sin autenticación")
    void accesoDenegado() throws Exception {
        mockMvc.perform(get("/usuarios"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

}