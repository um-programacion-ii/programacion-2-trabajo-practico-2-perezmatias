package com.biblioteca.servicio.alertas;

import com.biblioteca.servicio.GestorPrestamos;
import com.biblioteca.servicio.notificaciones.ServicioNotificaciones;
import com.biblioteca.modelo.prestamo.Prestamo;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import com.biblioteca.modelo.usuario.Usuario;
import com.biblioteca.modelo.recurso.RecursoDigital;

public class MonitorVencimientos {

    private final GestorPrestamos gestorPrestamos;
    private final ServicioNotificaciones servicioNotificaciones;

    public MonitorVencimientos(GestorPrestamos gestorPrestamos, ServicioNotificaciones servicioNotificaciones) {
        this.gestorPrestamos = Objects.requireNonNull(gestorPrestamos, "GestorPrestamos no puede ser nulo.");
        this.servicioNotificaciones = Objects.requireNonNull(servicioNotificaciones, "ServicioNotificaciones no puede ser nulo.");
    }
    public void verificarVencimientos() {
        System.out.println("\n[Monitor] Iniciando verificación de vencimientos...");
        LocalDate hoy = LocalDate.now();
        int alertasEnviadas = 0;
        List<Prestamo> prestamosActivos = gestorPrestamos.listarPrestamosActivos();

        if (prestamosActivos.isEmpty()) {
            System.out.println("[Monitor] No hay préstamos activos para verificar.");
            return;
        }

        System.out.println("[Monitor] Verificando " + prestamosActivos.size() + " préstamos activos...");
        for (Prestamo prestamo : prestamosActivos) {
            LocalDate fechaDevolucion = prestamo.getFechaDevolucionPrevista();
            Usuario usuario = prestamo.getUsuario();
            RecursoDigital recurso = prestamo.getRecurso();
            if (fechaDevolucion == null || usuario == null || recurso == null) {
                System.err.println("[Monitor] ADVERTENCIA: Préstamo ID " + prestamo.getIdPrestamo() + " tiene datos incompletos. Saltando...");
                continue;
            }

            String mensaje = "";
            String tipoNotificacion = "";

            if (fechaDevolucion.equals(hoy.plusDays(1))) {
                tipoNotificacion = "RECORDATORIO_VENCIMIENTO";
                mensaje = "Recordatorio: El préstamo del recurso '" + recurso.getTitulo() +
                        "' vence mañana (" + fechaDevolucion + ").";
            } else if (fechaDevolucion.equals(hoy)) {
                tipoNotificacion = "AVISO_VENCIMIENTO_HOY";
                mensaje = "¡Atención! El préstamo del recurso '" + recurso.getTitulo() +
                        "' vence HOY (" + fechaDevolucion + ").";
            } else if (fechaDevolucion.isBefore(hoy)) {
                tipoNotificacion = "PRESTAMO_VENCIDO";
                mensaje = "¡ALERTA! El préstamo del recurso '" + recurso.getTitulo() +
                        "' está VENCIDO. Debía devolverse el " + fechaDevolucion + ".";
            }
            if (!mensaje.isEmpty()) {
                try {
                    servicioNotificaciones.enviarNotificacion(tipoNotificacion, usuario.getId(), mensaje);
                    alertasEnviadas++;
                } catch (Exception e) {
                    System.err.println("[Monitor] Error al intentar enviar notificación para préstamo ID "
                            + prestamo.getIdPrestamo() + ": " + e.getMessage());
                }
            }
        }

        System.out.println("[Monitor] Verificación de vencimientos completada. Alertas enviadas: " + alertasEnviadas);
    }

}