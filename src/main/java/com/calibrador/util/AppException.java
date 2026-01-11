package com.calibrador.util;

/**
 * Excepción personalizada de la aplicación
 * Envuelve excepciones técnicas en excepciones de negocio
 */
public class AppException extends Exception {

    private String codigo;
    private String detalles;

    /**
     * Constructor con mensaje
     * @param mensaje mensaje de la excepción
     */
    public AppException(String mensaje) {
        super(mensaje);
    }

    /**
     * Constructor con mensaje y causa
     * @param mensaje mensaje de la excepción
     * @param causa excepción original
     */
    public AppException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }

    /**
     * Constructor con mensaje, código y detalles
     * @param mensaje mensaje de la excepción
     * @param codigo código de error
     * @param detalles detalles adicionales
     */
    public AppException(String mensaje, String codigo, String detalles) {
        super(mensaje);
        this.codigo = codigo;
        this.detalles = detalles;
    }

    /**
     * Constructor completo
     * @param mensaje mensaje de la excepción
     * @param codigo código de error
     * @param detalles detalles adicionales
     * @param causa excepción original
     */
    public AppException(String mensaje, String codigo, String detalles, Throwable causa) {
        super(mensaje, causa);
        this.codigo = codigo;
        this.detalles = detalles;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDetalles() {
        return detalles;
    }

    public void setDetalles(String detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AppException: ").append(getMessage());

        if (codigo != null) {
            sb.append(" [Código: ").append(codigo).append("]");
        }

        if (detalles != null) {
            sb.append(" - ").append(detalles);
        }

        return sb.toString();
    }
}
