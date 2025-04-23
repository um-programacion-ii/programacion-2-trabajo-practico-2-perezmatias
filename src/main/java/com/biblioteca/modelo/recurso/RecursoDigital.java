package com.biblioteca.modelo.recurso;

public interface RecursoDigital {

    String getIdentificador();

    String getTitulo();

    EstadoRecurso getEstado();

    void actualizarEstado(EstadoRecurso nuevoEstado);

    CategoriaRecurso getCategoria();

    void incrementarVecesPrestado();

    int getVecesPrestado();

}