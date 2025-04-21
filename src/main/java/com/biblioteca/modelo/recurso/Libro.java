package com.biblioteca.modelo.recurso;

import com.biblioteca.modelo.usuario.Usuario;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Objects;

public class Libro extends RecursoBase implements Prestable {

    private String autor;
    private String isbn;

    private Usuario usuarioPrestamo = null;
    private LocalDate fechaDevolucionPrevista = null;

    public Libro(String titulo, String autor, String isbn) {
        super(titulo);
        this.autor = Objects.requireNonNull(autor, "El autor no puede ser nulo");
        this.isbn = Objects.requireNonNull(isbn, "El ISBN no puede ser nulo");
    }

    public String getAutor() {
        return autor;
    }

    public String getIsbn() {
        return isbn;
    }

    @Override
    public String toString() {
        String infoBase = super.toString();
        String infoPrestamo = "";
        if (estado == EstadoRecurso.PRESTADO && usuarioPrestamo != null) {
            infoPrestamo = ", Prestado a=" + usuarioPrestamo.getNombre() +
                    ", Devolución=" + fechaDevolucionPrevista;
        }
        return "Libro{ " + infoBase +
                ", Autor='" + autor + '\'' +
                ", ISBN='" + isbn + '\'' +
                infoPrestamo + " }";
    }

    @Override
    public boolean estaDisponibleParaPrestamo() {

        return this.estado == EstadoRecurso.DISPONIBLE;
    }

    @Override
    public void marcarComoPrestado(Usuario usuario, LocalDate fechaDevolucion) {
        if (!estaDisponibleParaPrestamo()) {
            System.err.println("Error: El libro '" + getTitulo() + "' no está disponible para préstamo.");
            return;
        }
        this.usuarioPrestamo = Objects.requireNonNull(usuario, "El usuario no puede ser nulo.");
        this.fechaDevolucionPrevista = Objects.requireNonNull(fechaDevolucion, "La fecha de devolución no puede ser nula.");
        this.actualizarEstado(EstadoRecurso.PRESTADO);
        System.out.println("Libro '" + getTitulo() + "' prestado a " + usuario.getNombre() + " hasta " + fechaDevolucion);
    }

    @Override
    public void marcarComoDevuelto() {
        if (this.estado != EstadoRecurso.PRESTADO) {
            System.err.println("Advertencia: Intentando devolver el libro '" + getTitulo() + "' que no estaba prestado.");
        }
        this.usuarioPrestamo = null;
        this.fechaDevolucionPrevista = null;
        this.actualizarEstado(EstadoRecurso.DISPONIBLE);
        System.out.println("Libro '" + getTitulo() + "' devuelto.");
    }

    @Override
    public Optional<LocalDate> getFechaDevolucionPrevista() {

        return Optional.ofNullable(this.fechaDevolucionPrevista);
    }

    public Optional<Usuario> getUsuarioPrestamo() {
        return Optional.ofNullable(this.usuarioPrestamo);
    }

}