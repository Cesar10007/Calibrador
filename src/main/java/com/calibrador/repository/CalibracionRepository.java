package com.calibrador.repository;

import com.calibrador.model.Calibracion;
import java.sql.SQLException;
import java.util.List;

/**
 * Interfaz del repositorio de calibraciones
 */
public interface CalibracionRepository {

    /**
     * Obtiene todas las calibraciones
     */
    List<Calibracion> obtenerTodas() throws SQLException;

    /**
     * Obtiene calibraciones por equipo
     */
    List<Calibracion> obtenerPorEquipo(int equipoId) throws SQLException;

    /**
     * Inserta una nueva calibración
     */
    int insertar(Calibracion calibracion) throws SQLException;

    /**
     * Obtiene una calibración por ID
     */
    Calibracion obtenerPorId(int id) throws SQLException;
}
