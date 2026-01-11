package com.calibrador.service;

import com.calibrador.model.Producto;
import java.util.List;

/**
 * Interfaz del servicio de notificaciones
 * Define operaciones para mostrar alertas del sistema
 */
public interface NotificacionService {

    /**
     * Muestra una notificación del sistema
     * @param titulo título de la notificación
     * @param mensaje mensaje de la notificación
     */
    void mostrarNotificacion(String titulo, String mensaje);

    /**
     * Muestra notificación de productos próximos a vencer
     * @param productos lista de productos próximos a vencer
     */
    void notificarProductosProximosAVencer(List<Producto> productos);
}
