package com.biblioteca.modelo.recurso;

import java.util.Objects;

public class Libro extends RecursoBase {

    private String autor;
    private String isbn;

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

        return "Libro{ " + super.toString() +
                ", Autor='" + autor + '\'' +
                ", ISBN='" + isbn + '\'' +
                " }";
    }

}