package com.calibrador.app;

import com.calibrador.infra.DatabaseInitializer;
import com.calibrador.infra.DatabaseManager;
import com.calibrador.ui.view.Calibrador;
import javax.swing.*;
import java.awt.*;

/**
 * Clase principal de la aplicación
 * Punto de entrada que inicializa la base de datos y lanza la interfaz gráfica
 */
public class Main {

    public static void main(String[] args) {
        // Configurar Look and Feel del sistema operativo
        configurarLookAndFeel();

        // Inicializar base de datos
        inicializarBaseDatos();

        // Lanzar interfaz gráfica en el hilo de eventos de Swing
        SwingUtilities.invokeLater(() -> {
            try {
                Calibrador ventana = new Calibrador();
                ventana.setVisible(true);

                System.out.println("✓ Aplicación iniciada correctamente");

            } catch (Exception e) {
                mostrarErrorCritico("Error al iniciar la aplicación", e);
            }
        });
    }

    /**
     * Configura el Look and Feel de la aplicación
     * Intenta usar el del sistema operativo para mejor integración
     */
    private static void configurarLookAndFeel() {
        try {
            // Intentar usar el Look and Feel del sistema operativo
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println("✓ Look and Feel configurado: " + UIManager.getLookAndFeel().getName());

        } catch (Exception e) {
            System.err.println("⚠ No se pudo configurar Look and Feel del sistema, usando predeterminado");
            // Continuar con el Look and Feel por defecto de Java
        }
    }

    /**
     * Inicializa la estructura de la base de datos
     * Crea las tablas si no existen
     */
    private static void inicializarBaseDatos() {
        try {
            System.out.println("Inicializando base de datos...");

            DatabaseInitializer initializer = new DatabaseInitializer();
            initializer.inicializar();

            System.out.println("✓ Base de datos inicializada correctamente");

        } catch (Exception e) {
            System.err.println("✗ Error al inicializar base de datos: " + e.getMessage());

            int opcion = JOptionPane.showConfirmDialog(
                    null,
                    "Error al inicializar la base de datos.\n" +
                            "¿Desea intentar continuar de todos modos?\n\n" +
                            "Error: " + e.getMessage(),
                    "Error de Inicialización",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.ERROR_MESSAGE
            );

            if (opcion != JOptionPane.YES_OPTION) {
                System.exit(1);
            }
        }
    }

    /**
     * Muestra un mensaje de error crítico y cierra la aplicación
     * @param mensaje mensaje descriptivo del error
     * @param excepcion excepción que causó el error
     */
    private static void mostrarErrorCritico(String mensaje, Exception excepcion) {
        System.err.println("✗ ERROR CRÍTICO: " + mensaje);
        excepcion.printStackTrace();

        JOptionPane.showMessageDialog(
                null,
                mensaje + "\n\n" +
                        "Detalles: " + excepcion.getMessage() + "\n\n" +
                        "La aplicación se cerrará.",
                "Error Crítico",
                JOptionPane.ERROR_MESSAGE
        );

        System.exit(1);
    }
}
