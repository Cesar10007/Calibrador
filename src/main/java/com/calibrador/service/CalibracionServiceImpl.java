package com.calibrador.service;

import com.calibrador.model.Calibracion;
import com.calibrador.repository.CalibracionRepository;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de calibraciones
 */
public class CalibracionServiceImpl implements CalibracionService {

    private final CalibracionRepository calibracionRepository;

    public CalibracionServiceImpl(CalibracionRepository calibracionRepository) {
        this.calibracionRepository = calibracionRepository;
    }

    @Override
    public List<Calibracion> obtenerTodasLasCalibraciones() {
        try {
            return calibracionRepository.obtenerTodas();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar calibraciones: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    @Override
    public List<Calibracion> obtenerCalibracionesPorEquipo(int equipoId) {
        try {
            return calibracionRepository.obtenerPorEquipo(equipoId);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar calibraciones: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    @Override
    public boolean registrarCalibracion(Calibracion calibracion) {
        try {
            int resultado = calibracionRepository.insertar(calibracion);

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Calibración registrada exitosamente");
                return true;
            }

            return false;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al registrar calibración: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
}
