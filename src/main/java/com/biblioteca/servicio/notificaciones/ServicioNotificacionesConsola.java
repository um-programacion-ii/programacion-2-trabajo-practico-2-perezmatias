package com.biblioteca.servicio.notificaciones;

import java.util.concurrent.ExecutorService;
import java.util.Objects;

public class ServicioNotificacionesConsola implements ServicioNotificaciones {

    private final ExecutorService executor;

    public ServicioNotificacionesConsola(ExecutorService executor) {
        this.executor = Objects.requireNonNull(executor, "ExecutorService no puede ser nulo.");
    }

    @Override
    public void enviarNotificacion(String tipo, String destinatarioId, String mensaje) {
        executor.execute(() -> {
            System.out.println("\n---=== [ NOTIFICACIÓN ASÍNCRONA ] ===---");
            System.out.println("  Tipo         : " + tipo);
            System.out.println("  Destinatario : " + destinatarioId);
            System.out.println("  Mensaje      : " + mensaje);
            System.out.println("---=================================---\n");
        });
    }
}