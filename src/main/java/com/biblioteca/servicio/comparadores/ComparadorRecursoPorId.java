package com.biblioteca.servicio.comparadores;

import com.biblioteca.modelo.recurso.RecursoDigital;
import java.util.Comparator;
import java.util.Objects;

public class ComparadorRecursoPorId implements Comparator<RecursoDigital> {

    @Override
    public int compare(RecursoDigital r1, RecursoDigital r2) {
        String id1 = r1.getIdentificador();
        String id2 = r2.getIdentificador();

        return id1.compareTo(id2);
    }
}