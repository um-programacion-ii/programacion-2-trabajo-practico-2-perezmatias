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

public class GestorReservas {

    private final Map<String, BlockingQueue<Usuario>> colasDeReserva = new ConcurrentHashMap<>();

    public GestorReservas() {
    }

    public void realizarReserva(Usuario usuario, RecursoDigital recurso) {
        System.out.println(">>> Lógica de realizarReserva PENDIENTE <<<");
        throw new UnsupportedOperationException("realizarReserva no implementado todavía.");
    }

    public Optional<Usuario> obtenerSiguienteUsuarioEnCola(String recursoId) {
        System.out.println(">>> Lógica de obtenerSiguienteUsuarioEnCola PENDIENTE <<<");
        throw new UnsupportedOperationException("obtenerSiguienteUsuarioEnCola no implementado todavía.");
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