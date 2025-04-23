package com.biblioteca.servicio.alertas;

import com.biblioteca.servicio.GestorPrestamos;
import com.biblioteca.servicio.notificaciones.ServicioNotificaciones;
import com.biblioteca.modelo.prestamo.Prestamo;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

public class MonitorVencimientos {

    private final GestorPrestamos gestorPrestamos;
    private final ServicioNotificaciones servicioNotificaciones;

    public MonitorVencimientos(GestorPrestamos gestorPrestamos, ServicioNotificaciones servicioNotificaciones) {
        this.gestorPrestamos = Objects.requireNonNull(gestorPrestamos, "GestorPrestamos no puede ser nulo.");
        this.servicioNotificaciones = Objects.requireNonNull(servicioNotificaciones, "ServicioNotificaciones no puede ser nulo.");
    }
    public void verificarVencimientos() {
        System.out.println(">>> Lógica de verificarVencimientos PENDIENTE <<<");
        throw new UnsupportedOperationException("verificarVencimientos no implementado todavía.");
    }

}