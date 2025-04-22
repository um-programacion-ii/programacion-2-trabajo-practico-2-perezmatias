package com.biblioteca.ui;

import com.biblioteca.modelo.usuario.Usuario;
import com.biblioteca.servicio.GestorRecursos;
import com.biblioteca.servicio.GestorUsuarios;

import com.biblioteca.modelo.recurso.Libro;
import com.biblioteca.modelo.recurso.Revista;
import com.biblioteca.modelo.recurso.Audiolibro;
import com.biblioteca.modelo.recurso.RecursoDigital;
import com.biblioteca.modelo.recurso.CategoriaRecurso;
import java.util.Optional;

import java.util.InputMismatchException;
import java.util.Scanner;

import com.biblioteca.modelo.recurso.Prestable;
import com.biblioteca.modelo.recurso.Renovable;
import java.time.LocalDate;

import com.biblioteca.servicio.notificaciones.ServicioNotificaciones;
import java.util.Objects;
import java.util.List;

public class Consola {

    private final GestorUsuarios gestorUsuarios;
    private final GestorRecursos gestorRecursos;
    private final Scanner scanner;
    private final ServicioNotificaciones servicioNotificaciones;

    public Consola(GestorUsuarios gestorUsuarios, GestorRecursos gestorRecursos, ServicioNotificaciones servicioNotificaciones) {
        this.gestorUsuarios = gestorUsuarios;
        this.gestorRecursos = gestorRecursos;
        this.scanner = new Scanner(System.in);
        this.servicioNotificaciones = Objects.requireNonNull(servicioNotificaciones, "El servicio de notificaciones no puede ser nulo.");
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
        System.out.println("6. Buscar Recursos por Título");
        System.out.println("7. Listar Recursos por Categoría");
        System.out.println("8. Mostrar Categorías Disponibles");
        System.out.println("9. Prestar Recurso");
        System.out.println("10. Devolver Recurso");
        System.out.println("11. Renovar Préstamo");
        System.out.println("0. Volver al menú principal");
        System.out.print("Seleccione una opción: ");

        int opcion = leerOpcion();
        switch (opcion) {
            case 1: agregarNuevoLibro(); break;
            case 2: agregarNuevaRevista(); break;
            case 3: agregarNuevoAudiolibro(); break;
            case 4: listarRecursos(); break;
            case 5: buscarRecurso(); break;
            case 6: buscarRecursosPorTitulo(); break;
            case 7: listarRecursosPorCategoria(); break;
            case 8: mostrarCategoriasDisponibles(); break;
            case 9: prestarRecurso(); break;
            case 10: devolverRecurso(); break;
            case 11: renovarRecurso(); break;
            case 0: break;
            default: mostrarMensaje("Opción no válida."); break;
        }
    }

    private void agregarNuevoLibro() {
        mostrarMensaje("--- Agregar Nuevo Libro ---");
        try {
            String titulo = leerTexto("Ingrese título");
            String autor = leerTexto("Ingrese autor");
            String isbn = leerTexto("Ingrese ISBN");
            CategoriaRecurso categoria = seleccionarCategoria();
            Libro nuevoLibro = new Libro(titulo, autor, isbn, categoria);
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
            CategoriaRecurso categoria = seleccionarCategoria();
            Revista nuevaRevista = new Revista(titulo, edicion, periodicidad, categoria);
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
            CategoriaRecurso categoria = seleccionarCategoria();
            Audiolibro nuevoAudiolibro = new Audiolibro(titulo, narrador, duracion, categoria);
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

    private void prestarRecurso() {
        mostrarMensaje("--- Prestar Recurso ---");
        String recursoId = leerTexto("Ingrese ID del recurso a prestar");
        String usuarioId = leerTexto("Ingrese ID del usuario que recibe el préstamo");

        Optional<RecursoDigital> recursoOpt = gestorRecursos.buscarRecursoPorId(recursoId);
        Optional<Usuario> usuarioOpt = gestorUsuarios.buscarUsuarioPorId(usuarioId);

        if (recursoOpt.isEmpty()) {
            mostrarMensaje("Error: No se encontró el recurso con ID: " + recursoId);
            return;
        }
        if (usuarioOpt.isEmpty()) {
            mostrarMensaje("Error: No se encontró el usuario con ID: " + usuarioId);
            return;
        }

        RecursoDigital recurso = recursoOpt.get();
        Usuario usuario = usuarioOpt.get();

        if (recurso instanceof Prestable prestableRecurso) {
            if (prestableRecurso.estaDisponibleParaPrestamo()) {
                LocalDate fechaDevolucion = LocalDate.now().plusDays(14);
                prestableRecurso.marcarComoPrestado(usuario, fechaDevolucion);
                this.servicioNotificaciones.enviarNotificacion(
                        "PRESTAMO_EXITOSO",
                        usuario.getId(),
                        "Prestamo registrado: '" + recurso.getTitulo() + "' hasta " + fechaDevolucion
                );
            } else {
            }
        } else {
        }
    }

    private void devolverRecurso() {
        mostrarMensaje("--- Devolver Recurso ---");
        String recursoId = leerTexto("Ingrese ID del recurso a devolver");

        Optional<RecursoDigital> recursoOpt = gestorRecursos.buscarRecursoPorId(recursoId);

        if (recursoOpt.isEmpty()) {
            mostrarMensaje("Error: No se encontró el recurso con ID: " + recursoId);
            return;
        }

        RecursoDigital recurso = recursoOpt.get();

        if (recurso instanceof Prestable prestableRecurso) {
            Optional<Usuario> usuarioOptAnterior = prestableRecurso.getUsuarioPrestamo();
            String idUsuarioAnterior = usuarioOptAnterior.map(Usuario::getId).orElse("DESCONOCIDO");

            prestableRecurso.marcarComoDevuelto();

            this.servicioNotificaciones.enviarNotificacion(
                    "DEVOLUCION_EXITOSA",
                    idUsuarioAnterior,
                    "Devolución registrada: '" + recurso.getTitulo() + "'"
            );
        } else {
        }
    }

    private void renovarRecurso() {
        mostrarMensaje("--- Renovar Préstamo ---");
        String recursoId = leerTexto("Ingrese ID del recurso a renovar");

        Optional<RecursoDigital> recursoOpt = gestorRecursos.buscarRecursoPorId(recursoId);

        if (recursoOpt.isEmpty()) {
            mostrarMensaje("Error: No se encontró el recurso con ID: " + recursoId);
            return;
        }

        RecursoDigital recurso = recursoOpt.get();

        if (recurso instanceof Renovable renovableRecurso && recurso instanceof Prestable prestableRecurso) {
            Optional<LocalDate> fechaActualOpt = prestableRecurso.getFechaDevolucionPrevista();
            if(fechaActualOpt.isPresent()) {
                LocalDate nuevaFecha = fechaActualOpt.get().plusDays(7);

                boolean exito = renovableRecurso.renovarPrestamo(nuevaFecha);

                if (exito) {
                    String idUsuarioActual = prestableRecurso.getUsuarioPrestamo().map(Usuario::getId).orElse("DESCONOCIDO");
                    this.servicioNotificaciones.enviarNotificacion(
                            "RENOVACION_EXITOSA",
                            idUsuarioActual,
                            "Préstamo renovado para '" + recurso.getTitulo() + "' hasta " + nuevaFecha
                    );
                }
            } else {
            }
        } else {
        }
    }

    private CategoriaRecurso seleccionarCategoria() {
        mostrarMensaje("Seleccione la categoría del recurso:");
        CategoriaRecurso[] categorias = CategoriaRecurso.values();

        for (int i = 0; i < categorias.length; i++) {
            System.out.printf("%d. %s%n", i + 1, categorias[i].name());
        }

        int opcionNum;
        while (true) {
            System.out.print("Ingrese el número de la categoría: ");
            opcionNum = leerOpcion();

            if (opcionNum >= 1 && opcionNum <= categorias.length) {
                return categorias[opcionNum - 1];
            } else if (opcionNum != -1) {
                mostrarMensaje("Número de categoría no válido. Intente de nuevo.");
            }
        }
    }

    private void mostrarCategoriasDisponibles() {
        mostrarMensaje("--- Categorías Disponibles ---");
        int i = 1;
        for (CategoriaRecurso cat : CategoriaRecurso.values()) {
            System.out.println(i + ". " + cat.name());
            i++;
        }
        System.out.println("----------------------------");
    }

    private void listarRecursosPorCategoria() {
        mostrarMensaje("--- Listar Recursos por Categoría ---");

        CategoriaRecurso categoriaSeleccionada = seleccionarCategoria();

        List<RecursoDigital> recursosFiltrados = gestorRecursos.buscarPorCategoria(categoriaSeleccionada);

        if (recursosFiltrados.isEmpty()) {
            mostrarMensaje("No se encontraron recursos en la categoría: " + categoriaSeleccionada.name());
        } else {
            mostrarMensaje("Recursos encontrados en la categoría '" + categoriaSeleccionada.name() + "':");
            recursosFiltrados.forEach(recurso -> System.out.println(recurso));
        }
    }

    private void buscarRecursosPorTitulo() {
        mostrarMensaje("--- Buscar Recursos por Título ---");
        String textoBusqueda = leerTexto("Ingrese el texto a buscar en el título");

        if (textoBusqueda.trim().isEmpty()) {
            mostrarMensaje("La búsqueda no puede estar vacía.");
            return;
        }

        List<RecursoDigital> recursosEncontrados = gestorRecursos.buscarPorTitulo(textoBusqueda);

        if (recursosEncontrados.isEmpty()) {
            mostrarMensaje("No se encontraron recursos cuyo título contenga: '" + textoBusqueda + "'");
        } else {
            mostrarMensaje("Recursos encontrados con '" + textoBusqueda + "' en el título:");
            recursosEncontrados.forEach(recurso -> System.out.println(recurso));
        }
    }

    public void cerrarScanner() {
        if (scanner != null) {
            scanner.close();
        }
    }
}