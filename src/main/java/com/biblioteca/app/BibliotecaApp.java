package com.biblioteca.app;

import com.biblioteca.servicio.GestorRecursos;
import com.biblioteca.servicio.GestorUsuarios;
import com.biblioteca.ui.Consola;
import com.biblioteca.servicio.notificaciones.ServicioNotificaciones;
import com.biblioteca.servicio.notificaciones.ServicioNotificacionesConsola;
import com.biblioteca.servicio.GestorPrestamos;
import com.biblioteca.servicio.GestorReservas;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BibliotecaApp {

    public static void main(String[] args) {
        System.out.println("Iniciando Biblioteca Digital...");
        ExecutorService executorNotificaciones = Executors.newSingleThreadExecutor();

        GestorUsuarios gestorUsuarios = new GestorUsuarios();
        GestorRecursos gestorRecursos = new GestorRecursos();
        GestorReservas gestorReservas = new GestorReservas();

        ServicioNotificaciones servicioNotificaciones = new ServicioNotificacionesConsola(executorNotificaciones);

        GestorPrestamos gestorPrestamos = new GestorPrestamos(gestorReservas, servicioNotificaciones);

        Consola consola = new Consola(gestorUsuarios, gestorRecursos, servicioNotificaciones, gestorPrestamos, gestorReservas);

        try {
            consola.iniciar();
        } finally {
            consola.cerrarScanner();
            apagarExecutor(executorNotificaciones);
        }

        System.out.println("Cerrando Biblioteca Digital.");
    }
    private static void apagarExecutor(ExecutorService executor) {
        System.out.println("Intentando apagar el servicio de notificaciones...");
        executor.shutdown();
        try {

            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("El servicio de notificaciones no terminó a tiempo, forzando apagado...");
                executor.shutdownNow();
                if (!executor.awaitTermination(5, TimeUnit.SECONDS))
                    System.err.println("El servicio de notificaciones no pudo ser apagado.");
            } else {
                System.out.println("Servicio de notificaciones apagado correctamente.");
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}