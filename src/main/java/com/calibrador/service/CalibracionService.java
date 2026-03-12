package com.calibrador.service;

import com.calibrador.model.Calibracion;
import com.calibrador.util.AppException;
import java.util.List;

/**
 * Contrato del servicio de calibraciones.
 * Define QUÉ se puede hacer, no CÓMO.
 */
public interface CalibracionService {

    /**
     * Obtiene todas las calibraciones registradas
     */
    List<Calibracion> obtenerTodasLasCalibraciones() throws AppException;

    /**
     * Obtiene el historial de calibraciones de un equipo específico
     */
    List<Calibracion> obtenerCalibracionesPorEquipo(int equipoId) throws AppException;

    /**
     * Registra una nueva calibración
     */
    boolean registrarCalibracion(Calibracion calibracion) throws AppException;

    /**
     * Obtiene una calibración por su ID
     */
    Calibracion obtenerCalibracionPorId(int id) throws AppException;
}