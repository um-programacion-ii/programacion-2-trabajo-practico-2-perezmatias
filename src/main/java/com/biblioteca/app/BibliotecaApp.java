package com.biblioteca.app;

import com.biblioteca.servicio.GestorRecursos;
import com.biblioteca.servicio.GestorUsuarios;
import com.biblioteca.servicio.GestorPrestamos;
import com.biblioteca.servicio.GestorReservas;
import com.biblioteca.servicio.alertas.MonitorVencimientos;
import com.biblioteca.servicio.notificaciones.ServicioNotificaciones;
import com.biblioteca.servicio.notificaciones.ServicioNotificacionesConsola;
import com.biblioteca.servicio.reportes.GeneradorReportes;
import com.biblioteca.ui.Consola;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class BibliotecaApp {

    public static void main(String[] args) {
        System.out.println("Iniciando Biblioteca Digital...");
        ExecutorService executorNotificaciones = Executors.newSingleThreadExecutor();
        ExecutorService executorReportes = Executors.newFixedThreadPool(2);
        ServicioNotificaciones servicioNotificaciones = new ServicioNotificacionesConsola(executorNotificaciones);
        GestorUsuarios gestorUsuarios = new GestorUsuarios();
        GestorRecursos gestorRecursos = new GestorRecursos();
        GestorReservas gestorReservas = new GestorReservas();
        GeneradorReportes generadorReportes = new GeneradorReportes(gestorRecursos, gestorUsuarios);
        GestorPrestamos gestorPrestamos = new GestorPrestamos(gestorReservas, servicioNotificaciones);
        MonitorVencimientos monitorVencimientos = new MonitorVencimientos(gestorPrestamos, servicioNotificaciones);
        Consola consola = new Consola(
                gestorUsuarios,
                gestorRecursos,
                servicioNotificaciones,
                gestorPrestamos,
                gestorReservas,
                monitorVencimientos,
                generadorReportes,
                executorReportes
        );
        try {
            consola.iniciar();
        } catch (Exception e) {
            System.err.println("¡ERROR INESPERADO EN LA APLICACIÓN PRINCIPAL! " + e.getMessage());
            e.printStackTrace();
        } finally {
            System.out.println("\n--- Iniciando cierre de la aplicación ---");
            consola.cerrarScanner();
            apagarExecutor(executorNotificaciones, "Notificaciones");
            apagarExecutor(executorReportes, "Reportes");
            System.out.println("--- Cierre de servicios finalizado ---");
        }
        System.out.println("Cerrando Biblioteca Digital.");
    }
    private static void apagarExecutor(ExecutorService executor, String nombreServicio) {
        if (executor == null || executor.isShutdown()) {
            return;
        }
        System.out.println("Intentando apagar el servicio de " + nombreServicio + "...");
        executor.shutdown();
        try {
            if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                System.err.println("El servicio de " + nombreServicio + " no terminó a tiempo, forzando apagado...");
                List<Runnable> tareasPendientes = executor.shutdownNow();
                System.err.println("Tareas pendientes canceladas: " + tareasPendientes.size());
                if (!executor.awaitTermination(5, TimeUnit.SECONDS))
                    System.err.println("El servicio de " + nombreServicio + " no pudo ser apagado limpiamente.");
            } else {
                System.out.println("Servicio de " + nombreServicio + " apagado correctamente.");
            }
        } catch (InterruptedException ie) {
            System.err.println("El apagado del servicio de " + nombreServicio + " fue interrumpido.");
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}