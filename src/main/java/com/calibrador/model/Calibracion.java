package com.calibrador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

/**
 * Modelo de Calibración (CORE DEL SISTEMA)
 * Representa los registros de calibración de equipos
 */
public class Calibracion {

    private Integer idCalibracion;
    private Integer idEquipo;
    private LocalDate fechaCalibracion;
    private Integer periodoMeses;
    private LocalDate fechaVencimiento;
    private ResultadoCalibracion resultado;
    private String observaciones;
    private Integer idUsuario;
    private String tecnicoResponsable;
    private String numeroCertificado;
    private String metodoUtilizado;
    private LocalDateTime fechaRegistro;

    // Enum para resultados
    public enum ResultadoCalibracion {
        APROBADO("Aprobado"),
        RECHAZADO("Rechazado"),
        CONDICIONAL("Condicional");

        private final String descripcion;

        ResultadoCalibracion(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    // Constructor vacío
    public Calibracion() {
        this.resultado = ResultadoCalibracion.APROBADO;
        this.periodoMeses = 12; // Por defecto 12 meses
        this.fechaRegistro = LocalDateTime.now();
    }

    // Constructor con campos básicos
    public Calibracion(Integer idEquipo, LocalDate fechaCalibracion, Integer periodoMeses) {
        this();
        this.idEquipo = idEquipo;
        this.fechaCalibracion = fechaCalibracion;
        this.periodoMeses = periodoMeses;
        this.fechaVencimiento = fechaCalibracion.plusMonths(periodoMeses);
    }

    // Getters y Setters
    public Integer getIdCalibracion() {
        return idCalibracion;
    }

    public void setIdCalibracion(Integer idCalibracion) {
        this.idCalibracion = idCalibracion;
    }

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public LocalDate getFechaCalibracion() {
        return fechaCalibracion;
    }

    public void setFechaCalibracion(LocalDate fechaCalibracion) {
        this.fechaCalibracion = fechaCalibracion;
        // Recalcular fecha de vencimiento si hay periodo
        if (this.periodoMeses != null) {
            this.fechaVencimiento = fechaCalibracion.plusMonths(this.periodoMeses);
        }
    }

    public Integer getPeriodoMeses() {
        return periodoMeses;
    }

    public void setPeriodoMeses(Integer periodoMeses) {
        this.periodoMeses = periodoMeses;
        // Recalcular fecha de vencimiento si hay fecha de calibración
        if (this.fechaCalibracion != null) {
            this.fechaVencimiento = this.fechaCalibracion.plusMonths(periodoMeses);
        }
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public ResultadoCalibracion getResultado() {
        return resultado;
    }

    public void setResultado(ResultadoCalibracion resultado) {
        this.resultado = resultado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getTecnicoResponsable() {
        return tecnicoResponsable;
    }

    public void setTecnicoResponsable(String tecnicoResponsable) {
        this.tecnicoResponsable = tecnicoResponsable;
    }

    public String getNumeroCertificado() {
        return numeroCertificado;
    }

    public void setNumeroCertificado(String numeroCertificado) {
        this.numeroCertificado = numeroCertificado;
    }

    public String getMetodoUtilizado() {
        return metodoUtilizado;
    }

    public void setMetodoUtilizado(String metodoUtilizado) {
        this.metodoUtilizado = metodoUtilizado;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // Métodos auxiliares

    /**
     * Calcula los días restantes hasta el vencimiento
     */
    public long getDiasRestantes() {
        if (fechaVencimiento == null) return 0;
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaVencimiento);
    }

    /**
     * Verifica si la calibración está vencida
     */
    public boolean estaVencida() {
        return getDiasRestantes() < 0;
    }

    /**
     * Verifica si está próxima a vencer (30 días)
     */
    public boolean estaProximaAVencer() {
        long dias = getDiasRestantes();
        return dias >= 0 && dias <= 30;
    }

    /**
     * Verifica si está en alerta urgente (7 días)
     */
    public boolean estaEnAlertaUrgente() {
        long dias = getDiasRestantes();
        return dias >= 0 && dias <= 7;
    }

    /**
     * Verifica si el resultado es aprobado
     */
    public boolean esAprobada() {
        return resultado == ResultadoCalibracion.APROBADO;
    }

    @Override
    public String toString() {
        return "Calibracion{" +
                "idCalibracion=" + idCalibracion +
                ", idEquipo=" + idEquipo +
                ", fechaCalibracion=" + fechaCalibracion +
                ", fechaVencimiento=" + fechaVencimiento +
                ", resultado=" + resultado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calibracion that = (Calibracion) o;
        return idCalibracion != null && idCalibracion.equals(that.idCalibracion);
    }

    @Override
    public int hashCode() {
        return idCalibracion != null ? idCalibracion.hashCode() : 0;
    }
}
