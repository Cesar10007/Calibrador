package com.calibrador.util;

/**
 * Clase con constantes de la aplicación
 * Centraliza valores que se usan en múltiples lugares
 */
public class Constants {

    // Constantes de fecha
    public static final String DATE_FORMAT = "yyyy-MM-dd";

    // Constantes de calibración
    public static final int DIAS_ALERTA_EXPIRACION = 30;
    public static final int DIAS_ALERTA_URGENTE = 7;
    public static final int MESES_VALIDEZ_MINIMO = 1;
    public static final int MESES_VALIDEZ_MAXIMO = 60;

    // Constantes de base de datos
    public static final String DB_PATH = "/db/Inventario.db";
    public static final String DB_DRIVER = "org.sqlite.JDBC";
    public static final int DB_MAX_REINTENTOS = 3;
    public static final int DB_TIMEOUT_SEGUNDOS = 1;

    // Constantes de tabla
    public static final String TABLA_PRODUCTOS = "Producto1";
    public static final String TABLA_EQUIPOS = "Equipo";
    public static final String TABLA_CALIBRACIONES = "Calibracion";

    // Mensajes de error comunes
    public static final String ERROR_CONEXION_BD = "Error de conexión a la base de datos";
    public static final String ERROR_DATOS_INVALIDOS = "Los datos ingresados no son válidos";
    public static final String ERROR_PRODUCTO_NO_ENCONTRADO = "Producto no encontrado";
    public static final String ERROR_OPERACION_CANCELADA = "Operación cancelada por el usuario";

    // Mensajes de éxito
    public static final String EXITO_PRODUCTO_CREADO = "Producto agregado exitosamente";
    public static final String EXITO_PRODUCTO_ACTUALIZADO = "Producto actualizado correctamente";
    public static final String EXITO_PRODUCTO_ELIMINADO = "Producto eliminado correctamente";

    // Títulos de ventanas
    public static final String TITULO_APLICACION = "Sistema de Calibración";
    public static final String TITULO_ERROR = "Error";
    public static final String TITULO_CONFIRMACION = "Confirmación";
    public static final String TITULO_EXITO = "Éxito";
    public static final String TITULO_ALERTA = "Alerta";

    // Colores (RGB)
    public static final int[] COLOR_EXPIRADO = {255, 200, 200};      // Rojo claro
    public static final int[] COLOR_URGENTE = {255, 220, 150};       // Naranja
    public static final int[] COLOR_PROXIMO = {255, 255, 150};       // Amarillo
    public static final int[] COLOR_NORMAL = {255, 255, 255};        // Blanco
    public static final int[] COLOR_SELECCIONADO = {220, 240, 255};  // Azul claro

    // Constructor privado para evitar instanciación
    private Constants() {
        throw new AssertionError("No se puede instanciar la clase Constants");
    }
}
