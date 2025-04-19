package com.biblioteca.ui;

import com.biblioteca.modelo.usuario.Usuario;
import com.biblioteca.servicio.GestorRecursos;
import com.biblioteca.servicio.GestorUsuarios;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Consola {

    private final GestorUsuarios gestorUsuarios;
    private final GestorRecursos gestorRecursos;
    private final Scanner scanner;

    public Consola(GestorUsuarios gestorUsuarios, GestorRecursos gestorRecursos) {
        this.gestorUsuarios = gestorUsuarios;
        this.gestorRecursos = gestorRecursos;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        int opcion;
        do {
            mostrarMenuPrincipal();
            opcion = leerOpcion();
            procesarOpcionPrincipal(opcion);
        } while (opcion != 0);

        cerrarScanner();
        System.out.println("¡Gracias por usar la Biblioteca Digital! Adiós.");
    }

    private void mostrarMenuPrincipal() {
        System.out.println("\n--- Biblioteca Digital ---");
        System.out.println("1. Gestionar Usuarios");
        System.out.println("2. Gestionar Recursos");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opción: ");
    }

    private int leerOpcion() {
        try {
            int opcion = scanner.nextInt();
            scanner.nextLine();
            return opcion;
        } catch (InputMismatchException e) {
            System.err.println("Error: Por favor, ingrese un número válido.");
            scanner.nextLine();
            return -1;
        }
    }

    private String leerTexto(String mensaje) {
        System.out.print(mensaje + ": ");
        return scanner.nextLine();
    }

    private void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }

    private void procesarOpcionPrincipal(int opcion) {
        switch (opcion) {
            case 1:
                gestionarUsuarios();
                break;
            case 2:
                gestionarRecursos();
                break;
            case 0:
                break;
            case -1:
                break;
            default:
                mostrarMensaje("Opción no válida. Intente de nuevo.");
                break;
        }
    }


    private void gestionarUsuarios() {
        System.out.println("\n--- Gestión de Usuarios ---");
        System.out.println("1. Agregar Usuario");
        System.out.println("2. Listar Usuarios");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion();
        switch (opcion) {
            case 1:
                agregarNuevoUsuario();
                break;
            case 2:
                listarUsuarios();
                break;
            case 0:
                break;
            default:
                mostrarMensaje("Opción no válida.");
                break;
        }
    }

    private void agregarNuevoUsuario() {
        mostrarMensaje("--- Agregar Nuevo Usuario ---");
        String nombre = leerTexto("Ingrese nombre del usuario");
        String email = leerTexto("Ingrese email del usuario");
        Usuario nuevoUsuario = new Usuario(nombre, email);
        if (gestorUsuarios.agregarUsuario(nuevoUsuario)) {
            mostrarMensaje("Usuario agregado con éxito.");
        } else {
            mostrarMensaje("No se pudo agregar el usuario (posible ID duplicado o datos inválidos).");
        }
    }

    private void listarUsuarios() {
        mostrarMensaje("--- Listado de Usuarios ---");
        var usuarios = gestorUsuarios.listarTodosLosUsuarios();
        if (usuarios.isEmpty()) {
            mostrarMensaje("No hay usuarios registrados.");
        } else {

            usuarios.forEach(usuario -> System.out.println(usuario));
        }
    }


    private void gestionarRecursos() {
        mostrarMensaje(">>> Gestión de Recursos (Pendiente de Implementar) <<<");
    }

    public void cerrarScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}