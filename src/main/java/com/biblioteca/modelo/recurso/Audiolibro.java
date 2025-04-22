package com.biblioteca.modelo.recurso;

import com.biblioteca.modelo.usuario.Usuario;
import com.biblioteca.modelo.recurso.CategoriaRecurso;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;

public class Audiolibro extends RecursoBase implements Prestable, Renovable {

    private String narrador;
    private int duracionMinutos;

    private Usuario usuarioPrestamo = null;
    private LocalDate fechaDevolucionPrevista = null;

    private int numeroRenovaciones = 0;

    public Audiolibro(String titulo, String narrador, int duracionMinutos, CategoriaRecurso categoria) {
        super(titulo, categoria);
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
        this.numeroRenovaciones = 0;
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

    @Override
    public Optional<Usuario> getUsuarioPrestamo() {
        return Optional.ofNullable(this.usuarioPrestamo);
    }

    @Override
    public boolean puedeRenovarse(LocalDate fechaActual) {
        return this.estado == EstadoRecurso.PRESTADO;
    }

    @Override
    public boolean renovarPrestamo(LocalDate nuevaFechaDevolucion) {
        if (!puedeRenovarse(LocalDate.now())) {
            System.err.println("Error: El préstamo del audiolibro '" + getTitulo() + "' no puede ser renovado ahora.");
            return false;
        }
        if (nuevaFechaDevolucion == null || !nuevaFechaDevolucion.isAfter(this.fechaDevolucionPrevista)) {
            System.err.println("Error: La nueva fecha de devolución debe ser posterior a la actual ("+ this.fechaDevolucionPrevista +").");
            return false;
        }

        this.fechaDevolucionPrevista = nuevaFechaDevolucion;
        this.numeroRenovaciones++;
        System.out.println("Préstamo del audiolibro '" + getTitulo() + "' renovado hasta " + nuevaFechaDevolucion
                + ". Renovaciones: " + this.numeroRenovaciones);
        return true;
    }


    public int getNumeroRenovaciones() {
        return numeroRenovaciones;
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
        return "Audiolibro{ " + infoBase +
                ", Narrador='" + narrador + '\'' +
                ", Duración=" + duracionMinutos + " min" +
                infoPrestamo + " }";
    }
}