package com.biblioteca.servicio;

import com.biblioteca.modelo.prestamo.Prestamo;
import com.biblioteca.modelo.recurso.RecursoDigital;
import com.biblioteca.modelo.usuario.Usuario;
import com.biblioteca.excepciones.OperacionNoPermitidaException;
import com.biblioteca.modelo.recurso.Prestable;
import java.time.LocalDate;
import java.lang.IllegalArgumentException;
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
        Objects.requireNonNull(usuario, "El usuario no puede ser nulo para realizar un préstamo.");
        Objects.requireNonNull(recurso, "El recurso no puede ser nulo para realizar un préstamo.");
        if (diasPrestamo <= 0) {
            throw new IllegalArgumentException("El número de días de préstamo debe ser positivo.");
        }

        if (prestamosActivosPorRecursoId.containsKey(recurso.getIdentificador())) {
            throw new OperacionNoPermitidaException("Error interno: El recurso con ID " + recurso.getIdentificador() + " ya figura como prestado en el gestor.");
        }

        if (!(recurso instanceof Prestable prestable)) {
            throw new OperacionNoPermitidaException("El recurso '" + recurso.getTitulo() + "' no es del tipo prestable.");
        }

        if (!prestable.estaDisponibleParaPrestamo()) {

            throw new OperacionNoPermitidaException("El recurso '" + recurso.getTitulo() + "' no está disponible para préstamo en este momento (Estado: " + recurso.getEstado() + ").");
        }

        LocalDate fechaPrestamo = LocalDate.now();
        LocalDate fechaDevolucion = fechaPrestamo.plusDays(diasPrestamo);
        prestable.marcarComoPrestado(usuario, fechaDevolucion);

        Prestamo nuevoPrestamo = new Prestamo(recurso, usuario, fechaPrestamo, fechaDevolucion);

        prestamosActivosPorRecursoId.put(recurso.getIdentificador(), nuevoPrestamo);

        System.out.println("Préstamo creado exitosamente: ID " + nuevoPrestamo.getIdPrestamo());
        return nuevoPrestamo;
    }

    public void registrarDevolucion(String recursoId) {
        Objects.requireNonNull(recursoId, "El ID del recurso no puede ser nulo para la devolución.");
        Prestamo prestamoActivo = prestamosActivosPorRecursoId.get(recursoId);
        if (prestamoActivo == null) {
            throw new OperacionNoPermitidaException("No se encontró un préstamo activo para el recurso con ID: " + recursoId);
        }
        RecursoDigital recurso = prestamoActivo.getRecurso();
        if (recurso == null) {
            throw new OperacionNoPermitidaException("Error interno: El préstamo activo (ID: " + prestamoActivo.getIdPrestamo() + ") no tiene un recurso asociado.");
        }
        if (!(recurso instanceof Prestable prestable)) {
            throw new OperacionNoPermitidaException("Error interno: El recurso asociado al préstamo (ID: " + recursoId + ") no es de tipo Prestable.");
        }

        try {
            prestable.marcarComoDevuelto();
        } catch (Exception e) {
            System.err.println("Advertencia: Ocurrió un error al intentar marcar el recurso como devuelto, pero se procederá a eliminar el registro del préstamo. Error: " + e.getMessage());
        }
        prestamosActivosPorRecursoId.remove(recursoId);
        System.out.println("Devolución registrada para recurso ID: " + recursoId);
    }

    public Optional<Prestamo> buscarPrestamoPorRecursoId(String recursoId) {
        return Optional.ofNullable(prestamosActivosPorRecursoId.get(recursoId));
    }

    public List<Prestamo> listarPrestamosActivos() {
        return new ArrayList<>(prestamosActivosPorRecursoId.values());
    }

}