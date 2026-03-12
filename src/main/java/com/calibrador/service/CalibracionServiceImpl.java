package com.calibrador.service;

import com.calibrador.model.Calibracion;
import com.calibrador.repository.CalibracionRepository;
import com.calibrador.util.AppException;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementación del servicio de calibraciones.
 * Solo contiene lógica de negocio — no sabe nada de UI.
 * Cuando algo sale mal, lanza AppException para que
 * quien llame (el endpoint de Javalin) decida cómo mostrarlo.
 */
public class CalibracionServiceImpl implements CalibracionService {

    private final CalibracionRepository calibracionRepository;

    public CalibracionServiceImpl(CalibracionRepository calibracionRepository) {
        this.calibracionRepository = calibracionRepository;
    }

    @Override
    public List<Calibracion> obtenerTodasLasCalibraciones() throws AppException {
        try {
            return calibracionRepository.obtenerTodas();
        } catch (SQLException e) {
            throw new AppException("Error al cargar las calibraciones", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public List<Calibracion> obtenerCalibracionesPorEquipo(int equipoId) throws AppException {
        if (equipoId <= 0) {
            throw new AppException("El ID del equipo debe ser positivo", "VALIDATION_ERROR", null);
        }
        try {
            return calibracionRepository.obtenerPorEquipo(equipoId);
        } catch (SQLException e) {
            throw new AppException("Error al cargar calibraciones del equipo", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public boolean registrarCalibracion(Calibracion calibracion) throws AppException {
        // Validar que la calibración tenga los datos mínimos
        validarCalibracion(calibracion);

        try {
            int resultado = calibracionRepository.insertar(calibracion);
            return resultado > 0;
        } catch (SQLException e) {
            throw new AppException("Error al registrar la calibración", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public Calibracion obtenerCalibracionPorId(int id) throws AppException {
        if (id <= 0) {
            throw new AppException("El ID debe ser positivo", "VALIDATION_ERROR", null);
        }
        try {
            Calibracion calibracion = calibracionRepository.obtenerPorId(id);
            if (calibracion == null) {
                throw new AppException("Calibración no encontrada con ID: " + id, "NOT_FOUND", null);
            }
            return calibracion;
        } catch (SQLException e) {
            throw new AppException("Error al obtener la calibración", "DB_ERROR", e.getMessage(), e);
        }
    }

    //MÉTODOS PRIVADOS
    /**
     * Valida que una calibración tenga los datos mínimos requeridos.
     * Lanza AppException si algo falta — el endpoint decide cómo mostrarlo.
     */
    private void validarCalibracion(Calibracion calibracion) throws AppException {
        if (calibracion == null) {
            throw new AppException("La calibración no puede ser nula", "VALIDATION_ERROR", null);
        }
        if (calibracion.getIdEquipo() == null || calibracion.getIdEquipo() <= 0) {
            throw new AppException("Debe especificar un equipo válido", "VALIDATION_ERROR", null);
        }
        if (calibracion.getFechaCalibracion() == null) {
            throw new AppException("La fecha de calibración es obligatoria", "VALIDATION_ERROR", null);
        }
        if (calibracion.getPeriodoMeses() == null || calibracion.getPeriodoMeses() <= 0) {
            throw new AppException("El período de validez debe ser un número positivo", "VALIDATION_ERROR", null);
        }
    }
}