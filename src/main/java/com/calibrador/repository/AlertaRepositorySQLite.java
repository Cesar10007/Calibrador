package com.calibrador.repository;

import com.calibrador.infra.DatabaseManager;
import com.calibrador.model.Alerta;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación SQLite del repositorio de alertas.
 * Maneja todas las operaciones SQL de la tabla alerta.
 */
public class AlertaRepositorySQLite implements AlertaRepository {

    private final DatabaseManager databaseManager;
    private static final DateTimeFormatter DATE_FORMATTER     = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public AlertaRepositorySQLite() {
        this.databaseManager = DatabaseManager.getInstance();
    }

    @Override
    public int insertar(Alerta alerta) throws SQLException {
        String sql = "INSERT INTO alerta " +
                "(id_equipo, tipo, titulo, mensaje, fecha_vencimiento) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, alerta.getIdEquipo());
            ps.setString(2, alerta.getTipo().name());
            ps.setString(3, alerta.getTitulo());
            ps.setString(4, alerta.getMensaje());
            ps.setString(5, alerta.getFechaVencimiento() != null
                    ? alerta.getFechaVencimiento().format(DATE_FORMATTER)
                    : null);

            return ps.executeUpdate();
        }
    }

    @Override
    public List<Alerta> obtenerPendientes() throws SQLException {
        String sql = "SELECT * FROM alerta WHERE atendida = 0 ORDER BY fecha_alerta DESC";
        return ejecutarConsulta(sql);
    }

    @Override
    public List<Alerta> obtenerPorEquipo(int idEquipo) throws SQLException {
        String sql = "SELECT * FROM alerta WHERE id_equipo = ? ORDER BY fecha_alerta DESC";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEquipo);

            try (ResultSet rs = ps.executeQuery()) {
                return mapearLista(rs);
            }
        }
    }

    @Override
    public List<Alerta> obtenerPorTipo(Alerta.TipoAlerta tipo) throws SQLException {
        String sql = "SELECT * FROM alerta WHERE tipo = ? AND atendida = 0 ORDER BY fecha_alerta DESC";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, tipo.name());

            try (ResultSet rs = ps.executeQuery()) {
                return mapearLista(rs);
            }
        }
    }

    @Override
    public int marcarComoAtendida(int idAlerta, int idUsuario) throws SQLException {
        String sql = "UPDATE alerta SET atendida = 1, fecha_atencion = ?, id_usuario_atencion = ? " +
                "WHERE id_alerta = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, LocalDateTime.now().format(DATETIME_FORMATTER));
            ps.setInt(2, idUsuario);
            ps.setInt(3, idAlerta);

            return ps.executeUpdate();
        }
    }

    @Override
    public boolean existeAlertaPendiente(int idEquipo, Alerta.TipoAlerta tipo) throws SQLException {
        String sql = "SELECT COUNT(*) FROM alerta WHERE id_equipo = ? AND tipo = ? AND atendida = 0";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idEquipo);
            ps.setString(2, tipo.name());

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    @Override
    public Alerta obtenerPorId(int idAlerta) throws SQLException {
        String sql = "SELECT * FROM alerta WHERE id_alerta = ?";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, idAlerta);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearAlerta(rs);
                }
            }
        }
        return null;
    }

    @Override
    public int eliminarAtendidas(int diasAntiguedad) throws SQLException {
        String sql = "DELETE FROM alerta WHERE atendida = 1 " +
                "AND fecha_atencion < date('now', '-' || ? || ' days')";

        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, diasAntiguedad);
            return ps.executeUpdate();
        }
    }

    // ==================== MÉTODOS PRIVADOS ====================

    /**
     * Ejecuta una consulta SQL sin parámetros y devuelve lista de alertas
     */
    private List<Alerta> ejecutarConsulta(String sql) throws SQLException {
        try (Connection conn = databaseManager.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            return mapearLista(rs);
        }
    }

    /**
     * Convierte un ResultSet completo en lista de alertas
     */
    private List<Alerta> mapearLista(ResultSet rs) throws SQLException {
        List<Alerta> alertas = new ArrayList<>();
        while (rs.next()) {
            alertas.add(mapearAlerta(rs));
        }
        return alertas;
    }

    /**
     * Convierte una fila del ResultSet en un objeto Alerta
     */
    private Alerta mapearAlerta(ResultSet rs) throws SQLException {
        Alerta alerta = new Alerta();

        alerta.setIdAlerta(rs.getInt("id_alerta"));
        alerta.setIdEquipo(rs.getInt("id_equipo"));
        alerta.setTipo(Alerta.TipoAlerta.valueOf(rs.getString("tipo")));
        alerta.setTitulo(rs.getString("titulo"));
        alerta.setMensaje(rs.getString("mensaje"));
        alerta.setAtendida(rs.getInt("atendida") == 1);

        // Parsear fecha_alerta
        String fechaAlertaStr = rs.getString("fecha_alerta");
        if (fechaAlertaStr != null && !fechaAlertaStr.isEmpty()) {
            alerta.setFechaAlerta(LocalDateTime.parse(
                    fechaAlertaStr.replace(" ", "T").substring(0, 19),
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        // Parsear fecha_vencimiento
        String fechaVencStr = rs.getString("fecha_vencimiento");
        if (fechaVencStr != null && !fechaVencStr.isEmpty()) {
            alerta.setFechaVencimiento(LocalDate.parse(fechaVencStr, DATE_FORMATTER));
        }

        // Parsear fecha_atencion
        String fechaAtencionStr = rs.getString("fecha_atencion");
        if (fechaAtencionStr != null && !fechaAtencionStr.isEmpty()) {
            alerta.setFechaAtencion(LocalDateTime.parse(
                    fechaAtencionStr.replace(" ", "T").substring(0, 19),
                    DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        }

        // id_usuario_atencion puede ser null
        int idUsuario = rs.getInt("id_usuario_atencion");
        if (!rs.wasNull()) {
            alerta.setIdUsuarioAtencion(idUsuario);
        }

        return alerta;
    }
}