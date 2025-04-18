package com.biblioteca.servicio;

import com.biblioteca.modelo.usuario.Usuario;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


public class GestorUsuarios {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    public boolean agregarUsuario(Usuario usuario) {
        if (usuario == null || usuarios.containsKey(usuario.getId())) {

            System.err.println("Error: Intentando agregar usuario nulo o con ID duplicado: " + (usuario != null ? usuario.getId() : "null"));
            return false;
        }
        usuarios.put(usuario.getId(), usuario);
        System.out.println("Usuario agregado: " + usuario.getNombre() + " (ID: " + usuario.getId() + ")");
        return true;
    }

    public Optional<Usuario> buscarUsuarioPorId(String id) {

        return Optional.ofNullable(usuarios.get(id));
    }

    public List<Usuario> listarTodosLosUsuarios() {

        return new ArrayList<>(usuarios.values());
    }


    public boolean eliminarUsuario(String id) {
        Usuario usuarioEliminado = usuarios.remove(id);
        if (usuarioEliminado != null) {
            System.out.println("Usuario eliminado: " + usuarioEliminado.getNombre());
            return true;
        }
        System.err.println("Error: No se encontró usuario con ID para eliminar: " + id);
        return false;
    }


    public boolean actualizarUsuario(Usuario usuarioActualizado) {
        if (usuarioActualizado == null || !usuarios.containsKey(usuarioActualizado.getId())) {
            System.err.println("Error: Intentando actualizar usuario nulo o no existente: " + (usuarioActualizado != null ? usuarioActualizado.getId() : "null"));
            return false;
        }
        usuarios.put(usuarioActualizado.getId(), usuarioActualizado);
        System.out.println("Usuario actualizado: " + usuarioActualizado.getNombre());
        return true;
    }
}