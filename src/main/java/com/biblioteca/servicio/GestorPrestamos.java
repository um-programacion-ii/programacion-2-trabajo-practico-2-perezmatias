package com.biblioteca.servicio;

import com.biblioteca.modelo.prestamo.Prestamo;
import com.biblioteca.modelo.recurso.RecursoDigital;
import com.biblioteca.modelo.usuario.Usuario;
import com.biblioteca.excepciones.OperacionNoPermitidaException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Objects;

public class GestorPrestamos {

    private final Map<String, Prestamo> prestamosActivosPorRecursoId = new HashMap<>();

    public GestorPrestamos() {
    }

    public Prestamo realizarPrestamo(Usuario usuario, RecursoDigital recurso, int diasPrestamo) {
        System.out.println(">>> Lógica de realizarPrestamo PENDIENTE <<<");

        throw new UnsupportedOperationException("realizarPrestamo no implementado todavía.");
    }

    public void registrarDevolucion(String recursoId) {
        System.out.println(">>> Lógica de registrarDevolucion PENDIENTE <<<");
        throw new UnsupportedOperationException("registrarDevolucion no implementado todavía.");
    }

    public Optional<Prestamo> buscarPrestamoPorRecursoId(String recursoId) {
        return Optional.ofNullable(prestamosActivosPorRecursoId.get(recursoId));
    }

    public List<Prestamo> listarPrestamosActivos() {
        return new ArrayList<>(prestamosActivosPorRecursoId.values());
    }

}