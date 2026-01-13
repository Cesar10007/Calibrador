package com.calibrador.model;

import java.time.LocalDateTime;

/**
 * Modelo de Laboratorio
 * Representa los laboratorios donde se encuentran los equipos
 */
public class Laboratorio {

    private Integer idLaboratorio;
    private String nombre;
    private String codigo;
    private Boolean acreditadoIso17025;
    private String numeroAcreditacion;
    private String direccion;
    private String responsable;
    private Boolean activo;
    private LocalDateTime fechaCreacion;

    // Constructor vacío
    public Laboratorio() {
        this.activo = true;
        this.acreditadoIso17025 = false;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Constructor con campos básicos
    public Laboratorio(String nombre, String codigo) {
        this();
        this.nombre = nombre;
        this.codigo = codigo;
    }

    // Getters y Setters
    public Integer getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(Integer idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getAcreditadoIso17025() {
        return acreditadoIso17025;
    }

    public void setAcreditadoIso17025(Boolean acreditadoIso17025) {
        this.acreditadoIso17025 = acreditadoIso17025;
    }

    public String getNumeroAcreditacion() {
        return numeroAcreditacion;
    }

    public void setNumeroAcreditacion(String numeroAcreditacion) {
        this.numeroAcreditacion = numeroAcreditacion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getResponsable() {
        return responsable;
    }

    public void setResponsable(String responsable) {
        this.responsable = responsable;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    // Métodos auxiliares

    /**
     * Verifica si el laboratorio está acreditado ISO 17025
     */
    public boolean estaAcreditado() {
        return acreditadoIso17025 != null && acreditadoIso17025;
    }

    /**
     * Verifica si tiene número de acreditación válido
     */
    public boolean tieneAcreditacionValida() {
        return estaAcreditado() &&
                numeroAcreditacion != null &&
                !numeroAcreditacion.trim().isEmpty();
    }

    @Override
    public String toString() {
        return "Laboratorio{" +
                "idLaboratorio=" + idLaboratorio +
                ", nombre='" + nombre + '\'' +
                ", codigo='" + codigo + '\'' +
                ", acreditadoIso17025=" + acreditadoIso17025 +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Laboratorio that = (Laboratorio) o;
        return idLaboratorio != null && idLaboratorio.equals(that.idLaboratorio);
    }

    @Override
    public int hashCode() {
        return idLaboratorio != null ? idLaboratorio.hashCode() : 0;
    }
}
