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
import java.util.Comparator;
import java.util.stream.Stream;
import com.biblioteca.excepciones.RecursoDuplicadoException;
import com.biblioteca.modelo.recurso.RecursoDigital;
import java.util.Objects;

public class GestorRecursos {


    private final Map<String, RecursoDigital> recursos = new HashMap<>();

    public void agregarRecurso(RecursoDigital recurso) {

        Objects.requireNonNull(recurso, "El recurso a agregar no puede ser nulo.");

        if (recursos.containsKey(recurso.getIdentificador())) {
            throw new RecursoDuplicadoException("Ya existe un recurso con el ID: " + recurso.getIdentificador());
        }
        recursos.put(recurso.getIdentificador(), recurso);
    }

    public Optional<RecursoDigital> buscarRecursoPorId(String id) {
        return Optional.ofNullable(recursos.get(id));
    }

    public List<RecursoDigital> listarTodosLosRecursos(Comparator<RecursoDigital> comparador) {
        Stream<RecursoDigital> streamRecursos = this.recursos.values().stream();

        if (comparador != null) {
            streamRecursos = streamRecursos.sorted(comparador);
        }

        return streamRecursos.collect(Collectors.toList());
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

    public List<RecursoDigital> buscarPorTitulo(String textoBusqueda) {
        if (textoBusqueda == null || textoBusqueda.trim().isEmpty()) {
            System.err.println("Advertencia: Texto de búsqueda por título está vacío.");
            return new ArrayList<>();
        }

        final String textoBusquedaLower = textoBusqueda.toLowerCase();

        return this.recursos.values()
                .stream()
                .filter(recurso -> recurso.getTitulo() != null &&
                        recurso.getTitulo().toLowerCase()
                                .contains(textoBusquedaLower))
                .collect(Collectors.toList());
    }

    public List<RecursoDigital> filtrarPorTipo(Class<? extends RecursoDigital> tipoClase) {
        Objects.requireNonNull(tipoClase, "La clase del tipo de recurso no puede ser nula para filtrar.");

        return this.recursos.values()
                .stream()
                .filter(recurso -> tipoClase.isInstance(recurso))
                .collect(Collectors.toList());
    }
}