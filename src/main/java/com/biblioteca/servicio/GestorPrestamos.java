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
import com.biblioteca.servicio.notificaciones.ServicioNotificaciones;
import com.biblioteca.servicio.GestorReservas;
import com.biblioteca.modelo.recurso.EstadoRecurso;

public class GestorPrestamos {
    private final Map<String, Prestamo> prestamosActivosPorRecursoId = new HashMap<>();
    private final GestorReservas gestorReservas;
    private final ServicioNotificaciones servicioNotificaciones;

    public GestorPrestamos(GestorReservas gestorReservas, ServicioNotificaciones servicioNotificaciones) {
        this.gestorReservas = Objects.requireNonNull(gestorReservas, "GestorReservas no puede ser nulo.");
        this.servicioNotificaciones = Objects.requireNonNull(servicioNotificaciones, "ServicioNotificaciones no puede ser nulo.");
    }

    public synchronized Prestamo realizarPrestamo(Usuario usuario, RecursoDigital recurso, int diasPrestamo) {
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

    public synchronized void registrarDevolucion(String recursoId) {
        Objects.requireNonNull(recursoId, "El ID del recurso no puede ser nulo para la devolución.");

        Prestamo prestamoActivo = prestamosActivosPorRecursoId.remove(recursoId);

        if (prestamoActivo == null) {
            throw new OperacionNoPermitidaException("No se encontró un préstamo activo para el recurso con ID: " + recursoId);
        }

        RecursoDigital recurso = prestamoActivo.getRecurso();
        if (recurso == null) {

            System.err.println("Error CRÍTICO: El préstamo completado (ID: " + prestamoActivo.getIdPrestamo() + ") no tenía un recurso asociado.");

            return;
        }


        if (recurso instanceof Prestable prestable) {

            Optional<Usuario> siguienteUsuarioOpt = gestorReservas.obtenerSiguienteUsuarioEnCola(recursoId);

            if (siguienteUsuarioOpt.isPresent()) {

                Usuario siguienteUsuario = siguienteUsuarioOpt.get();

                try {
                    recurso.actualizarEstado(EstadoRecurso.RESERVADO);
                    System.out.println("Recurso ID " + recursoId + " devuelto y ahora en estado RESERVADO para " + siguienteUsuario.getNombre());

                    servicioNotificaciones.enviarNotificacion(
                            "RESERVA_DISPONIBLE",
                            siguienteUsuario.getId(),
                            "El recurso '" + recurso.getTitulo() + "' (ID: " + recursoId + ") que reservaste ya está disponible para ti."
                    );

                } catch (Exception e) {
                    System.err.println("Error al actualizar estado a RESERVADO o notificar para recurso ID " + recursoId + ": " + e.getMessage());
                    try { prestable.marcarComoDevuelto(); } catch (Exception ex) { /* Ignorar error secundario */ }
                }

            } else {
                try {
                    prestable.marcarComoDevuelto();
                    System.out.println("Recurso ID " + recursoId + " devuelto y ahora DISPONIBLE.");
                } catch (Exception e) {
                    System.err.println("Error al marcar el recurso ID " + recursoId + " como devuelto: " + e.getMessage());
                }
            }

        } else {

            System.err.println("Error CRÍTICO: El recurso devuelto (ID: " + recursoId + ") no implementa Prestable.");

        }
    }

    public Optional<Prestamo> buscarPrestamoPorRecursoId(String recursoId) {
        return Optional.ofNullable(prestamosActivosPorRecursoId.get(recursoId));
    }

    public List<Prestamo> listarPrestamosActivos() {
        return new ArrayList<>(prestamosActivosPorRecursoId.values());
    }

}