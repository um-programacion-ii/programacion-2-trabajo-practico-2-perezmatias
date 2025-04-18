package com.biblioteca.modelo.usuario;

import java.util.Objects;
import java.util.UUID;

public class Usuario {

    private final String id;
    private String nombre;
    private String email;


    public Usuario(String nombre, String email) {

        this.id = UUID.randomUUID().toString();
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
        this.email = Objects.requireNonNull(email, "El email no puede ser nulo");

    }


    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getEmail() {
        return email;
    }


    public void setNombre(String nombre) {
        this.nombre = Objects.requireNonNull(nombre, "El nombre no puede ser nulo");
    }

    public void setEmail(String email) {

        this.email = Objects.requireNonNull(email, "El email no puede ser nulo");
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Usuario usuario = (Usuario) o;
        return Objects.equals(id, usuario.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}