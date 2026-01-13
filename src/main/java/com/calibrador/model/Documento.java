package com.calibrador.model;

import java.time.LocalDateTime;

/**
 * Modelo de Documento
 * Representa certificados, manuales, fotos y otros archivos relacionados con equipos
 */
public class Documento {

    private Integer idDocumento;
    private TipoDocumento tipo;
    private String nombreArchivo;
    private String rutaArchivo;
    private Integer idEquipo;
    private Integer idCalibracion;
    private String descripcion;
    private LocalDateTime fechaSubida;
    private Integer idUsuario;

    // Enum para tipos de documento
    public enum TipoDocumento {
        CERTIFICADO("Certificado de Calibración"),
        MANUAL("Manual de Usuario"),
        FOTO("Fotografía"),
        REPORTE("Reporte Técnico"),
        OTRO("Otro");

        private final String descripcion;

        TipoDocumento(String descripcion) {
            this.descripcion = descripcion;
        }

        public String getDescripcion() {
            return descripcion;
        }
    }

    // Constructor vacío
    public Documento() {
        this.fechaSubida = LocalDateTime.now();
    }

    // Constructor con campos básicos
    public Documento(TipoDocumento tipo, String nombreArchivo, String rutaArchivo) {
        this();
        this.tipo = tipo;
        this.nombreArchivo = nombreArchivo;
        this.rutaArchivo = rutaArchivo;
    }

    // Getters y Setters
    public Integer getIdDocumento() {
        return idDocumento;
    }

    public void setIdDocumento(Integer idDocumento) {
        this.idDocumento = idDocumento;
    }

    public TipoDocumento getTipo() {
        return tipo;
    }

    public void setTipo(TipoDocumento tipo) {
        this.tipo = tipo;
    }

    public String getNombreArchivo() {
        return nombreArchivo;
    }

    public void setNombreArchivo(String nombreArchivo) {
        this.nombreArchivo = nombreArchivo;
    }

    public String getRutaArchivo() {
        return rutaArchivo;
    }

    public void setRutaArchivo(String rutaArchivo) {
        this.rutaArchivo = rutaArchivo;
    }

    public Integer getIdEquipo() {
        return idEquipo;
    }

    public void setIdEquipo(Integer idEquipo) {
        this.idEquipo = idEquipo;
    }

    public Integer getIdCalibracion() {
        return idCalibracion;
    }

    public void setIdCalibracion(Integer idCalibracion) {
        this.idCalibracion = idCalibracion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFechaSubida() {
        return fechaSubida;
    }

    public void setFechaSubida(LocalDateTime fechaSubida) {
        this.fechaSubida = fechaSubida;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    // Métodos auxiliares

    /**
     * Obtiene la extensión del archivo
     */
    public String getExtension() {
        if (nombreArchivo == null || !nombreArchivo.contains(".")) {
            return "";
        }
        return nombreArchivo.substring(nombreArchivo.lastIndexOf(".") + 1).toLowerCase();
    }

    /**
     * Verifica si es un certificado
     */
    public boolean esCertificado() {
        return tipo == TipoDocumento.CERTIFICADO;
    }

    /**
     * Verifica si es una imagen
     */
    public boolean esImagen() {
        String ext = getExtension();
        return ext.equals("jpg") || ext.equals("jpeg") ||
                ext.equals("png") || ext.equals("gif") || ext.equals("bmp");
    }

    /**
     * Verifica si es un PDF
     */
    public boolean esPdf() {
        return getExtension().equals("pdf");
    }

    /**
     * Verifica si está asociado a un equipo
     */
    public boolean tieneEquipoAsociado() {
        return idEquipo != null;
    }

    /**
     * Verifica si está asociado a una calibración
     */
    public boolean tieneCalibracionAsociada() {
        return idCalibracion != null;
    }

    @Override
    public String toString() {
        return "Documento{" +
                "idDocumento=" + idDocumento +
                ", tipo=" + tipo +
                ", nombreArchivo='" + nombreArchivo + '\'' +
                ", idEquipo=" + idEquipo +
                ", idCalibracion=" + idCalibracion +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Documento documento = (Documento) o;
        return idDocumento != null && idDocumento.equals(documento.idDocumento);
    }

    @Override
    public int hashCode() {
        return idDocumento != null ? idDocumento.hashCode() : 0;
    }
}
