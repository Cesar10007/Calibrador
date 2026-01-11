package com.calibrador.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Clase utilitaria para validaciones genéricas
 * Contiene métodos de validación reutilizables en toda la aplicación
 */
public class Validator {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern(Constants.DATE_FORMAT);

    /**
     * Valida que un texto no sea nulo ni vacío
     * @param texto texto a validar
     * @param nombreCampo nombre del campo para el mensaje de error
     * @throws IllegalArgumentException si el texto es inválido
     */
    public static void validarTextoNoVacio(String texto, String nombreCampo) {
        if (texto == null || texto.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo '" + nombreCampo + "' no puede estar vacío");
        }
    }

    /**
     * Valida que un número sea positivo
     * @param numero número a validar
     * @param nombreCampo nombre del campo para el mensaje de error
     * @throws IllegalArgumentException si el número no es positivo
     */
    public static void validarNumeroPositivo(int numero, String nombreCampo) {
        if (numero <= 0) {
            throw new IllegalArgumentException("El campo '" + nombreCampo + "' debe ser un número positivo");
        }
    }

    /**
     * Valida que un número esté dentro de un rango
     * @param numero número a validar
     * @param minimo valor mínimo permitido
     * @param maximo valor máximo permitido
     * @param nombreCampo nombre del campo para el mensaje de error
     * @throws IllegalArgumentException si el número está fuera del rango
     */
    public static void validarRango(int numero, int minimo, int maximo, String nombreCampo) {
        if (numero < minimo || numero > maximo) {
            throw new IllegalArgumentException(
                    "El campo '" + nombreCampo + "' debe estar entre " + minimo + " y " + maximo
            );
        }
    }

    /**
     * Valida que una fecha tenga el formato correcto (yyyy-MM-dd)
     * @param fechaStr fecha en formato String
     * @param nombreCampo nombre del campo para el mensaje de error
     * @return LocalDate si la fecha es válida
     * @throws IllegalArgumentException si el formato es inválido
     */
    public static LocalDate validarFormatoFecha(String fechaStr, String nombreCampo) {
        try {
            return LocalDate.parse(fechaStr, DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "El campo '" + nombreCampo + "' debe tener el formato " + Constants.DATE_FORMAT
            );
        }
    }

    /**
     * Valida que una fecha no sea nula
     * @param fecha fecha a validar
     * @param nombreCampo nombre del campo para el mensaje de error
     * @throws IllegalArgumentException si la fecha es nula
     */
    public static void validarFechaNoNula(LocalDate fecha, String nombreCampo) {
        if (fecha == null) {
            throw new IllegalArgumentException("El campo '" + nombreCampo + "' no puede ser nulo");
        }
    }

    /**
     * Valida que una fecha sea futura
     * @param fecha fecha a validar
     * @param nombreCampo nombre del campo para el mensaje de error
     * @throws IllegalArgumentException si la fecha no es futura
     */
    public static void validarFechaFutura(LocalDate fecha, String nombreCampo) {
        validarFechaNoNula(fecha, nombreCampo);

        if (fecha.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "El campo '" + nombreCampo + "' debe ser una fecha futura"
            );
        }
    }

    /**
     * Valida que una fecha no sea futura
     * @param fecha fecha a validar
     * @param nombreCampo nombre del campo para el mensaje de error
     * @throws IllegalArgumentException si la fecha es futura
     */
    public static void validarFechaPasada(LocalDate fecha, String nombreCampo) {
        validarFechaNoNula(fecha, nombreCampo);

        if (fecha.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(
                    "El campo '" + nombreCampo + "' no puede ser una fecha futura"
            );
        }
    }

    /**
     * Valida que un ID sea válido (mayor que 0)
     * @param id identificador a validar
     * @throws IllegalArgumentException si el ID es inválido
     */
    public static void validarId(Integer id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("El ID debe ser un número positivo");
        }
    }

    /**
     * Valida que una longitud de texto no exceda el máximo
     * @param texto texto a validar
     * @param longitudMaxima longitud máxima permitida
     * @param nombreCampo nombre del campo para el mensaje de error
     * @throws IllegalArgumentException si excede la longitud
     */
    public static void validarLongitudMaxima(String texto, int longitudMaxima, String nombreCampo) {
        if (texto != null && texto.length() > longitudMaxima) {
            throw new IllegalArgumentException(
                    "El campo '" + nombreCampo + "' no puede exceder " + longitudMaxima + " caracteres"
            );
        }
    }

    /**
     * Valida que un objeto no sea nulo
     * @param objeto objeto a validar
     * @param nombreCampo nombre del campo para el mensaje de error
     * @throws IllegalArgumentException si el objeto es nulo
     */
    public static void validarNoNulo(Object objeto, String nombreCampo) {
        if (objeto == null) {
            throw new IllegalArgumentException("El campo '" + nombreCampo + "' no puede ser nulo");
        }
    }

    // Constructor privado para evitar instanciación
    private Validator() {
        throw new AssertionError("No se puede instanciar la clase Validator");
    }
}
