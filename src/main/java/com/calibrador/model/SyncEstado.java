package com.calibrador.model;

import java.time.LocalDateTime;

/**
 * Modelo de Estado de Sincronización
 * Para sincronización futura con servidor (offline-first)
 */
public class SyncEstado {

    private Integer idSync;
    private String tabla;
    private Integer idRegistro;
    private EstadoSync estadoSync;
    private LocalDateTime fechaModificacion;
    private LocalDateTime fechaSync;
    private Integer intentos;
    private String errorMensaje;

    // Enum para estados de sincronización
    public enum EstadoSync {
        PENDIENTE("Pendiente de sincronización"),
        ENVIADO("Sincronizado exitosamente"),
        ERROR("Error en sincronización");

        private final String descripcion;

        EstadoSync(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    // Constructor vacío
    public SyncEstado() {
        this.estadoSync = EstadoSync.PENDIENTE;
        this.fechaModificacion = LocalDateTime.now();
        this.intentos = 0;
    }

    // Constructor con campos básicos
    public SyncEstado(String tabla, Integer idRegistro) {
        this();
        this.tabla = tabla;
        this.idRegistro = idRegistro;
    }

    // Getters y Setters
    public Integer getIdSync() {
        return idSync;
    }

    public void setIdSync(Integer idSync) {
        this.idSync = idSync;
    }

    public String getTabla() {
        return tabla;
    }

    public void setTabla(String tabla) {
        this.tabla = tabla;
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public EstadoSync getEstadoSync() {
        return estadoSync;
    }

    public void setEstadoSync(EstadoSync estadoSync) {
        this.estadoSync = estadoSync;
    }

    public LocalDateTime getFechaModificacion() {
        return fechaModificacion;
    }

    public void setFechaModificacion(LocalDateTime fechaModificacion) {
        this.fechaModificacion = fechaModificacion;
    }

    public LocalDateTime getFechaSync() {
        return fechaSync;
    }

    public void setFechaSync(LocalDateTime fechaSync) {
        this.fechaSync = fechaSync;
    }

    public Integer getIntentos() {
        return intentos;
    }

    public void setIntentos(Integer intentos) {
        this.intentos = intentos;
    }

    public String getErrorMensaje() {
        return errorMensaje;
    }

    public void setErrorMensaje(String errorMensaje) {
        this.errorMensaje = errorMensaje;
    }

    // Métodos auxiliares

    /**
     * Verifica si está pendiente de sincronizar
     */
    public boolean estaPendiente() {
        return estadoSync == EstadoSync.PENDIENTE;
    }

    /**
     * Verifica si está sincronizado
     */
    public boolean estaSincronizado() {
        return estadoSync == EstadoSync.ENVIADO;
    }

    /**
     * Verifica si tuvo error
     */
    public boolean tieneError() {
        return estadoSync == EstadoSync.ERROR;
    }

    /**
     * Incrementa contador de intentos
     */
    public void incrementarIntentos() {
        this.intentos++;
    }

    /**
     * Marca como sincronizado exitosamente
     */
    public void marcarComoSincronizado() {
        this.estadoSync = EstadoSync.ENVIADO;
        this.fechaSync = LocalDateTime.now();
        this.errorMensaje = null;
    }

    /**
     * Marca como error
     */
    public void marcarComoError(String mensaje) {
        this.estadoSync = EstadoSync.ERROR;
        this.errorMensaje = mensaje;
        incrementarIntentos();
    }

    /**
     * Verifica si ha superado el máximo de intentos
     */
    public boolean hasSuperadoMaximoIntentos() {
        return intentos != null && intentos >= 3;
    }

    @Override
    public String toString() {
        return "SyncEstado{" +
                "idSync=" + idSync +
                ", tabla='" + tabla + '\'' +
                ", idRegistro=" + idRegistro +
                ", estadoSync=" + estadoSync +
                ", intentos=" + intentos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SyncEstado that = (SyncEstado) o;
        return idSync != null && idSync.equals(that.idSync);
    }

    @Override
    public int hashCode() {
        return idSync != null ? idSync.hashCode() : 0;
    }
}
