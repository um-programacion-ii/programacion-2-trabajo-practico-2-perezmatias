package com.biblioteca.modelo.recurso;

import com.biblioteca.modelo.usuario.Usuario;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class Audiolibro extends RecursoBase implements Prestable {

    private String narrador;
    private int duracionMinutos;

    private Usuario usuarioPrestamo = null;
    private LocalDate fechaDevolucionPrevista = null;

    public Audiolibro(String titulo, String narrador, int duracionMinutos) {
        super(titulo);
        if (duracionMinutos <= 0) {
            throw new IllegalArgumentException("La duración en minutos debe ser positiva.");
        }
        this.narrador = Objects.requireNonNull(narrador, "El narrador no puede ser nulo");
        this.duracionMinutos = duracionMinutos;
    }

    public String getNarrador() {
        return narrador;
    }

    public int getDuracionMinutos() {
        return duracionMinutos;
    }

    @Override
    public boolean estaDisponibleParaPrestamo() {
        return this.estado == EstadoRecurso.DISPONIBLE;
    }

    @Override
    public void marcarComoPrestado(Usuario usuario, LocalDate fechaDevolucion) {
        if (!estaDisponibleParaPrestamo()) {
            System.err.println("Error: El audiolibro '" + getTitulo() + "' no está disponible para préstamo.");
            return;
        }
        this.usuarioPrestamo = Objects.requireNonNull(usuario, "El usuario no puede ser nulo.");
        this.fechaDevolucionPrevista = Objects.requireNonNull(fechaDevolucion, "La fecha de devolución no puede ser nula.");
        this.actualizarEstado(EstadoRecurso.PRESTADO);
        System.out.println("Audiolibro '" + getTitulo() + "' prestado a " + usuario.getNombre() + " hasta " + fechaDevolucion);
    }

    @Override
    public void marcarComoDevuelto() {
        if (this.estado != EstadoRecurso.PRESTADO) {
            System.err.println("Advertencia: Intentando devolver el audiolibro '" + getTitulo() + "' que no estaba prestado.");
        }
        this.usuarioPrestamo = null;
        this.fechaDevolucionPrevista = null;
        this.actualizarEstado(EstadoRecurso.DISPONIBLE);
        System.out.println("Audiolibro '" + getTitulo() + "' devuelto.");
    }

    @Override
    public Optional<LocalDate> getFechaDevolucionPrevista() {
        return Optional.ofNullable(this.fechaDevolucionPrevista);
    }

    public Optional<Usuario> getUsuarioPrestamo() {
        return Optional.ofNullable(this.usuarioPrestamo);
    }

    @Override
    public String toString() {
        String infoBase = super.toString();
        String infoPrestamo = "";

        if (estado == EstadoRecurso.PRESTADO && usuarioPrestamo != null) {
            infoPrestamo = ", Prestado a=" + usuarioPrestamo.getNombre() +
                    ", Devolución=" + fechaDevolucionPrevista;
        }

        return "Audiolibro{ " + infoBase +
                ", Narrador='" + narrador + '\'' +
                ", Duración=" + duracionMinutos + " min" +
                infoPrestamo + " }";
    }
}