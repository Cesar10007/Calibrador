package com.calibrador.repository;

import com.calibrador.model.Producto;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Interfaz del repositorio de productos
 * Define las operaciones de acceso a datos para Producto
 */
public interface ProductoRepository {

    /**
     * Obtiene todos los productos de la base de datos
     * @return lista de productos
     * @throws SQLException si hay error en la consulta
     */
    List<Producto> obtenerTodos() throws SQLException;

    /**
     * Obtiene un producto por su ID
     * @param id identificador del producto
     * @return producto encontrado o null si no existe
     * @throws SQLException si hay error en la consulta
     */
    Producto obtenerPorId(int id) throws SQLException;

    /**
     * Inserta un nuevo producto en la base de datos
     * @param producto producto a insertar
     * @return número de filas afectadas
     * @throws SQLException si hay error en la inserción
     */
    int insertar(Producto producto) throws SQLException;

    /**
     * Actualiza el nombre de un producto
     * @param id identificador del producto
     * @param nuevoNombre nuevo nombre del producto
     * @return número de filas afectadas
     * @throws SQLException si hay error en la actualización
     */
    int actualizarNombre(int id, String nuevoNombre) throws SQLException;

    /**
     * Actualiza la fecha de calibración y recalcula la fecha de expiración
     * @param id identificador del producto
     * @param nuevaFechaCalibracion nueva fecha de calibración
     * @param mesesValidez meses de validez del producto
     * @return número de filas afectadas
     * @throws SQLException si hay error en la actualización
     */
    int actualizarFechaCalibracion(int id, LocalDate nuevaFechaCalibracion, int mesesValidez) throws SQLException;

    /**
     * Actualiza los meses de validez y recalcula la fecha de expiración
     * @param id identificador del producto
     * @param nuevosMeses nuevos meses de validez
     * @param fechaCalibracion fecha de calibración actual
     * @return número de filas afectadas
     * @throws SQLException si hay error en la actualización
     */
    int actualizarMesesValidez(int id, int nuevosMeses, LocalDate fechaCalibracion) throws SQLException;

    /**
     * Elimina un producto de la base de datos
     * @param id identificador del producto a eliminar
     * @return número de filas afectadas
     * @throws SQLException si hay error en la eliminación
     */
    int eliminar(int id) throws SQLException;

    /**
     * Obtiene productos próximos a vencer (30 días o menos)
     * @return lista de productos próximos a vencer
     * @throws SQLException si hay error en la consulta
     */
    List<Producto> obtenerProximosAVencer() throws SQLException;
}
