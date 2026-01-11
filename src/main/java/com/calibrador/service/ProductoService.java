package com.calibrador.service;

import com.calibrador.model.Producto;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz del servicio de productos
 * Define la lógica de negocio para la gestión de productos de calibración
 */
public interface ProductoService {

    /**
     * Obtiene todos los productos
     * @return lista de productos
     */
    List<Producto> obtenerTodosLosProductos();

    /**
     * Obtiene un producto por su ID
     * @param id identificador del producto
     * @return producto encontrado o null
     */
    Producto obtenerProductoPorId(int id);

    /**
     * Crea un nuevo producto con validaciones
     * @param nombre nombre del producto
     * @param fechaCalibracion fecha de calibración
     * @param mesesValidez meses de validez
     * @return true si se creó correctamente
     */
    boolean crearProducto(String nombre, LocalDate fechaCalibracion, int mesesValidez);

    /**
     * Actualiza el nombre de un producto
     * @param id identificador del producto
     * @param nuevoNombre nuevo nombre
     * @return true si se actualizó correctamente
     */
    boolean actualizarNombreProducto(int id, String nuevoNombre);

    /**
     * Actualiza la fecha de calibración y recalcula la expiración
     * @param id identificador del producto
     * @param nuevaFechaCalibracion nueva fecha de calibración
     * @return true si se actualizó correctamente
     */
    boolean actualizarFechaCalibracion(int id, LocalDate nuevaFechaCalibracion);

    /**
     * Actualiza los meses de validez y recalcula la expiración
     * @param id identificador del producto
     * @param nuevosMeses nuevos meses de validez
     * @return true si se actualizó correctamente
     */
    boolean actualizarMesesValidez(int id, int nuevosMeses);

    /**
     * Elimina un producto
     * @param id identificador del producto
     * @return true si se eliminó correctamente
     */
    boolean eliminarProducto(int id);

    /**
     * Obtiene productos próximos a vencer (30 días o menos)
     * @return lista de productos próximos a vencer
     */
    List<Producto> obtenerProductosProximosAVencer();

    /**
     * Calcula la fecha de expiración basada en fecha de calibración y meses
     * @param fechaCalibracion fecha de calibración
     * @param mesesValidez meses de validez
     * @return fecha de expiración calculada
     */
    LocalDate calcularFechaExpiracion(LocalDate fechaCalibracion, int mesesValidez);

    /**
     * Valida los datos de un producto antes de guardar
     * @param nombre nombre del producto
     * @param fechaCalibracion fecha de calibración
     * @param meses meses de validez
     * @throws IllegalArgumentException si los datos no son válidos
     */
    void validarProducto(String nombre, LocalDate fechaCalibracion, int meses);
}
