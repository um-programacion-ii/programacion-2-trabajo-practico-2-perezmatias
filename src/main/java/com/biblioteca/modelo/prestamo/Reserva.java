package com.biblioteca.modelo.prestamo;

import com.biblioteca.modelo.recurso.RecursoDigital;
import com.biblioteca.modelo.usuario.Usuario;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

public class Reserva {

    private final String idReserva;
    private final RecursoDigital recurso;
    private final Usuario usuario;
    private final LocalDateTime fechaReserva;

    public Reserva(RecursoDigital recurso, Usuario usuario) {
        this.idReserva = UUID.randomUUID().toString();
        this.recurso = Objects.requireNonNull(recurso, "El recurso no puede ser nulo para la reserva.");
        this.usuario = Objects.requireNonNull(usuario, "El usuario no puede ser nulo para la reserva.");
        this.fechaReserva = LocalDateTime.now();
    }

    public String getIdReserva() {
        return idReserva;
    }

    public RecursoDigital getRecurso() {
        return recurso;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public LocalDateTime getFechaReserva() {
        return fechaReserva;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return Objects.equals(idReserva, reserva.idReserva);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idReserva);
    }

    @Override
    public String toString() {
        return "Reserva{" +
                "id='" + idReserva + '\'' +
                ", Recurso=" + recurso.getTitulo() + " (ID: " + recurso.getIdentificador() + ")" +
                ", Usuario=" + usuario.getNombre() + " (ID: " + usuario.getId() + ")" +
                ", Fecha=" + fechaReserva +
                '}';
    }
}