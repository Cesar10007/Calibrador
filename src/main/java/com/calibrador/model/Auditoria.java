package com.calibrador.model;

import java.time.LocalDateTime;

/**
 * Modelo de Auditoría
 * Registra todas las acciones realizadas en el sistema para trazabilidad
 */
public class Auditoria {

    private Integer idAuditoria;
    private String tablaAfectada;
    private Integer idRegistro;
    private AccionAuditoria accion;
    private String datosAnteriores; // JSON
    private String datosNuevos; // JSON
    private LocalDateTime fecha;
    private Integer idUsuario;
    private String ipOrigen;

    // Enum para acciones
    public enum AccionAuditoria {
        INSERT("Inserción"),
        UPDATE("Actualización"),
        DELETE("Eliminación");

        private final String descripcion;

        AccionAuditoria(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    // Constructor vacío
    public Auditoria() {
        this.fecha = LocalDateTime.now();
    }

    // Constructor con campos básicos
    public Auditoria(String tablaAfectada, Integer idRegistro, AccionAuditoria accion) {
        this();
        this.tablaAfectada = tablaAfectada;
        this.idRegistro = idRegistro;
        this.accion = accion;
    }

    // Getters y Setters
    public Integer getIdAuditoria() {
        return idAuditoria;
    }

    public void setIdAuditoria(Integer idAuditoria) {
        this.idAuditoria = idAuditoria;
    }

    public String getTablaAfectada() {
        return tablaAfectada;
    }

    public void setTablaAfectada(String tablaAfectada) {
        this.tablaAfectada = tablaAfectada;
    }

    public Integer getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(Integer idRegistro) {
        this.idRegistro = idRegistro;
    }

    public AccionAuditoria getAccion() {
        return accion;
    }

    public void setAccion(AccionAuditoria accion) {
        this.accion = accion;
    }

    public String getDatosAnteriores() {
        return datosAnteriores;
    }

    public void setDatosAnteriores(String datosAnteriores) {
        this.datosAnteriores = datosAnteriores;
    }

    public String getDatosNuevos() {
        return datosNuevos;
    }

    public void setDatosNuevos(String datosNuevos) {
        this.datosNuevos = datosNuevos;
    }

    public LocalDateTime getFecha() {
        return fecha;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getIpOrigen() {
        return ipOrigen;
    }

    public void setIpOrigen(String ipOrigen) {
        this.ipOrigen = ipOrigen;
    }

    // Métodos auxiliares

    /**
     * Verifica si es una inserción
     */
    public boolean esInsercion() {
        return accion == AccionAuditoria.INSERT;
    }

    /**
     * Verifica si es una actualización
     */
    public boolean esActualizacion() {
        return accion == AccionAuditoria.UPDATE;
    }

    /**
     * Verifica si es una eliminación
     */
    public boolean esEliminacion() {
        return accion == AccionAuditoria.DELETE;
    }

    /**
     * Obtiene descripción legible de la acción
     */
    public String getDescripcionCompleta() {
        return String.format("%s en tabla %s (ID: %d)",
                accion.getDescripcion(), tablaAfectada, idRegistro);
    }

    @Override
    public String toString() {
        return "Auditoria{" +
                "idAuditoria=" + idAuditoria +
                ", tablaAfectada='" + tablaAfectada + '\'' +
                ", idRegistro=" + idRegistro +
                ", accion=" + accion +
                ", fecha=" + fecha +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auditoria auditoria = (Auditoria) o;
        return idAuditoria != null && idAuditoria.equals(auditoria.idAuditoria);
    }

    @Override
    public int hashCode() {
        return idAuditoria != null ? idAuditoria.hashCode() : 0;
    }
}
