package com.biblioteca.modelo.recurso;

import java.util.Objects;

public class Revista extends RecursoBase {

    private int numeroEdicion;
    private String periodicidad;

    public Revista(String titulo, int numeroEdicion, String periodicidad) {
        super(titulo);
        if (numeroEdicion <= 0) {
            throw new IllegalArgumentException("El número de edición debe ser positivo.");
        }
        this.numeroEdicion = numeroEdicion;
        this.periodicidad = Objects.requireNonNull(periodicidad, "La periodicidad no puede ser nula");
    }

    public int getNumeroEdicion() {
        return numeroEdicion;
    }

    public String getPeriodicidad() {
        return periodicidad;
    }

    @Override
    public String toString() {
        return "Revista{ " + super.toString() +
                ", Edición=" + numeroEdicion +
                ", Periodicidad='" + periodicidad + '\'' +
                " }";
    }
}