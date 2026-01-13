package com.calibrador.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Modelo de Equipo (CORE DEL SISTEMA)
 * Representa los equipos que requieren calibración
 */
public class Equipo {

    private Integer idEquipo;
    private String codigoInterno;
    private String nombre;
    private String marca;
    private String modelo;
    private String serie;
    private String ubicacion;
    private EstadoEquipo estado;
    private Integer idLaboratorio;
    private Integer idEmpresa;
    private LocalDate fechaAdquisicion;
    private LocalDateTime fechaRegistro;
    private String observaciones;
    private Boolean activo;

    // Enum para estados
    public enum EstadoEquipo {
        OPERATIVO("Operativo"),
        MANTENIMIENTO("En Mantenimiento"),
        FUERA_SERVICIO("Fuera de Servicio"),
        BAJA("Dado de Baja");

        private final String descripcion;

        EstadoEquipo(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    // Constructor vacío
    public Equipo() {
        this.activo = true;
        this.estado = EstadoEquipo.OPERATIVO;
        this.fechaRegistro = LocalDateTime.now();
    }

    // Constructor con campos básicos
    public Equipo(String codigoInterno, String nombre, String marca, String modelo) {
        this();
        this.codigoInterno = codigoInterno;
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
    }

    // Getters y Setters
    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public String getCodigoInterno() {
        return codigoInterno;
    }

    public void setCodigoInterno(String codigoInterno) {
        this.codigoInterno = codigoInterno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getSerie() {
        return serie;
    }

    public void setSerie(String serie) {
        this.serie = serie;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(String ubicacion) {
        this.ubicacion = ubicacion;
    }

    public EstadoEquipo getEstado() {
        return estado;
    }

    public void setEstado(EstadoEquipo estado) {
        this.estado = estado;
    }

    public Integer getIdLaboratorio() {
        return idLaboratorio;
    }

    public void setIdLaboratorio(Integer idLaboratorio) {
        this.idLaboratorio = idLaboratorio;
    }

    public Integer getIdEmpresa() {
        return idEmpresa;
    }

    public void setIdEmpresa(Integer idEmpresa) {
        this.idEmpresa = idEmpresa;
    }

    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public LocalDateTime getFechaRegistro() {
        return fechaRegistro;
    }

    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Boolean getActivo() {
        return activo;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    // Métodos auxiliares

    /**
     * Verifica si el equipo está operativo
     */
    public boolean estaOperativo() {
        return estado == EstadoEquipo.OPERATIVO && activo;
    }

    /**
     * Verifica si el equipo requiere calibración
     */
    public boolean requiereCalibracion() {
        return estaOperativo();
    }

    /**
     * Obtiene descripción completa del equipo
     */
    public String getDescripcionCompleta() {
        StringBuilder sb = new StringBuilder();
        sb.append(codigoInterno).append(" - ");
        sb.append(nombre);
        if (marca != null) sb.append(" | ").append(marca);
        if (modelo != null) sb.append(" ").append(modelo);
        return sb.toString();
    }

    @Override
    public String toString() {
        return "Equipo{" +
                "idEquipo=" + idEquipo +
                ", codigoInterno='" + codigoInterno + '\'' +
                ", nombre='" + nombre + '\'' +
                ", estado=" + estado +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        return idEquipo != null && idEquipo.equals(equipo.idEquipo);
    }

    @Override
    public int hashCode() {
        return idEquipo != null ? idEquipo.hashCode() : 0;
    }
}
