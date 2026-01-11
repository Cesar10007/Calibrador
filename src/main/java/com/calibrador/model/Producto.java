package com.calibrador.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Entidad que representa un producto de calibración
 * Corresponde a la tabla Producto1 en la base de datos
 */
public class Producto {

    private Integer id;
    private String nombre;
    private LocalDate fechaCalibracion;
    private LocalDate fechaExpiracion;
    private Integer mesesValidez;
    private Integer alertaMostrada;
    private LocalDate fechaCreacion;
    private Integer activo;

    /**
     * Constructor vacío
     */
    public Producto() {
        this.alertaMostrada = 0;
        this.activo = 1;
    }

    /**
     * Constructor con parámetros principales
     */
    public Producto(String nombre, LocalDate fechaCalibracion, LocalDate fechaExpiracion, Integer mesesValidez) {
        this.nombre = nombre;
        this.fechaCalibracion = fechaCalibracion;
        this.fechaExpiracion = fechaExpiracion;
        this.mesesValidez = mesesValidez;
        this.alertaMostrada = 0;
        this.activo = 1;
    }

    /**
     * Constructor completo
     */
    public Producto(Integer id, String nombre, LocalDate fechaCalibracion, LocalDate fechaExpiracion,
                    Integer mesesValidez, Integer alertaMostrada, LocalDate fechaCreacion, Integer activo) {
        this.id = id;
        this.nombre = nombre;
        this.fechaCalibracion = fechaCalibracion;
        this.fechaExpiracion = fechaExpiracion;
        this.mesesValidez = mesesValidez;
        this.alertaMostrada = alertaMostrada;
        this.fechaCreacion = fechaCreacion;
        this.activo = activo;
    }

    // ==================== MÉTODOS DE NEGOCIO ====================

    /**
     * Calcula los días restantes hasta la expiración
     * @return número de días (negativo si ya expiró)
     */
    public long getDiasRestantes() {
        if (fechaExpiracion == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), fechaExpiracion);
    }

    /**
     * Verifica si el producto está próximo a vencer (30 días o menos)
     * @return true si está en el periodo de alerta
     */
    public boolean isProximoAVencer() {
        long dias = getDiasRestantes();
        return dias >= 0 && dias <= 30;
    }

    /**
     * Verifica si el producto ya expiró
     * @return true si la fecha de expiración ya pasó
     */
    public boolean isExpirado() {
        return getDiasRestantes() < 0;
    }

    /**
     * Verifica si el producto está vigente
     * @return true si aún no ha expirado
     */
    public boolean isVigente() {
        return getDiasRestantes() >= 0;
    }

    /**
     * Obtiene una descripción del estado del producto
     * @return descripción legible del estado
     */
    public String getEstadoDescripcion() {
        long dias = getDiasRestantes();

        if (dias < 0) {
            return "EXPIRADO (hace " + Math.abs(dias) + " días)";
        } else if (dias == 0) {
            return "VENCE HOY";
        } else if (dias <= 7) {
            return "URGENTE (" + dias + " días)";
        } else if (dias <= 30) {
            return "PRÓXIMO A VENCER (" + dias + " días)";
        } else {
            return "VIGENTE (" + dias + " días)";
        }
    }

    /**
     * Actualiza la fecha de expiración basada en la fecha de calibración y meses de validez
     */
    public void calcularFechaExpiracion() {
        if (fechaCalibracion != null && mesesValidez != null) {
            this.fechaExpiracion = fechaCalibracion.plusMonths(mesesValidez);
        }
    }

    // ==================== GETTERS Y SETTERS ====================

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public LocalDate getFechaCalibracion() {
        return fechaCalibracion;
    }

    public void setFechaCalibracion(LocalDate fechaCalibracion) {
        this.fechaCalibracion = fechaCalibracion;
    }

    public LocalDate getFechaExpiracion() {
        return fechaExpiracion;
    }

    public void setFechaExpiracion(LocalDate fechaExpiracion) {
        this.fechaExpiracion = fechaExpiracion;
    }

    public Integer getMesesValidez() {
        return mesesValidez;
    }

    public void setMesesValidez(Integer mesesValidez) {
        this.mesesValidez = mesesValidez;
    }

    public Integer getAlertaMostrada() {
        return alertaMostrada;
    }

    public void setAlertaMostrada(Integer alertaMostrada) {
        this.alertaMostrada = alertaMostrada;
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
        Producto producto = (Producto) o;
        return Objects.equals(id, producto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", fechaCalibracion=" + fechaCalibracion +
                ", fechaExpiracion=" + fechaExpiracion +
                ", mesesValidez=" + mesesValidez +
                ", diasRestantes=" + getDiasRestantes() +
                ", estado='" + getEstadoDescripcion() + '\'' +
                '}';
    }
}
