package com.biblioteca.modelo.recurso;

import java.util.Objects;

public class Audiolibro extends RecursoBase {

    private String narrador;
    private int duracionMinutos;

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
    public String toString() {
        return "Audiolibro{ " + super.toString() +
                ", Narrador='" + narrador + '\'' +
                ", Duración=" + duracionMinutos + " min" +
                " }";
    }
}