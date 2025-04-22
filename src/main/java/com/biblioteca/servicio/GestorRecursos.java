package com.biblioteca.servicio;

import com.biblioteca.modelo.recurso.RecursoDigital;
import com.biblioteca.modelo.recurso.CategoriaRecurso;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GestorRecursos {


    private final Map<String, RecursoDigital> recursos = new HashMap<>();

    public boolean agregarRecurso(RecursoDigital recurso) {
        if (recurso == null || recursos.containsKey(recurso.getIdentificador())) {
            System.err.println("Error: Intentando agregar recurso nulo o con ID duplicado: " + (recurso != null ? recurso.getIdentificador() : "null"));
            return false;
        }
        recursos.put(recurso.getIdentificador(), recurso);
        System.out.println("Recurso agregado: " + recurso.getTitulo() + " (ID: " + recurso.getIdentificador() + ")");
        return true;
    }

    public Optional<RecursoDigital> buscarRecursoPorId(String id) {
        return Optional.ofNullable(recursos.get(id));
    }

    public List<RecursoDigital> listarTodosLosRecursos() {
        return new ArrayList<>(recursos.values());
    }


    public boolean eliminarRecurso(String id) {
        RecursoDigital recursoEliminado = recursos.remove(id);
        if (recursoEliminado != null) {
            System.out.println("Recurso eliminado: " + recursoEliminado.getTitulo());
            return true;
        }
        System.err.println("Error: No se encontró recurso con ID para eliminar: " + id);
        return false;
    }

    public boolean actualizarRecurso(RecursoDigital recursoActualizado) {
        if (recursoActualizado == null || !recursos.containsKey(recursoActualizado.getIdentificador())) {
            System.err.println("Error: Intentando actualizar recurso nulo o no existente: " + (recursoActualizado != null ? recursoActualizado.getIdentificador() : "null"));
            return false;
        }
        recursos.put(recursoActualizado.getIdentificador(), recursoActualizado); // Sobrescribe
        System.out.println("Recurso actualizado: " + recursoActualizado.getTitulo());
        return true;
    }

    public List<RecursoDigital> buscarPorCategoria(CategoriaRecurso categoria) {
        Objects.requireNonNull(categoria, "La categoría no puede ser nula para la búsqueda.");

        return this.recursos.values()
                .stream()
                .filter(recurso -> recurso.getCategoria().equals(categoria))
                .collect(Collectors.toList());
    }

}