package com.demo.web_recetas.controller;

import com.demo.web_recetas.config.WebSecurityConfig;
import com.demo.web_recetas.model.User;
import com.demo.web_recetas.service.RecetasService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.containsString;

@WebMvcTest(UsuariosController.class)
@AutoConfigureMockMvc(addFilters = true)
@Import(WebSecurityConfig.class)
public class UsuariosControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecetasService recetasService;

    private User usuario;

    @BeforeEach
    public void setUp() {
        usuario = new User();
        usuario.setId(1);
        usuario.setUsername("testUser");
        usuario.setNombreCompleto("Juan Pérez");
        usuario.setEmail("juan.perez@example.com");
    }

    // @Test
    // @DisplayName("Listar usuarios exitosamente")
    // @WithMockUser(roles = "ADMIN")
    // public void testListarUsuarios_Success() throws Exception {
    //     when(recetasService.listarUsuarios()).thenReturn(Arrays.asList(usuario));

    //     mockMvc.perform(get("/usuarios"))
    //             .andExpect(status().isOk())
    //             .andExpect(view().name("listarusuarios"))
    //             .andExpect(model().attributeExists("usuarios"))
    //             .andExpect(model().attribute("usuarios", Arrays.asList(usuario)));

    //     verify(recetasService, times(1)).listarUsuarios();
    // }

    // @Test
    // @DisplayName("Error al listar usuarios")
    // @WithMockUser(roles = "ADMIN")
    // public void testListarUsuarios_Error() throws Exception {
    //     when(recetasService.listarUsuarios()).thenThrow(new RuntimeException("Database error"));

    //     mockMvc.perform(get("/usuarios"))
    //             .andExpect(status().isOk())
    //             .andExpect(view().name("error"))
    //             .andExpect(model().attributeExists("error"))
    //             .andExpect(model().attribute("error", "Error al obtener la lista de usuarios: Database error"));

    //     verify(recetasService, times(1)).listarUsuarios();
    // }

    // @Test
    // @DisplayName("Mostrar formulario de edición exitosamente")
    // @WithMockUser(roles = "ADMIN")
    // public void testMostrarFormularioEdicion_Success() throws Exception {
    //     when(recetasService.obtenerUsuarioPorId(1L)).thenReturn(Optional.of(usuario));

    //     mockMvc.perform(get("/usuarios/1/editar"))
    //             .andExpect(status().isOk())
    //             .andExpect(view().name("editarusuario"))
    //             .andExpect(model().attributeExists("usuario"))
    //             .andExpect(model().attribute("usuario", usuario));

    //     verify(recetasService, times(1)).obtenerUsuarioPorId(1L);
    // }

    // @Test
    // @DisplayName("Usuario no encontrado al mostrar formulario")
    // @WithMockUser(roles = "ADMIN")
    // public void testMostrarFormularioEdicion_NotFound() throws Exception {
    //     when(recetasService.obtenerUsuarioPorId(1L)).thenReturn(Optional.empty());

    //     mockMvc.perform(get("/usuarios/1/editar"))
    //             .andExpect(status().isOk())
    //             .andExpect(view().name("error"))
    //             .andExpect(model().attributeExists("error"))
    //             .andExpect(model().attribute("error", "Usuario no encontrado"));

    //     verify(recetasService, times(1)).obtenerUsuarioPorId(1L);
    // }

    // @Test
    // @DisplayName("Actualizar usuario exitosamente")
    // @WithMockUser(roles = "ADMIN")
    // public void testActualizarUsuario_Success() throws Exception {
    //     when(recetasService.actualizarUsuario(Mockito.any(User.class))).thenReturn("Usuario actualizado exitosamente");

    //     mockMvc.perform(post("/usuarios/1/editar")
    //                     .with(csrf())
    //                     .flashAttr("usuario", usuario))
    //             .andExpect(status().is3xxRedirection())
    //             .andExpect(redirectedUrl("/usuarios"))
    //             .andExpect(model().attributeDoesNotExist("error"));

    //     verify(recetasService, times(1)).actualizarUsuario(Mockito.any(User.class));
    // }

    // @Test
    // @DisplayName("Error al actualizar usuario")
    // @WithMockUser(roles = "ADMIN")
    // public void testActualizarUsuario_Error() throws Exception {
    //     doThrow(new RuntimeException("Database error")).when(recetasService).actualizarUsuario(Mockito.any(User.class));

    //     mockMvc.perform(post("/usuarios/1/editar")
    //                     .with(csrf())
    //                     .flashAttr("usuario", usuario))
    //             .andExpect(status().isOk())
    //             .andExpect(view().name("editarusuario"))
    //             .andExpect(model().attributeExists("error"))
    //             .andExpect(model().attribute("error", "Error al actualizar el usuario: Database error"));

    //     verify(recetasService, times(1)).actualizarUsuario(Mockito.any(User.class));
    // }

    // @Test
    // @DisplayName("Error al obtener usuario por ID")
    // @WithMockUser(roles = "ADMIN")
    // public void testMostrarFormularioEdicion_Error() throws Exception {
    //     when(recetasService.obtenerUsuarioPorId(1L))
    //         .thenThrow(new RuntimeException("Error de base de datos"));

    //     mockMvc.perform(get("/usuarios/1/editar"))
    //         .andExpect(status().isOk())
    //         .andExpect(view().name("error"))
    //         .andExpect(model().attributeExists("error"))
    //         .andExpect(model().attribute("error", 
    //             containsString("Error al obtener los datos del usuario")));
    // }

    // @Test
    // @DisplayName("Acceso denegado a usuarios no autorizados")
    // void testAccesoDenegado() throws Exception {
    //     mockMvc.perform(get("/usuarios"))
    //         .andExpect(status().is3xxRedirection())
    //         .andExpect(redirectedUrl("/login"));
    // }

    // @Test
    // @DisplayName("Actualización con error de validación")
    // @WithMockUser(roles = "ADMIN")
    // public void testActualizarUsuario_ErrorValidacion() throws Exception {
    //     User usuarioInvalido = new User();
    //     usuarioInvalido.setId(1);
    //     // No establecemos campos requeridos para provocar error de validación
    
    //     mockMvc.perform(post("/usuarios/1/editar")
    //             .with(csrf())
    //             .flashAttr("usuario", usuarioInvalido))
    //         .andExpect(status().is3xxRedirection())
    //         .andExpect(redirectedUrl("/usuarios")); // Verificar redirección
    // }

    // @Test
    // @DisplayName("Acceso denegado a usuarios no admin")
    // @WithMockUser(roles = "USER")
    // void testAccesoDenegadoUsuarioNoAdmin() throws Exception {
    //     mockMvc.perform(get("/usuarios"))
    //         .andExpect(status().is3xxRedirection())
    //         .andExpect(redirectedUrl("/accesodenegado")); // Cambiado a la URL real
    // }
    
    // @Test
    // @DisplayName("Intento de actualización sin CSRF")
    // @WithMockUser(roles = "ADMIN")
    // public void testActualizarUsuario_SinCSRF() throws Exception {
    //     mockMvc.perform(post("/usuarios/1/editar")
    //             .flashAttr("usuario", usuario))
    //         .andExpect(status().is3xxRedirection())
    //         .andExpect(redirectedUrl("/accesodenegado")); // Cambiado a la URL real
    // }
}