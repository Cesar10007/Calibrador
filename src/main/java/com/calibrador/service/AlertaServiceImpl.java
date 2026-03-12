package com.calibrador.service;

import com.calibrador.model.Alerta;
import com.calibrador.model.Calibracion;
import com.calibrador.repository.AlertaRepository;
import com.calibrador.repository.AlertaRepositorySQLite;
import com.calibrador.repository.CalibracionRepository;
import com.calibrador.repository.CalibracionRepositorySQLite;
import com.calibrador.util.AppException;
import com.calibrador.util.Constants;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Implementación del servicio de alertas.
 *
 * Lógica principal:
 *   1. Revisa todas las calibraciones activas en la BD
 *   2. Calcula cuántos días faltan para cada vencimiento
 *   3. Si cae dentro del umbral de alerta, crea una Alerta en la BD
 *   4. No crea alertas duplicadas (verifica si ya existe una pendiente)
 *
 * Esta clase NO sabe nada de UI — solo lanza AppException si algo falla.
 * El controlador o endpoint decide cómo mostrar el error.
 */
public class AlertaServiceImpl implements AlertaService {

    private final AlertaRepository alertaRepository;
    private final CalibracionRepository calibracionRepository;

    // Umbrales de alerta (vienen de Constants para que sea fácil cambiarlos)
    private static final int DIAS_ALERTA_NORMAL  = Constants.DIAS_ALERTA_EXPIRACION; // 30 días
    private static final int DIAS_ALERTA_URGENTE = Constants.DIAS_ALERTA_URGENTE;    // 7 días
    private static final int DIAS_LIMPIEZA       = 90; // alertas atendidas de más de 90 días

    public AlertaServiceImpl() {
        this.alertaRepository      = new AlertaRepositorySQLite();
        this.calibracionRepository = new CalibracionRepositorySQLite();
    }

    // Constructor para inyección de dependencias (testing)
    public AlertaServiceImpl(AlertaRepository alertaRepository,
                             CalibracionRepository calibracionRepository) {
        this.alertaRepository      = alertaRepository;
        this.calibracionRepository = calibracionRepository;
    }

    // ==================== VERIFICACIÓN DE VENCIMIENTOS ====================

    @Override
    public int verificarVencimientos() throws AppException {
        try {
            List<Calibracion> calibraciones = calibracionRepository.obtenerTodas();
            int alertasGeneradas = 0;

            for (Calibracion calibracion : calibraciones) {
                int nuevas = procesarCalibracion(calibracion);
                alertasGeneradas += nuevas;
            }

            System.out.println("✓ Verificación completada — " + alertasGeneradas + " alerta(s) nueva(s)");
            return alertasGeneradas;

        } catch (SQLException e) {
            throw new AppException("Error al verificar vencimientos", "DB_ERROR", e.getMessage(), e);
        }
    }

    /**
     * Analiza una calibración y crea la alerta correspondiente si aplica.
     *
     * Reglas:
     *   - Ya vencida (días < 0)     → CRITICO
     *   - ≤ 7 días para vencer      → CRITICO
     *   - ≤ 30 días para vencer     → VENCIMIENTO
     *   - > 30 días                 → no hacer nada
     *
     * En todos los casos primero verifica que no exista ya una alerta
     * pendiente del mismo tipo para ese equipo.
     */
    private int procesarCalibracion(Calibracion calibracion) throws SQLException {
        LocalDate hoy             = LocalDate.now();
        LocalDate fechaVencimiento = calibracion.getFechaVencimiento();

        if (fechaVencimiento == null) return 0;

        long diasRestantes = java.time.temporal.ChronoUnit.DAYS.between(hoy, fechaVencimiento);

        // Determinar qué tipo de alerta corresponde
        Alerta.TipoAlerta tipoAlerta;
        String titulo;
        String mensaje;

        if (diasRestantes < 0) {
            // Ya venció
            tipoAlerta = Alerta.TipoAlerta.CRITICO;
            titulo     = "Calibración vencida";
            mensaje    = String.format(
                    "El equipo ID %d tiene su calibración vencida desde hace %d día(s). " +
                            "Vencimiento era: %s",
                    calibracion.getIdEquipo(),
                    Math.abs(diasRestantes),
                    fechaVencimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            );

        } else if (diasRestantes <= DIAS_ALERTA_URGENTE) {
            // Urgente: ≤ 7 días
            tipoAlerta = Alerta.TipoAlerta.CRITICO;
            titulo     = "Calibración vence en " + diasRestantes + " día(s)";
            mensaje    = String.format(
                    "URGENTE: El equipo ID %d vence el %s (%d día(s) restantes).",
                    calibracion.getIdEquipo(),
                    fechaVencimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    diasRestantes
            );

        } else if (diasRestantes <= DIAS_ALERTA_NORMAL) {
            // Próximo: ≤ 30 días
            tipoAlerta = Alerta.TipoAlerta.VENCIMIENTO;
            titulo     = "Calibración próxima a vencer";
            mensaje    = String.format(
                    "El equipo ID %d vence el %s (%d día(s) restantes).",
                    calibracion.getIdEquipo(),
                    fechaVencimiento.format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    diasRestantes
            );

        } else {
            // Más de 30 días — no hacer nada
            return 0;
        }

        // Verificar si ya existe alerta pendiente del mismo tipo para ese equipo
        // (evita duplicar alertas si el scheduler corre varias veces)
        boolean yaExiste = alertaRepository.existeAlertaPendiente(
                calibracion.getIdEquipo(), tipoAlerta);

        if (yaExiste) return 0;

        // Crear y guardar la alerta
        Alerta nuevaAlerta = new Alerta(
                calibracion.getIdEquipo(),
                tipoAlerta,
                titulo,
                mensaje
        );
        nuevaAlerta.setFechaVencimiento(fechaVencimiento);

        alertaRepository.insertar(nuevaAlerta);
        return 1;
    }

    // ==================== CONSULTAS ====================

    @Override
    public List<Alerta> obtenerPendientes() throws AppException {
        try {
            return alertaRepository.obtenerPendientes();
        } catch (SQLException e) {
            throw new AppException("Error al obtener alertas pendientes", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public List<Alerta> obtenerPendientesPorEquipo(int idEquipo) throws AppException {
        try {
            return alertaRepository.obtenerPorEquipo(idEquipo);
        } catch (SQLException e) {
            throw new AppException("Error al obtener alertas del equipo " + idEquipo,
                    "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public int contarPendientes() throws AppException {
        try {
            return alertaRepository.obtenerPendientes().size();
        } catch (SQLException e) {
            throw new AppException("Error al contar alertas pendientes", "DB_ERROR", e.getMessage(), e);
        }
    }

    // ==================== ACCIONES ====================

    @Override
    public boolean atenderAlerta(int idAlerta, int idUsuario) throws AppException {
        try {
            // Verificar que la alerta existe antes de atenderla
            Alerta alerta = alertaRepository.obtenerPorId(idAlerta);
            if (alerta == null) {
                throw new AppException(
                        "Alerta no encontrada con ID: " + idAlerta, "NOT_FOUND", null);
            }
            if (!alerta.estaPendiente()) {
                throw new AppException(
                        "La alerta ID " + idAlerta + " ya fue atendida", "ALREADY_DONE", null);
            }

            int resultado = alertaRepository.marcarComoAtendida(idAlerta, idUsuario);
            return resultado > 0;

        } catch (SQLException e) {
            throw new AppException("Error al atender la alerta", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public int limpiarAtendidas() throws AppException {
        try {
            int eliminadas = alertaRepository.eliminarAtendidas(DIAS_LIMPIEZA);
            System.out.println("✓ Limpieza: " + eliminadas + " alerta(s) antigua(s) eliminada(s)");
            return eliminadas;
        } catch (SQLException e) {
            throw new AppException("Error al limpiar alertas antiguas", "DB_ERROR", e.getMessage(), e);
        }
    }
}