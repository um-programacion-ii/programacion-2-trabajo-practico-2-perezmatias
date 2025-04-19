package com.biblioteca.ui;

import com.biblioteca.modelo.usuario.Usuario;
import com.biblioteca.servicio.GestorRecursos;
import com.biblioteca.servicio.GestorUsuarios;

import com.biblioteca.modelo.recurso.Libro;
import com.biblioteca.modelo.recurso.Revista;
import com.biblioteca.modelo.recurso.Audiolibro;
import com.biblioteca.modelo.recurso.RecursoDigital;
import java.util.Optional;

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
        System.out.println("\n--- Gestión de Recursos ---");
        System.out.println("1. Agregar Libro");
        System.out.println("2. Agregar Revista");
        System.out.println("3. Agregar Audiolibro");
        System.out.println("4. Listar Todos los Recursos");
        System.out.println("5. Buscar Recurso por ID");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion();
        switch (opcion) {
            case 1:
                agregarNuevoLibro();
                break;
            case 2:
                agregarNuevaRevista();
                break;
            case 3:
                agregarNuevoAudiolibro();
                break;
            case 4:
                listarRecursos();
                break;
            case 5:
                buscarRecurso();
                break;
            case 0:
                break;
            default:
                mostrarMensaje("Opción no válida.");
                break;
        }
    }

    private void agregarNuevoLibro() {
        mostrarMensaje("--- Agregar Nuevo Libro ---");
        try {
            String titulo = leerTexto("Ingrese título");
            String autor = leerTexto("Ingrese autor");
            String isbn = leerTexto("Ingrese ISBN");
            Libro nuevoLibro = new Libro(titulo, autor, isbn);
            if (gestorRecursos.agregarRecurso(nuevoLibro)) {
                mostrarMensaje("Libro agregado con éxito.");
            } else {
                mostrarMensaje("No se pudo agregar el libro (posible ID duplicado o datos inválidos).");
            }
        } catch (NullPointerException | IllegalArgumentException e) {
            mostrarMensaje("Error en los datos ingresados: " + e.getMessage());
        }
    }

    private void agregarNuevaRevista() {
        mostrarMensaje("--- Agregar Nueva Revista ---");
        try {
            String titulo = leerTexto("Ingrese título");
            int edicion = Integer.parseInt(leerTexto("Ingrese número de edición"));
            String periodicidad = leerTexto("Ingrese periodicidad (ej. Mensual)");
            Revista nuevaRevista = new Revista(titulo, edicion, periodicidad);
            if (gestorRecursos.agregarRecurso(nuevaRevista)) {
                mostrarMensaje("Revista agregada con éxito.");
            } else {
                mostrarMensaje("No se pudo agregar la revista (posible ID duplicado o datos inválidos).");
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Error: El número de edición debe ser un número entero.");
        } catch (NullPointerException | IllegalArgumentException e) {
            mostrarMensaje("Error en los datos ingresados: " + e.getMessage());
        }
    }

    private void agregarNuevoAudiolibro() {
        mostrarMensaje("--- Agregar Nuevo Audiolibro ---");
        try {
            String titulo = leerTexto("Ingrese título");
            String narrador = leerTexto("Ingrese narrador");
            int duracion = Integer.parseInt(leerTexto("Ingrese duración en minutos"));
            Audiolibro nuevoAudiolibro = new Audiolibro(titulo, narrador, duracion);
            if (gestorRecursos.agregarRecurso(nuevoAudiolibro)) {
                mostrarMensaje("Audiolibro agregado con éxito.");
            } else {
                mostrarMensaje("No se pudo agregar el audiolibro (posible ID duplicado o datos inválidos).");
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Error: La duración debe ser un número entero.");
        } catch (NullPointerException | IllegalArgumentException e) {
            mostrarMensaje("Error en los datos ingresados: " + e.getMessage());
        }
    }

    private void listarRecursos() {
        mostrarMensaje("--- Listado de Recursos ---");
        var recursos = gestorRecursos.listarTodosLosRecursos();
        if (recursos.isEmpty()) {
            mostrarMensaje("No hay recursos registrados.");
        } else {
            recursos.forEach(recurso -> System.out.println(recurso));
        }
    }

    private void buscarRecurso() {
        mostrarMensaje("--- Buscar Recurso por ID ---");
        String id = leerTexto("Ingrese el ID del recurso a buscar");
        Optional<RecursoDigital> recursoOpt = gestorRecursos.buscarRecursoPorId(id);
        recursoOpt.ifPresentOrElse(
                recurso -> {
                    mostrarMensaje("Recurso encontrado:");
                    System.out.println(recurso);
                },
                () -> mostrarMensaje("No se encontró ningún recurso con el ID: " + id)
        );
    }

    public void cerrarScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}