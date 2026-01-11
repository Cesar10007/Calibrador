package com.calibrador.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad que representa un registro de calibración
 * Corresponde a la tabla Calibracion en la base de datos
 */
public class Calibracion {

    private Integer id;
    private Integer equipoId;
    private Integer productoId;
    private LocalDate fechaCalibracion;
    private LocalDate fechaProxima;
    private String tecnico;
    private String resultado;
    private String observaciones;
    private String certificadoPath;
    private LocalDate fechaRegistro;

    /**
     * Constructor vacío
     */
    public Calibracion() {
    }

    /**
     * Constructor con parámetros principales
     */
    public Calibracion(Integer equipoId, Integer productoId, LocalDate fechaCalibracion,
                       LocalDate fechaProxima, String tecnico) {
        this.equipoId = equipoId;
        this.productoId = productoId;
        this.fechaCalibracion = fechaCalibracion;
        this.fechaProxima = fechaProxima;
        this.tecnico = tecnico;
    }

    // ==================== GETTERS Y SETTERS ====================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEquipoId() {
        return equipoId;
    }

    public void setEquipoId(Integer equipoId) {
        this.equipoId = equipoId;
    }

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    public LocalDate getFechaCalibracion() {
        return fechaCalibracion;
    }

    public void setFechaCalibracion(LocalDate fechaCalibracion) {
        this.fechaCalibracion = fechaCalibracion;
    }

    public LocalDate getFechaProxima() {
        return fechaProxima;
    }

    public void setFechaProxima(LocalDate fechaProxima) {
        this.fechaProxima = fechaProxima;
    }

    public String getTecnico() {
        return tecnico;
    }

    public void setTecnico(String tecnico) {
        this.tecnico = tecnico;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public String getCertificadoPath() {
        return certificadoPath;
    }

    public void setCertificadoPath(String certificadoPath) {
        this.certificadoPath = certificadoPath;
    }

    public LocalDate getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDate fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // ==================== MÉTODOS OBJECT ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Calibracion that = (Calibracion) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Calibracion{" +
                "id=" + id +
                ", equipoId=" + equipoId +
                ", productoId=" + productoId +
                ", fechaCalibracion=" + fechaCalibracion +
                ", fechaProxima=" + fechaProxima +
                ", tecnico='" + tecnico + '\'' +
                ", resultado='" + resultado + '\'' +
                '}';
    }
}
