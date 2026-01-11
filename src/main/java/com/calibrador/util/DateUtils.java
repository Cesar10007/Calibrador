package com.calibrador.util;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Clase utilitaria para operaciones con fechas
 * Proporciona métodos comunes para manipulación de fechas
 */
public class DateUtils {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

    /**
     * Convierte una fecha LocalDate a String
     * @param fecha fecha a convertir
     * @return String en formato yyyy-MM-dd
     */
    public static String formatearFecha(LocalDate fecha) {
        if (fecha == null) {
            return "";
        }
        return fecha.format(DATE_FORMATTER);
    }

    /**
     * Convierte un String a LocalDate
     * @param fechaStr fecha en formato String
     * @return LocalDate o null si la conversión falla
     */
    public static LocalDate parsearFecha(String fechaStr) {
        try {
            return LocalDate.parse(fechaStr, DATE_FORMATTER);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Calcula los días entre dos fechas
     * @param fechaInicio fecha inicial
     * @param fechaFin fecha final
     * @return número de días entre las fechas
     */
    public static long calcularDiasEntre(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(fechaInicio, fechaFin);
    }

    /**
     * Calcula los días desde una fecha hasta hoy
     * @param fecha fecha de referencia
     * @return número de días desde la fecha hasta hoy
     */
    public static long calcularDiasDesdeHoy(LocalDate fecha) {
        return calcularDiasEntre(LocalDate.now(), fecha);
    }

    /**
     * Calcula los días desde hoy hasta una fecha
     * @param fecha fecha de referencia
     * @return número de días desde hoy hasta la fecha
     */
    public static long calcularDiasHastaHoy(LocalDate fecha) {
        return calcularDiasEntre(fecha, LocalDate.now());
    }

    /**
     * Calcula el periodo entre dos fechas
     * @param fechaInicio fecha inicial
     * @param fechaFin fecha final
     * @return Period con años, meses y días
     */
    public static Period calcularPeriodo(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio == null || fechaFin == null) {
            return Period.ZERO;
        }
        return Period.between(fechaInicio, fechaFin);
    }

    /**
     * Suma meses a una fecha
     * @param fecha fecha base
     * @param meses número de meses a sumar
     * @return nueva fecha con los meses sumados
     */
    public static LocalDate sumarMeses(LocalDate fecha, int meses) {
        if (fecha == null) {
            return null;
        }
        return fecha.plusMonths(meses);
    }

    /**
     * Resta meses a una fecha
     * @param fecha fecha base
     * @param meses número de meses a restar
     * @return nueva fecha con los meses restados
     */
    public static LocalDate restarMeses(LocalDate fecha, int meses) {
        if (fecha == null) {
            return null;
        }
        return fecha.minusMonths(meses);
    }

    /**
     * Verifica si una fecha está en el futuro
     * @param fecha fecha a verificar
     * @return true si la fecha es futura
     */
    public static boolean esFechaFutura(LocalDate fecha) {
        if (fecha == null) {
            return false;
        }
        return fecha.isAfter(LocalDate.now());
    }

    /**
     * Verifica si una fecha está en el pasado
     * @param fecha fecha a verificar
     * @return true si la fecha es pasada
     */
    public static boolean esFechaPasada(LocalDate fecha) {
        if (fecha == null) {
            return false;
        }
        return fecha.isBefore(LocalDate.now());
    }

    /**
     * Verifica si una fecha es hoy
     * @param fecha fecha a verificar
     * @return true si la fecha es hoy
     */
    public static boolean esFechaHoy(LocalDate fecha) {
        if (fecha == null) {
            return false;
        }
        return fecha.isEqual(LocalDate.now());
    }

    /**
     * Obtiene la fecha actual
     * @return LocalDate de hoy
     */
    public static LocalDate obtenerFechaActual() {
        return LocalDate.now();
    }

    /**
     * Formatea un periodo en texto legible
     * @param periodo periodo a formatear
     * @return String descriptivo del periodo
     */
    public static String formatearPeriodo(Period periodo) {
        if (periodo == null) {
            return "Sin periodo";
        }

        StringBuilder sb = new StringBuilder();

        if (periodo.getYears() > 0) {
            sb.append(periodo.getYears()).append(" año(s) ");
        }
        if (periodo.getMonths() > 0) {
            sb.append(periodo.getMonths()).append(" mes(es) ");
        }
        if (periodo.getDays() > 0) {
            sb.append(periodo.getDays()).append(" día(s)");
        }

        return sb.toString().trim();
    }

    // Constructor privado para evitar instanciación
    private DateUtils() {
        throw new AssertionError("No se puede instanciar la clase DateUtils");
    }
}
