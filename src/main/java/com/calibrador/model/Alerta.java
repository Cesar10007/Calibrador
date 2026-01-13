package com.calibrador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo de Alerta
 * Representa notificaciones de vencimientos y eventos importantes
 */
public class Alerta {

    private Integer idAlerta;
    private Integer idEquipo;
    private TipoAlerta tipo;
    private String titulo;
    private String mensaje;
    private LocalDateTime fechaAlerta;
    private LocalDate fechaVencimiento;
    private Boolean atendida;
    private LocalDateTime fechaAtencion;
    private Integer idUsuarioAtencion;

    // Enum para tipos de alerta
    public enum TipoAlerta {
        VENCIMIENTO("Vencimiento de Calibración"),
        MANTENIMIENTO("Mantenimiento Requerido"),
        CRITICO("Alerta Crítica");

        private final String descripcion;

        TipoAlerta(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    // Constructor vacío
    public Alerta() {
        this.atendida = false;
        this.fechaAlerta = LocalDateTime.now();
    }

    // Constructor con campos básicos
    public Alerta(Integer idEquipo, TipoAlerta tipo, String titulo, String mensaje) {
        this();
        this.idEquipo = idEquipo;
        this.tipo = tipo;
        this.titulo = titulo;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public Integer getIdAlerta() {
        return idAlerta;
    }

    public void setIdAlerta(Integer idAlerta) {
        this.idAlerta = idAlerta;
    }

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public TipoAlerta getTipo() {
        return tipo;
    }

    public void setTipo(TipoAlerta tipo) {
        this.tipo = tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public LocalDateTime getFechaAlerta() {
        return fechaAlerta;
    }

    public void setFechaAlerta(LocalDateTime fechaAlerta) {
        this.fechaAlerta = fechaAlerta;
    }

    public LocalDate getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(LocalDate fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public Boolean getAtendida() {
        return atendida;
    }

    public void setAtendida(Boolean atendida) {
        this.atendida = atendida;
    }

    public LocalDateTime getFechaAtencion() {
        return fechaAtencion;
    }

    public void setFechaAtencion(LocalDateTime fechaAtencion) {
        this.fechaAtencion = fechaAtencion;
    }

    public Integer getIdUsuarioAtencion() {
        return idUsuarioAtencion;
    }

    public void setIdUsuarioAtencion(Integer idUsuarioAtencion) {
        this.idUsuarioAtencion = idUsuarioAtencion;
    }

    // Métodos auxiliares

    /**
     * Verifica si la alerta está pendiente
     */
    public boolean estaPendiente() {
        return atendida == null || !atendida;
    }

    /**
     * Marca la alerta como atendida
     */
    public void marcarComoAtendida(Integer idUsuario) {
        this.atendida = true;
        this.fechaAtencion = LocalDateTime.now();
        this.idUsuarioAtencion = idUsuario;
    }

    /**
     * Verifica si es una alerta crítica
     */
    public boolean esCritica() {
        return tipo == TipoAlerta.CRITICO;
    }

    /**
     * Verifica si es una alerta de vencimiento
     */
    public boolean esVencimiento() {
        return tipo == TipoAlerta.VENCIMIENTO;
    }

    /**
     * Verifica si la alerta está vencida
     */
    public boolean estaVencida() {
        if (fechaVencimiento == null) return false;
        return LocalDate.now().isAfter(fechaVencimiento);
    }

    @Override
    public String toString() {
        return "Alerta{" +
                "idAlerta=" + idAlerta +
                ", tipo=" + tipo +
                ", titulo='" + titulo + '\'' +
                ", atendida=" + atendida +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Alerta alerta = (Alerta) o;
        return idAlerta != null && idAlerta.equals(alerta.idAlerta);
    }

    @Override
    public int hashCode() {
        return idAlerta != null ? idAlerta.hashCode() : 0;
    }
}
