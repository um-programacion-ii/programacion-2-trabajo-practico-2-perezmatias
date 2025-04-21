package com.biblioteca.modelo.recurso;


import com.biblioteca.modelo.usuario.Usuario;
import java.time.LocalDate;
import java.util.Optional;

public interface Prestable {

    boolean estaDisponibleParaPrestamo();

    void marcarComoPrestado(Usuario usuario, LocalDate fechaDevolucionPrevista);

    void marcarComoDevuelto();

    Optional<LocalDate> getFechaDevolucionPrevista();


}