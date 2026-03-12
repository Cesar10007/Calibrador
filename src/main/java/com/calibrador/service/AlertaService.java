package com.calibrador.service;

import com.calibrador.model.Alerta;
import com.calibrador.util.AppException;
import java.util.List;

/**
 * Contrato del servicio de alertas.
 * Define QUÉ lógica de negocio existe para alertas.
 *
 * Separamos interfaz de implementación para que el día que
 * cambies SQLite por PostgreSQL no toques este contrato.
 */
public interface AlertaService {

    /**
     * Revisa todas las calibraciones activas y genera alertas
     * para las que están próximas a vencer o ya vencidas.
     *
     * Llamado al arrancar la app y por el scheduler cada 24h.
     *
     * @return número de alertas nuevas generadas
     */
    int verificarVencimientos() throws AppException;

    /**
     * Devuelve todas las alertas que aún no han sido atendidas.
     * El frontend las muestra en el panel de notificaciones.
     */
    List<Alerta> obtenerPendientes() throws AppException;

    /**
     * Devuelve las alertas pendientes de un equipo específico.
     */
    List<Alerta> obtenerPendientesPorEquipo(int idEquipo) throws AppException;

    /**
     * Marca una alerta como atendida (el usuario hizo clic en "Atender").
     *
     * @param idAlerta  ID de la alerta a atender
     * @param idUsuario ID del usuario que la atiende
     */
    boolean atenderAlerta(int idAlerta, int idUsuario) throws AppException;

    /**
     * Cuenta cuántas alertas pendientes hay.
     * Útil para el badge rojo en la UI (ej: 🔔 3).
     */
    int contarPendientes() throws AppException;

    /**
     * Limpia alertas atendidas más antiguas de 90 días.
     * Evita que la BD crezca indefinidamente.
     */
    int limpiarAtendidas() throws AppException;
}