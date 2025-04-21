package com.biblioteca.servicio.notificaciones;

public class ServicioNotificacionesConsola implements ServicioNotificaciones {

    @Override
    public void enviarNotificacion(String tipo, String destinatarioId, String mensaje) {
        System.out.println("\n---=== [ NOTIFICACIÓN ] ===---");
        System.out.println("  Tipo         : " + tipo);
        System.out.println("  Destinatario : " + destinatarioId);
        System.out.println("  Mensaje      : " + mensaje);
        System.out.println("---========================---\n");
    }
}