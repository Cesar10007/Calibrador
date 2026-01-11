package com.calibrador.service;

import com.calibrador.model.Producto;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Implementación del servicio de notificaciones
 * Maneja notificaciones de bandeja del sistema para alertas de calibración
 */
public class NotificacionServiceImpl implements NotificacionService {

    private static Image icono;

    static {
        try {
            // Cargar un icono predeterminado
            icono = Toolkit.getDefaultToolkit().getImage(
                    NotificacionServiceImpl.class.getResource("/icono.png")
            );

            // Si no encuentra un icono, usa un icono básico
            if (icono == null) {
                icono = crearIconoBasico();
            }
        } catch (Exception e) {
            // Crear un icono básico si falla la carga
            icono = crearIconoBasico();
        }
    }

    @Override
    public void mostrarNotificacion(String titulo, String mensaje) {
        try {
            // Verificar soporte del sistema de bandeja
            if (!SystemTray.isSupported()) {
                System.out.println("¡El área de notificación no es compatible!");
                // Mostrar como JOptionPane si no hay soporte de bandeja
                JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
                return;
            }

            // Obtener la bandeja del sistema
            SystemTray tray = SystemTray.getSystemTray();

            // Crear icono de bandeja
            TrayIcon trayIcon = new TrayIcon(icono, "Notificación de Calibración");
            trayIcon.setImageAutoSize(true);
            trayIcon.setToolTip("Notificación de Calibración");

            // Añadir un menú emergente opcional
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Cerrar");
            defaultItem.addActionListener(e -> tray.remove(trayIcon));
            popup.add(defaultItem);
            trayIcon.setPopupMenu(popup);

            // Añadir el icono a la bandeja
            tray.add(trayIcon);

            // Mostrar mensaje
            trayIcon.displayMessage(titulo, mensaje, TrayIcon.MessageType.INFO);

        } catch (AWTException e) {
            // Mostrar como JOptionPane si falla la notificación del sistema
            System.err.println("Error al mostrar notificación del sistema: " + e.getMessage());
            JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
        }
    }

    @Override
    public void notificarProductosProximosAVencer(List<Producto> productos) {
        if (productos == null || productos.isEmpty()) {
            return;
        }

        StringBuilder mensaje = new StringBuilder();
        mensaje.append("Los siguientes productos están próximos a vencer:\n\n");

        for (Producto producto : productos) {
            mensaje.append("• ")
                    .append(producto.getNombre())
                    .append(" - expira en ")
                    .append(producto.getDiasRestantes())
                    .append(" días\n");
        }

        mostrarNotificacion("Alerta de Expiración", mensaje.toString());
    }

    /**
     * Crea un icono básico si no se puede cargar uno desde recursos
     */
    private static Image crearIconoBasico() {
        BufferedImage imagen = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics g = imagen.getGraphics();
        g.setColor(Color.BLUE);
        g.fillRect(0, 0, 16, 16);
        g.dispose();
        return imagen;
    }
}
