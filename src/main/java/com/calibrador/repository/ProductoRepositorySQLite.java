package com.calibrador.repository;

import com.calibrador.infra.DatabaseManager;
import com.calibrador.model.Producto;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación SQLite del repositorio de productos
 * Maneja todas las operaciones SQL de la tabla Producto1
 */
public class ProductoRepositorySQLite implements ProductoRepository {

    private final DatabaseManager databaseManager;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ProductoRepositorySQLite() {
        this.databaseManager = DatabaseManager.getInstance();
    }

    @Override
    public List<Producto> obtenerTodos() throws SQLException {
        String sql = "SELECT * FROM Producto1";
        List<Producto> productos = new ArrayList<>();

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement st = conn.prepareStatement(sql);
             ResultSet rs = st.executeQuery()) {

            while (rs.next()) {
                productos.add(mapearResultSetAProducto(rs));
            }
        }

        return productos;
    }

    @Override
    public Producto obtenerPorId(int id) throws SQLException {
        String sql = "SELECT * FROM Producto1 WHERE ID = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearResultSetAProducto(rs);
                }
            }
        }

        return null;
    }

    @Override
    public int insertar(Producto producto) throws SQLException {
        String sql = "INSERT INTO Producto1 (NOMBRE, FECHACALIBRACION, FECHAEXPIRACION, MESES_VALIDEZ) " +
                "VALUES (?, ?, ?, ?)";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement pst = conn.prepareStatement(sql)) {

            pst.setString(1, producto.getNombre());
            pst.setString(2, producto.getFechaCalibracion().format(DATE_FORMATTER));
            pst.setString(3, producto.getFechaExpiracion().format(DATE_FORMATTER));
            pst.setInt(4, producto.getMesesValidez());

            return pst.executeUpdate();
        }
    }

    @Override
    public int actualizarNombre(int id, String nuevoNombre) throws SQLException {
        String sql = "UPDATE Producto1 SET NOMBRE = ? WHERE ID = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevoNombre);
            ps.setInt(2, id);

            return ps.executeUpdate();
        }
    }

    @Override
    public int actualizarFechaCalibracion(int id, LocalDate nuevaFechaCalibracion, int mesesValidez) throws SQLException {
        // Calcular nueva fecha de expiración
        LocalDate nuevaFechaExpiracion = nuevaFechaCalibracion.plusMonths(mesesValidez);

        String sql = "UPDATE Producto1 SET FECHACALIBRACION = ?, FECHAEXPIRACION = ? WHERE ID = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, nuevaFechaCalibracion.format(DATE_FORMATTER));
            ps.setString(2, nuevaFechaExpiracion.format(DATE_FORMATTER));
            ps.setInt(3, id);

            return ps.executeUpdate();
        }
    }

    @Override
    public int actualizarMesesValidez(int id, int nuevosMeses, LocalDate fechaCalibracion) throws SQLException {
        // Calcular nueva fecha de expiración
        LocalDate nuevaFechaExpiracion = fechaCalibracion.plusMonths(nuevosMeses);

        String sql = "UPDATE Producto1 SET MESES_VALIDEZ = ?, FECHAEXPIRACION = ? WHERE ID = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, nuevosMeses);
            ps.setString(2, nuevaFechaExpiracion.format(DATE_FORMATTER));
            ps.setInt(3, id);

            return ps.executeUpdate();
        }
    }

    @Override
    public int eliminar(int id) throws SQLException {
        String sql = "DELETE FROM Producto1 WHERE ID = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate();
        }
    }

    @Override
    public List<Producto> obtenerProximosAVencer() throws SQLException {
        String sql = "SELECT * FROM Producto1";
        List<Producto> productosProximos = new ArrayList<>();

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                Producto producto = mapearResultSetAProducto(resultSet);

                // Calcular días restantes
                long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), producto.getFechaExpiracion());

                // Verificar si está próximo a vencer (30 días o menos)
                if (diasRestantes <= 30 && diasRestantes >= 0) {
                    productosProximos.add(producto);
                }
            }
        }

        return productosProximos;
    }

    // ==================== MÉTODOS PRIVADOS ====================

    /**
     * Mapea un ResultSet a un objeto Producto
     * @param rs ResultSet con los datos del producto
     * @return objeto Producto mapeado
     * @throws SQLException si hay error al leer los datos
     */
    private Producto mapearResultSetAProducto(ResultSet rs) throws SQLException {
        Producto producto = new Producto();

        producto.setId(rs.getInt("ID"));
        producto.setNombre(rs.getString("NOMBRE"));

        // Parsear fechas desde String a LocalDate
        String fechaCalibracionStr = rs.getString("FECHACALIBRACION");
        if (fechaCalibracionStr != null && !fechaCalibracionStr.isEmpty()) {
            producto.setFechaCalibracion(LocalDate.parse(fechaCalibracionStr, DATE_FORMATTER));
        }

        String fechaExpiracionStr = rs.getString("FECHAEXPIRACION");
        if (fechaExpiracionStr != null && !fechaExpiracionStr.isEmpty()) {
            producto.setFechaExpiracion(LocalDate.parse(fechaExpiracionStr, DATE_FORMATTER));
        }

        producto.setMesesValidez(rs.getInt("MESES_VALIDEZ"));
        producto.setAlertaMostrada(rs.getInt("ALERTA_MOSTRADA"));

        // Campos opcionales (si existen en la tabla)
        try {
            String fechaCreacionStr = rs.getString("FECHA_CREACION");
            if (fechaCreacionStr != null && !fechaCreacionStr.isEmpty()) {
                producto.setFechaCreacion(LocalDate.parse(fechaCreacionStr, DATE_FORMATTER));
            }
            producto.setActivo(rs.getInt("ACTIVO"));
        } catch (SQLException e) {
            // Campos opcionales no presentes en la tabla
        }

        return producto;
    }
}
