package com.calibrador.repository;

import com.calibrador.model.Alerta;
import java.sql.SQLException;
import java.util.List;

/**
 * Contrato del repositorio de alertas.
 * Define QUÉ operaciones de BD existen, no CÓMO se hacen.
 */
public interface AlertaRepository {

    /**
     * Inserta una nueva alerta en la BD
     */
    int insertar(Alerta alerta) throws SQLException;

    /**
     * Obtiene todas las alertas sin atender
     */
    List<Alerta> obtenerPendientes() throws SQLException;

    /**
     * Obtiene todas las alertas de un equipo específico
     */
    List<Alerta> obtenerPorEquipo(int idEquipo) throws SQLException;

    /**
     * Obtiene alertas por tipo (VENCIMIENTO, MANTENIMIENTO, CRITICO)
     */
    List<Alerta> obtenerPorTipo(Alerta.TipoAlerta tipo) throws SQLException;

    /**
     * Marca una alerta como atendida
     */
    int marcarComoAtendida(int idAlerta, int idUsuario) throws SQLException;

    /**
     * Verifica si ya existe una alerta pendiente para un equipo y tipo
     * Evita crear alertas duplicadas
     */
    boolean existeAlertaPendiente(int idEquipo, Alerta.TipoAlerta tipo) throws SQLException;

    /**
     * Obtiene una alerta por su ID
     */
    Alerta obtenerPorId(int idAlerta) throws SQLException;

    /**
     * Elimina alertas atendidas más antiguas de X días (limpieza)
     */
    int eliminarAtendidas(int diasAntiguedad) throws SQLException;
}