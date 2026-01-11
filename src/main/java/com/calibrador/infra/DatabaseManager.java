package com.calibrador.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import java.net.URL;
import javax.swing.JOptionPane;

/**
 * Gestiona la conexión a la base de datos SQLite
 * Implementa patrón Singleton para una única conexión
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private Connection connection;
    private final String dbUrl;

    private static final int MAX_REINTENTOS = 3;
    private static final String DB_PATH = "/db/Inventario.db";

    /**
     * Constructor privado (Singleton)
     */
    private DatabaseManager() {
        this.dbUrl = "jdbc:sqlite:" + obtenerRutaBaseDatos();
    }

    /**
     * Obtiene la instancia única del DatabaseManager
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Obtiene la conexión activa a la base de datos
     * Si no existe o está cerrada, crea una nueva
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            conectar();
        }
        return connection;
    }

    /**
     * Establece la conexión con la base de datos
     * Implementa reintentos automáticos
     */
    private void conectar() throws SQLException {
        int reintentos = MAX_REINTENTOS;
        SQLException ultimoError = null;

        while (reintentos > 0) {
            try {
                connection = DriverManager.getConnection(dbUrl);
                System.out.println("✓ Conexión a la base de datos establecida correctamente");
                return;
            } catch (SQLException ex) {
                ultimoError = ex;
                reintentos--;

                JOptionPane.showMessageDialog(null,
                        "Error de conexión a la base de datos: " + ex.getMessage());

                if (reintentos > 0) {
                    JOptionPane.showMessageDialog(null,
                            "Reintentando conexión... (" + reintentos + " intentos restantes)");
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                    }
                } else {
                    JOptionPane.showMessageDialog(null,
                            "No se pudo conectar a la base de datos. El programa se cerrará.");
                    System.exit(1);
                }
            }
        }

        if (ultimoError != null) {
            throw ultimoError;
        }
    }

    /**
     * Obtiene la ruta de la base de datos
     * Busca en múltiples ubicaciones posibles
     */
    private String obtenerRutaBaseDatos() {
        try {
            // 1. Intentar obtener desde recursos del classpath
            URL resourceUrl = getClass().getResource(DB_PATH);
            if (resourceUrl != null) {
                return resourceUrl.toURI().getPath();
            }

            // 2. Intentar desde el directorio de la aplicación
            File appDir = new File(System.getProperty("user.dir"), "db/Inventario.db");
            if (appDir.exists()) {
                return appDir.getAbsolutePath();
            }

            // 3. Intentar desde el directorio de recursos de la aplicación
            File resourcesDir = new File(System.getProperty("user.dir"), "Resources/Inventario.db");
            if (resourcesDir.exists()) {
                return resourcesDir.getAbsolutePath();
            }

            // 4. Si no existe, crear directorio y usar ruta por defecto
            File dbDir = new File(System.getProperty("user.dir"), "resources/db");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            return new File(dbDir, "Inventario.db").getAbsolutePath();

        } catch (Exception e) {
            // Manejo de errores
            System.err.println("Error al localizar base de datos: " + e.getMessage());
            return System.getProperty("user.dir") + "/db/Inventario.db";
        }
    }

    /**
     * Cierra la conexión a la base de datos
     */
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Conexión a la base de datos cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al cerrar conexión: " + e.getMessage());
        }
    }

    /**
     * Verifica si la conexión está activa
     */
    public boolean isConexionActiva() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    /**
     * Obtiene la URL de la base de datos
     */
    public String getDbUrl() {
        return dbUrl;
    }
}
