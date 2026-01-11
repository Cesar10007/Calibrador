package com.calibrador.infra;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import javax.swing.JOptionPane;

/**
 * Inicializa la estructura de la base de datos
 * Crea tablas si no existen
 */
public class DatabaseInitializer {

    private final DatabaseManager databaseManager;

    public DatabaseInitializer() {
        this.databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Inicializa todas las tablas necesarias
     */
    public void inicializar() {
        try {
            System.out.println("Iniciando creación de tablas...");

            crearTablaProducto();
            crearTablaEquipo();
            crearTablaCalibracion();

            System.out.println("✓ Base de datos inicializada correctamente");

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al inicializar la base de datos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Crea la tabla Producto1 (productos de calibración)
     */
    private void crearTablaProducto() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Producto1 (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    NOMBRE TEXT NOT NULL," +
                "    FECHACALIBRACION TEXT NOT NULL," +
                "    FECHAEXPIRACION TEXT NOT NULL," +
                "    MESES_VALIDEZ INTEGER NOT NULL," +
                "    ALERTA_MOSTRADA INTEGER DEFAULT 0," +
                "    FECHA_CREACION TEXT DEFAULT CURRENT_TIMESTAMP," +
                "    ACTIVO INTEGER DEFAULT 1" +
                ")";

        ejecutarSQL(sql, "Tabla Producto1 creada/verificada correctamente");
    }

    /**
     * Crea la tabla Equipo (equipos de laboratorio)
     */
    private void crearTablaEquipo() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Equipo (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    CODIGO TEXT UNIQUE NOT NULL," +
                "    NOMBRE TEXT NOT NULL," +
                "    MARCA TEXT," +
                "    MODELO TEXT," +
                "    SERIE TEXT," +
                "    UBICACION TEXT," +
                "    ESTADO TEXT DEFAULT 'OPERATIVO'," +
                "    FECHA_ADQUISICION TEXT," +
                "    FECHA_CREACION TEXT DEFAULT CURRENT_TIMESTAMP," +
                "    ACTIVO INTEGER DEFAULT 1" +
                ")";

        ejecutarSQL(sql, "Tabla Equipo creada/verificada correctamente");
    }

    /**
     * Crea la tabla Calibracion (registro histórico de calibraciones)
     */
    private void crearTablaCalibracion() throws SQLException {
        String sql = "CREATE TABLE IF NOT EXISTS Calibracion (" +
                "    ID INTEGER PRIMARY KEY AUTOINCREMENT," +
                "    EQUIPO_ID INTEGER NOT NULL," +
                "    PRODUCTO_ID INTEGER," +
                "    FECHA_CALIBRACION TEXT NOT NULL," +
                "    FECHA_PROXIMA TEXT NOT NULL," +
                "    TECNICO TEXT," +
                "    RESULTADO TEXT," +
                "    OBSERVACIONES TEXT," +
                "    CERTIFICADO_PATH TEXT," +
                "    FECHA_REGISTRO TEXT DEFAULT CURRENT_TIMESTAMP," +
                "    FOREIGN KEY (EQUIPO_ID) REFERENCES Equipo(ID)," +
                "    FOREIGN KEY (PRODUCTO_ID) REFERENCES Producto1(ID)" +
                ")";

        ejecutarSQL(sql, "Tabla Calibracion creada/verificada correctamente");
    }

    /**
     * Ejecuta una sentencia SQL DDL
     */
    private void ejecutarSQL(String sql, String mensajeExito) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("  ✓ " + mensajeExito);
        }
    }

    /**
     * Verifica si las tablas existen
     */
    public boolean verificarTablas() {
        try (Connection conn = databaseManager.getConnection();
             Statement stmt = conn.createStatement()) {

            // Intentar consultar las tablas principales
            stmt.executeQuery("SELECT COUNT(*) FROM Producto1").close();
            stmt.executeQuery("SELECT COUNT(*) FROM Equipo").close();
            stmt.executeQuery("SELECT COUNT(*) FROM Calibracion").close();

            return true;

        } catch (Exception e) {
            return false;
        }
    }
}
