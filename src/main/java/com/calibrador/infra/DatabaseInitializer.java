package com.calibrador.infra;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Inicializa la estructura de la base de datos.
 * Crea las tablas si no existen.
 *
 * Ya no muestra JOptionPane — si algo falla lanza
 * una RuntimeException para que Main.java la maneje.
 */
public class DatabaseInitializer {

    private final DatabaseManager databaseManager;

    public DatabaseInitializer() {
        this.databaseManager = DatabaseManager.getInstance();
    }

    /**
     * Crea todas las tablas necesarias si no existen.
     * Lanza RuntimeException si falla — la app no puede funcionar sin BD.
     */
    public void inicializar() {
        try {
            System.out.println("Iniciando creación de tablas...");

            crearTablaProducto();
            crearTablaEquipo();
            crearTablaCalibracion();

            System.out.println("✓ Base de datos inicializada correctamente");

        } catch (SQLException e) {
            // Relanzar como excepción no verificada para que Main.java la capture
            throw new RuntimeException("No se pudo inicializar la base de datos: " + e.getMessage(), e);
        }
    }

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
        ejecutarSQL(sql, "Tabla Producto1 lista");
    }

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
        ejecutarSQL(sql, "Tabla Equipo lista");
    }

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
        ejecutarSQL(sql, "Tabla Calibracion lista");
    }

    private void ejecutarSQL(String sql, String mensajeExito) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("  ✓ " + mensajeExito);
        }
    }

    public boolean verificarTablas() {
        try (Connection conn = databaseManager.getConnection();
             Statement stmt = conn.createStatement()) {
            stmt.executeQuery("SELECT COUNT(*) FROM Producto1").close();
            stmt.executeQuery("SELECT COUNT(*) FROM Equipo").close();
            stmt.executeQuery("SELECT COUNT(*) FROM Calibracion").close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}