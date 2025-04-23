package com.biblioteca.servicio.reportes;

import com.biblioteca.modelo.recurso.CategoriaRecurso;
import com.biblioteca.modelo.recurso.RecursoDigital;
import com.biblioteca.modelo.usuario.Usuario;
import com.biblioteca.servicio.GestorRecursos;
import com.biblioteca.servicio.GestorUsuarios;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GeneradorReportes {

    private final GestorRecursos gestorRecursos;
    private final GestorUsuarios gestorUsuarios;

    public GeneradorReportes(GestorRecursos gestorRecursos, GestorUsuarios gestorUsuarios) {
        this.gestorRecursos = Objects.requireNonNull(gestorRecursos, "GestorRecursos no puede ser nulo.");
        this.gestorUsuarios = Objects.requireNonNull(gestorUsuarios, "GestorUsuarios no puede ser nulo.");
    }

    public List<RecursoDigital> getTopNRecursosMasPrestados(int n) {
        System.out.println(">>> Lógica de getTopNRecursosMasPrestados PENDIENTE <<<");
        throw new UnsupportedOperationException("getTopNRecursosMasPrestados no implementado todavía.");
    }

    public List<Usuario> getTopNUsuariosMasActivos(int n) {
        System.out.println(">>> Lógica de getTopNUsuariosMasActivos PENDIENTE <<<");
        throw new UnsupportedOperationException("getTopNUsuariosMasActivos no implementado todavía.");
    }

    public Map<CategoriaRecurso, Long> getEstadisticasPorCategoria() {
        System.out.println(">>> Lógica de getEstadisticasPorCategoria PENDIENTE <<<");
        throw new UnsupportedOperationException("getEstadisticasPorCategoria no implementado todavía.");
    }
}