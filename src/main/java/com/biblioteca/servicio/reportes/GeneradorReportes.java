package com.biblioteca.servicio.reportes;

import com.biblioteca.modelo.recurso.CategoriaRecurso;
import com.biblioteca.modelo.recurso.RecursoDigital;
import com.biblioteca.modelo.usuario.Usuario;
import com.biblioteca.servicio.GestorRecursos;
import com.biblioteca.servicio.GestorUsuarios;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.stream.Collectors;
import java.util.Collections;

public class GeneradorReportes {

    private final GestorRecursos gestorRecursos;
    private final GestorUsuarios gestorUsuarios;

    public GeneradorReportes(GestorRecursos gestorRecursos, GestorUsuarios gestorUsuarios) {
        this.gestorRecursos = Objects.requireNonNull(gestorRecursos, "GestorRecursos no puede ser nulo.");
        this.gestorUsuarios = Objects.requireNonNull(gestorUsuarios, "GestorUsuarios no puede ser nulo.");
    }

    public List<RecursoDigital> getTopNRecursosMasPrestados(int n) {
        if (n <= 0) {
            System.err.println("Advertencia: El número 'n' para el top debe ser positivo.");
            return new ArrayList<>();
        }

        List<RecursoDigital> todosLosRecursos = this.gestorRecursos.listarTodosLosRecursos();

        return todosLosRecursos.stream()
                .sorted(Comparator.comparingInt(RecursoDigital::getVecesPrestado).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public List<Usuario> getTopNUsuariosMasActivos(int n) {
        if (n <= 0) {
            System.err.println("Advertencia: El número 'n' para el top debe ser positivo.");
            return new ArrayList<>();
        }
        List<Usuario> todosLosUsuarios = this.gestorUsuarios.listarTodosLosUsuarios();

        return todosLosUsuarios.stream()
                .sorted(Comparator.comparingInt(Usuario::getPrestamosRealizados).reversed())
                .limit(n)
                .collect(Collectors.toList());
    }

    public Map<CategoriaRecurso, Long> getEstadisticasPorCategoria() {
        List<RecursoDigital> todosLosRecursos = this.gestorRecursos.listarTodosLosRecursos();
        if (todosLosRecursos.isEmpty()) {
            return Collections.emptyMap();
        }
        return todosLosRecursos.stream()
                .filter(recurso -> recurso.getCategoria() != null)
                .collect(Collectors.groupingBy(
                        RecursoDigital::getCategoria,
                        Collectors.counting()
                ));
    }
}