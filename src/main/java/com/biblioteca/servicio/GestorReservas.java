package com.biblioteca.servicio;

import com.biblioteca.modelo.recurso.RecursoDigital;
import com.biblioteca.modelo.usuario.Usuario;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;
import java.util.ArrayList;
import com.biblioteca.modelo.recurso.EstadoRecurso;
import com.biblioteca.excepciones.OperacionNoPermitidaException;
import java.util.Objects;

public class GestorReservas {

    private final Map<String, BlockingQueue<Usuario>> colasDeReserva = new ConcurrentHashMap<>();

    public GestorReservas() {
    }

    public synchronized void realizarReserva(Usuario usuario, RecursoDigital recurso) {
        Objects.requireNonNull(usuario, "El usuario no puede ser nulo para reservar.");
        Objects.requireNonNull(recurso, "El recurso no puede ser nulo para reservar.");
        if (recurso.getEstado() != EstadoRecurso.PRESTADO) {
            throw new OperacionNoPermitidaException("El recurso '" + recurso.getTitulo()
                    + "' no se puede reservar porque su estado actual es " + recurso.getEstado()
                    + " (sólo se reservan recursos PRESTADOS).");
        }
        BlockingQueue<Usuario> cola = getColaParaRecurso(recurso.getIdentificador());

        if (cola.contains(usuario)) {
            throw new OperacionNoPermitidaException("El usuario '" + usuario.getNombre()
                    + "' ya tiene una reserva activa para el recurso '" + recurso.getTitulo() + "'.");
        }
        boolean anadido = cola.offer(usuario);

        if (!anadido) {
            System.err.println("Error crítico: No se pudo añadir al usuario " + usuario.getNombre() + " a la cola de reserva para " + recurso.getTitulo());
            throw new RuntimeException("No se pudo añadir la reserva a la cola por un problema interno.");
        }

        System.out.println("Reserva realizada por " + usuario.getNombre() + " para '" + recurso.getTitulo() + "'. Usuarios en cola: " + cola.size());
    }

    public synchronized Optional<Usuario> obtenerSiguienteUsuarioEnCola(String recursoId) {
        Objects.requireNonNull(recursoId, "El ID del recurso no puede ser nulo.");
        BlockingQueue<Usuario> cola = colasDeReserva.get(recursoId);

        if (cola == null || cola.isEmpty()) {
            return Optional.empty();
        }
        Usuario siguienteUsuario = cola.poll();
        return Optional.ofNullable(siguienteUsuario);
    }

    public boolean hayReservas(String recursoId) {
        BlockingQueue<Usuario> cola = colasDeReserva.get(recursoId);
        return cola != null && !cola.isEmpty();
    }

    private BlockingQueue<Usuario> getColaParaRecurso(String recursoId) {
        return colasDeReserva.computeIfAbsent(recursoId, k -> new LinkedBlockingQueue<>());
    }

    public Map<String, List<String>> getEstadoReservas() {
        Map<String, List<String>> estadoActual = new HashMap<>();
        colasDeReserva.forEach((recursoId, cola) -> {
            List<String> nombresUsuarios = new ArrayList<>(cola).stream()
                    .map(Usuario::getNombre)
                    .collect(Collectors.toList());
            if (!nombresUsuarios.isEmpty()) {
                estadoActual.put(recursoId, nombresUsuarios);
            }
        });
        return estadoActual;
    }
}