package com.biblioteca.servicio.notificaciones;

public interface ServicioNotificaciones {

    void enviarNotificacion(String tipo, String destinatarioId, String mensaje);

}