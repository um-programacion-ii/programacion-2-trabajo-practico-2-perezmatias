package com.biblioteca.modelo.recurso;

import com.biblioteca.modelo.recurso.CategoriaRecurso;
import com.biblioteca.excepciones.OperacionNoPermitidaException;
import com.biblioteca.modelo.usuario.Usuario;
import java.time.LocalDate;
import java.util.Optional;
import java.util.Objects;

public class Libro extends RecursoBase implements Prestable, Renovable {

    private String autor;
    private String isbn;

    private Usuario usuarioPrestamo = null;
    private LocalDate fechaDevolucionPrevista = null;
    private int numeroRenovaciones = 0;

    public Libro(String titulo, String autor, String isbn, CategoriaRecurso categoria) {
        super(titulo, categoria);
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
                    ", Devolución=" + fechaDevolucionPrevista +
                    ", Renovaciones=" + numeroRenovaciones;
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
        Objects.requireNonNull(usuario, "El usuario no puede ser nulo.");
        Objects.requireNonNull(fechaDevolucion, "La fecha de devolución no puede ser nula.");

        if (!estaDisponibleParaPrestamo()) {
            throw new OperacionNoPermitidaException("El libro '" + getTitulo() + "' no está disponible para préstamo.");
        }

        this.usuarioPrestamo = usuario;
        this.fechaDevolucionPrevista = fechaDevolucion;
        this.numeroRenovaciones = 0;
        this.actualizarEstado(EstadoRecurso.PRESTADO);
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

    @Override
    public Optional<Usuario> getUsuarioPrestamo() {
        return Optional.ofNullable(this.usuarioPrestamo);
    }

    @Override
    public boolean puedeRenovarse(LocalDate fechaActual) {
        return this.estado == EstadoRecurso.PRESTADO;
    }

    @Override
    public void renovarPrestamo(LocalDate nuevaFechaDevolucion) {
        Objects.requireNonNull(nuevaFechaDevolucion, "La nueva fecha de devolución no puede ser nula.");

        if (!puedeRenovarse(LocalDate.now())) {
            throw new OperacionNoPermitidaException("El préstamo del libro '" + getTitulo() + "' no puede ser renovado ahora.");
        }
        if (!nuevaFechaDevolucion.isAfter(this.fechaDevolucionPrevista)) {
            throw new OperacionNoPermitidaException("La nueva fecha de devolución ("+ nuevaFechaDevolucion + ") debe ser posterior a la actual ("+ this.fechaDevolucionPrevista +").");
        }

        this.fechaDevolucionPrevista = nuevaFechaDevolucion;
        this.numeroRenovaciones++;
    }

    public int getNumeroRenovaciones() {
        return numeroRenovaciones;
    }

}