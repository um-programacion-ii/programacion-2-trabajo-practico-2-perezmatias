package com.biblioteca.modelo.prestamo;

import com.biblioteca.modelo.recurso.RecursoDigital;
import com.biblioteca.modelo.usuario.Usuario;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Prestamo {

    private final String idPrestamo;
    private final RecursoDigital recurso;
    private final Usuario usuario;
    private final LocalDate fechaPrestamo;
    private LocalDate fechaDevolucionPrevista;

    public Prestamo(RecursoDigital recurso, Usuario usuario, LocalDate fechaPrestamo, LocalDate fechaDevolucionPrevista) {
        this.idPrestamo = UUID.randomUUID().toString();
        this.recurso = Objects.requireNonNull(recurso, "El recurso no puede ser nulo.");
        this.usuario = Objects.requireNonNull(usuario, "El usuario no puede ser nulo.");
        this.fechaPrestamo = Objects.requireNonNull(fechaPrestamo, "La fecha de préstamo no puede ser nula.");
        this.fechaDevolucionPrevista = Objects.requireNonNull(fechaDevolucionPrevista, "La fecha de devolución prevista no puede ser nula.");
        if (!fechaDevolucionPrevista.isAfter(fechaPrestamo)) {
            throw new IllegalArgumentException("La fecha de devolución debe ser posterior a la fecha de préstamo.");
        }
    }

    public String getIdPrestamo() {
        return idPrestamo;
    }

    public RecursoDigital getRecurso() {
        return recurso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDate getFechaPrestamo() {
        return fechaPrestamo;
    }

    public LocalDate getFechaDevolucionPrevista() {
        return fechaDevolucionPrevista;
    }

    public void setFechaDevolucionPrevista(LocalDate nuevaFechaDevolucionPrevista) {
        Objects.requireNonNull(nuevaFechaDevolucionPrevista, "La nueva fecha de devolución no puede ser nula.");
        if (!nuevaFechaDevolucionPrevista.isAfter(this.fechaDevolucionPrevista)) {
            throw new IllegalArgumentException("La nueva fecha de devolución ("+ nuevaFechaDevolucionPrevista + ") debe ser posterior a la actual ("+ this.fechaDevolucionPrevista +").");
        }
        this.fechaDevolucionPrevista = nuevaFechaDevolucionPrevista;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Prestamo prestamo = (Prestamo) o;
        return Objects.equals(idPrestamo, prestamo.idPrestamo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idPrestamo);
    }

    @Override
    public String toString() {
        return "Prestamo{" +
                "id='" + idPrestamo + '\'' +
                ", Recurso=" + recurso.getTitulo() + " (ID: " + recurso.getIdentificador() + ")" +
                ", Usuario=" + usuario.getNombre() + " (ID: " + usuario.getId() + ")" +
                ", Fecha Préstamo=" + fechaPrestamo +
                ", Fecha Devolución=" + fechaDevolucionPrevista +
                '}';
    }
}