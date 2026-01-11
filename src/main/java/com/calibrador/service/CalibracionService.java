package com.calibrador.service;

import com.calibrador.model.Calibracion;
import java.util.List;

/**
 * Interfaz del servicio de calibraciones
 */
public interface CalibracionService {

    /**
     * Obtiene todas las calibraciones
     */
    List<Calibracion> obtenerTodasLasCalibraciones();

    /**
     * Obtiene calibraciones por equipo
     */
    List<Calibracion> obtenerCalibracionesPorEquipo(int equipoId);

    /**
     * Registra una nueva calibración
     */
    boolean registrarCalibracion(Calibracion calibracion);
}
