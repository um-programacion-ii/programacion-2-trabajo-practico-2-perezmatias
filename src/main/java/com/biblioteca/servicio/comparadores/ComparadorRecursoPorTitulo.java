package com.biblioteca.servicio.comparadores;

import com.biblioteca.modelo.recurso.RecursoDigital;
import java.util.Comparator;
import java.util.Objects;

public class ComparadorRecursoPorTitulo implements Comparator<RecursoDigital> {

    @Override
    public int compare(RecursoDigital r1, RecursoDigital r2) {
        String titulo1 = r1.getTitulo();
        String titulo2 = r2.getTitulo();

        if (titulo1 == null && titulo2 == null) {
            return 0;
        }
        if (titulo1 == null) {
            return -1;
        }
        if (titulo2 == null) {
            return 1;
        }

        return titulo1.compareToIgnoreCase(titulo2);
    }
}