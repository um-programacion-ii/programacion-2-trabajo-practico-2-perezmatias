package com.biblioteca.modelo.recurso;
import com.biblioteca.excepciones.OperacionNoPermitidaException;
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
        Objects.requireNonNull(usuario, "El usuario no puede ser nulo.");
        Objects.requireNonNull(fechaDevolucion, "La fecha de devolución no puede ser nula.");

        if (!estaDisponibleParaPrestamo()) {
            throw new OperacionNoPermitidaException("El audiolibro '" + getTitulo() + "' no está disponible para préstamo.");
        }

        this.usuarioPrestamo = usuario;
        this.fechaDevolucionPrevista = fechaDevolucion;
        this.numeroRenovaciones = 0;
        this.actualizarEstado(EstadoRecurso.PRESTADO);
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
    public void renovarPrestamo(LocalDate nuevaFechaDevolucion) {
        Objects.requireNonNull(nuevaFechaDevolucion, "La nueva fecha de devolución no puede ser nula.");

        if (!puedeRenovarse(LocalDate.now())) {
            throw new OperacionNoPermitidaException("El préstamo del audiolibro '" + getTitulo() + "' no puede ser renovado ahora.");
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