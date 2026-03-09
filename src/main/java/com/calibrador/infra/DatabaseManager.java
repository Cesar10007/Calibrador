package com.calibrador.infra;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;
import java.net.URL;

/**
 * Gestiona la conexión a la base de datos SQLite.
 * Patrón Singleton — una sola conexión en toda la app.
 *
 * Ya no llama a JOptionPane ni a System.exit.
 * Si no puede conectarse, lanza una SQLException
 * para que Main.java decida qué hacer.
 */
public class DatabaseManager {

    private static DatabaseManager instance;
    private Connection connection;
    private final String dbUrl;

    private static final String DB_PATH = "/db/Inventario.db";

    private DatabaseManager() {
        this.dbUrl = "jdbc:sqlite:" + obtenerRutaBaseDatos();
    }

    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Devuelve la conexión activa.
     * Si no existe o está cerrada, abre una nueva.
     * Lanza SQLException si no puede conectarse — el llamador decide qué hacer.
     */
    public Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(dbUrl);
            System.out.println("✓ Conexión a la base de datos establecida: " + dbUrl);
        }
        return connection;
    }

    /**
     * Cierra la conexión limpiamente.
     */
    public void cerrarConexion() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("✓ Conexión cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("✗ Error al cerrar conexión: " + e.getMessage());
        }
    }

    public boolean isConexionActiva() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public String getDbUrl() {
        return dbUrl;
    }

    /**
     * Busca la base de datos en varias ubicaciones posibles.
     * Orden: classpath → directorio de la app → Resources (macOS) → crea uno nuevo
     */
    private String obtenerRutaBaseDatos() {
        try {
            // 1. Dentro del JAR (classpath)
            URL resourceUrl = getClass().getResource(DB_PATH);
            if (resourceUrl != null) {
                return resourceUrl.toURI().getPath();
            }

            // 2. Junto al JAR en el sistema de archivos
            File appDir = new File(System.getProperty("user.dir"), "db/Inventario.db");
            if (appDir.exists()) {
                return appDir.getAbsolutePath();
            }

            // 3. Carpeta Resources (bundle macOS)
            File resourcesDir = new File(System.getProperty("user.dir"), "Resources/Inventario.db");
            if (resourcesDir.exists()) {
                return resourcesDir.getAbsolutePath();
            }

            // 4. No existe — crear la carpeta y dejar que DatabaseInitializer cree las tablas
            File dbDir = new File(System.getProperty("user.dir"), "db");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }
            System.out.println("⚠ Base de datos no encontrada, se creará en: " + dbDir.getAbsolutePath());
            return new File(dbDir, "Inventario.db").getAbsolutePath();

        } catch (Exception e) {
            System.err.println("Error al localizar la base de datos: " + e.getMessage());
            return System.getProperty("user.dir") + "/db/Inventario.db";
        }
    }
}