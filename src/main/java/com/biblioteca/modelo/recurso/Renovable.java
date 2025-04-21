package com.biblioteca.modelo.recurso;

import java.time.LocalDate;

public interface Renovable {

    boolean puedeRenovarse(LocalDate fechaActual);

    boolean renovarPrestamo(LocalDate nuevaFechaDevolucion);

}