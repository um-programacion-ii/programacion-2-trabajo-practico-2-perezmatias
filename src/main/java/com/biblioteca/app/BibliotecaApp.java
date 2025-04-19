package com.biblioteca.app;

import com.biblioteca.servicio.GestorRecursos;
import com.biblioteca.servicio.GestorUsuarios;
import com.biblioteca.ui.Consola;

public class BibliotecaApp {

    public static void main(String[] args) {
        System.out.println("Iniciando Biblioteca Digital...");

        GestorUsuarios gestorUsuarios = new GestorUsuarios();
        GestorRecursos gestorRecursos = new GestorRecursos();

        Consola consola = new Consola(gestorUsuarios, gestorRecursos);

        try {
            consola.iniciar();
        } finally {
            consola.cerrarScanner();
        }

        System.out.println("Cerrando Biblioteca Digital.");
    }
}