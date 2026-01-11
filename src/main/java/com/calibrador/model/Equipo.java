package com.calibrador.model;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Entidad que representa un equipo de laboratorio
 * Corresponde a la tabla Equipo en la base de datos
 */

/**
 * ⚠️ IMPORTANTE: Este modelo NO existe en tu código original Calibrador.java, pero lo vi en la estructura de tablas de DatabaseInitializer.
 * Como me pediste seguir los mismos requisitos (no inventar código), aquí están las opciones:
 * Opción A: NO crearlo (porque no está en tu código original)
 * Opción B: Crearlo como POJO básico (para que coincida con la tabla Equipo)
 * Te muestro la Opción B por si lo necesitas más adelante:
 */
public class Equipo {

    private Integer id;
    private String codigo;
    private String nombre;
    private String marca;
    private String modelo;
    private String serie;
    private String ubicacion;
    private String estado;
    private LocalDate fechaAdquisicion;
    private LocalDate fechaCreacion;
    private Integer activo;

    /**
     * Constructor vacío
     */
    public Equipo() {
        this.estado = "OPERATIVO";
        this.activo = 1;
    }

    /**
     * Constructor con parámetros principales
     */
    public Equipo(String codigo, String nombre, String marca, String modelo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.marca = marca;
        this.modelo = modelo;
        this.estado = "OPERATIVO";
        this.activo = 1;
    }

    // ==================== GETTERS Y SETTERS ====================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
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

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    public void setFechaAdquisicion(LocalDate fechaAdquisicion) {
        this.fechaAdquisicion = fechaAdquisicion;
    }

    public LocalDate getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(LocalDate fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public Integer getActivo() {
        return activo;
    }

    public void setActivo(Integer activo) {
        this.activo = activo;
    }

    // ==================== MÉTODOS OBJECT ====================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Equipo equipo = (Equipo) o;
        return Objects.equals(id, equipo.id) || Objects.equals(codigo, equipo.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, codigo);
    }

    @Override
    public String toString() {
        return "Equipo{" +
                "id=" + id +
                ", codigo='" + codigo + '\'' +
                ", nombre='" + nombre + '\'' +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", estado='" + estado + '\'' +
                '}';
    }
}

