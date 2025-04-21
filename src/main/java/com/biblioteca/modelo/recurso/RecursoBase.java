package com.biblioteca.modelo.recurso;

import java.util.Objects;
import java.util.UUID;

public abstract class RecursoBase implements RecursoDigital {

    protected final String id;
    protected String titulo;
    protected EstadoRecurso estado;

    public RecursoBase(String titulo) {
        this.id = UUID.randomUUID().toString();
        this.titulo = Objects.requireNonNull(titulo, "El título no puede ser nulo");
        this.estado = EstadoRecurso.DISPONIBLE;
    }

    @Override
    public String getIdentificador() {
        return this.id;
    }

    @Override
    public String getTitulo() {
        return this.titulo;
    }

    @Override
    public EstadoRecurso getEstado() {
        return this.estado;
    }

    @Override
    public void actualizarEstado(EstadoRecurso nuevoEstado) {
        this.estado = Objects.requireNonNull(nuevoEstado, "El nuevo estado no puede ser nulo");
        System.out.println("Estado de '" + titulo + "' actualizado a: " + nuevoEstado);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RecursoBase that = (RecursoBase) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "ID='" + id + '\'' +
                ", Titulo='" + titulo + '\'' +
                ", Estado=" + estado;
    }
}