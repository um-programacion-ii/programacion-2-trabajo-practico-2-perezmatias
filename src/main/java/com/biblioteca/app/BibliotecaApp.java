package com.biblioteca.app;

import com.biblioteca.servicio.GestorRecursos;
import com.biblioteca.servicio.GestorUsuarios;
import com.biblioteca.ui.Consola;
import com.biblioteca.servicio.notificaciones.ServicioNotificaciones;
import com.biblioteca.servicio.notificaciones.ServicioNotificacionesConsola;
import com.biblioteca.servicio.GestorPrestamos;
import com.biblioteca.servicio.GestorReservas;

public class BibliotecaApp {

    public static void main(String[] args) {
        System.out.println("Iniciando Biblioteca Digital...");


        GestorUsuarios gestorUsuarios = new GestorUsuarios();
        GestorRecursos gestorRecursos = new GestorRecursos();
        ServicioNotificaciones servicioNotificaciones = new ServicioNotificacionesConsola();
        GestorReservas gestorReservas = new GestorReservas();
        GestorPrestamos gestorPrestamos = new GestorPrestamos(gestorReservas, servicioNotificaciones);

        Consola consola = new Consola(gestorUsuarios, gestorRecursos, servicioNotificaciones, gestorPrestamos,gestorReservas);

        try {
            consola.iniciar();
        } finally {
            consola.cerrarScanner();
        }

        System.out.println("Cerrando Biblioteca Digital.");
    }
}