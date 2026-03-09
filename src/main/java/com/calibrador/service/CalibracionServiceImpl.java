package com.calibrador.service;

import com.calibrador.model.Producto;
import com.calibrador.repository.ProductoRepository;
import com.calibrador.repository.ProductoRepositorySQLite;
import com.calibrador.util.AppException;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de productos.
 * Solo contiene lógica de negocio — no sabe nada de UI.
 * Cuando algo sale mal, lanza AppException para que
 * quien llame (el controlador o endpoint) decida cómo mostrarlo.
 */
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl() {
        this.productoRepository = new ProductoRepositorySQLite();
    }

    // Constructor para inyección de dependencias (testing)
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> obtenerTodosLosProductos() throws AppException {
        try {
            return productoRepository.obtenerTodos();
        } catch (SQLException e) {
            throw new AppException("Error al cargar los productos", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public Producto obtenerProductoPorId(int id) throws AppException {
        try {
            Producto producto = productoRepository.obtenerPorId(id);
            if (producto == null) {
                throw new AppException("Producto no encontrado con ID: " + id, "NOT_FOUND", null);
            }
            return producto;
        } catch (SQLException e) {
            throw new AppException("Error al obtener el producto", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public boolean crearProducto(String nombre, LocalDate fechaCalibracion, int mesesValidez) throws AppException {
        // Validar datos — lanza AppException si algo está mal
        validarProducto(nombre, fechaCalibracion, mesesValidez);

        try {
            LocalDate fechaExpiracion = calcularFechaExpiracion(fechaCalibracion, mesesValidez);

            Producto producto = new Producto();
            producto.setNombre(nombre.trim());
            producto.setFechaCalibracion(fechaCalibracion);
            producto.setFechaExpiracion(fechaExpiracion);
            producto.setMesesValidez(mesesValidez);

            int filasAfectadas = productoRepository.insertar(producto);
            return filasAfectadas > 0;

        } catch (SQLException e) {
            throw new AppException("Error al guardar el producto en la base de datos", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizarNombreProducto(int id, String nuevoNombre) throws AppException {
        if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
            throw new AppException("El nombre no puede estar vacío", "VALIDATION_ERROR", null);
        }

        try {
            int resultado = productoRepository.actualizarNombre(id, nuevoNombre.trim());
            return resultado > 0;
        } catch (SQLException e) {
            throw new AppException("Error al actualizar el nombre", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizarFechaCalibracion(int id, LocalDate nuevaFechaCalibracion) throws AppException {
        if (nuevaFechaCalibracion == null) {
            throw new AppException("La fecha de calibración no puede ser nula", "VALIDATION_ERROR", null);
        }

        try {
            Producto producto = productoRepository.obtenerPorId(id);
            if (producto == null) {
                throw new AppException("Producto no encontrado con ID: " + id, "NOT_FOUND", null);
            }

            int resultado = productoRepository.actualizarFechaCalibracion(
                    id,
                    nuevaFechaCalibracion,
                    producto.getMesesValidez()
            );
            return resultado > 0;

        } catch (SQLException e) {
            throw new AppException("Error al actualizar la fecha de calibración", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public boolean actualizarMesesValidez(int id, int nuevosMeses) throws AppException {
        if (nuevosMeses <= 0) {
            throw new AppException("Los meses de validez deben ser un número positivo", "VALIDATION_ERROR", null);
        }

        try {
            Producto producto = productoRepository.obtenerPorId(id);
            if (producto == null) {
                throw new AppException("Producto no encontrado con ID: " + id, "NOT_FOUND", null);
            }

            int resultado = productoRepository.actualizarMesesValidez(
                    id,
                    nuevosMeses,
                    producto.getFechaCalibracion()
            );
            return resultado > 0;

        } catch (SQLException e) {
            throw new AppException("Error al actualizar los meses de validez", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public boolean eliminarProducto(int id) throws AppException {
        try {
            // Verificar que existe antes de eliminar
            Producto producto = productoRepository.obtenerPorId(id);
            if (producto == null) {
                throw new AppException("Producto no encontrado con ID: " + id, "NOT_FOUND", null);
            }

            int resultado = productoRepository.eliminar(id);
            return resultado > 0;

        } catch (SQLException e) {
            throw new AppException("Error al eliminar el producto", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public List<Producto> obtenerProductosProximosAVencer() throws AppException {
        try {
            return productoRepository.obtenerProximosAVencer();
        } catch (SQLException e) {
            throw new AppException("Error al consultar productos próximos a vencer", "DB_ERROR", e.getMessage(), e);
        }
    }

    @Override
    public LocalDate calcularFechaExpiracion(LocalDate fechaCalibracion, int mesesValidez) {
        if (fechaCalibracion == null) {
            throw new IllegalArgumentException("La fecha de calibración no puede ser nula");
        }
        return fechaCalibracion.plusMonths(mesesValidez);
    }

    @Override
    public void validarProducto(String nombre, LocalDate fechaCalibracion, int meses) throws AppException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new AppException("El nombre del producto no puede estar vacío", "VALIDATION_ERROR", null);
        }
        if (fechaCalibracion == null) {
            throw new AppException("La fecha de calibración no puede ser nula", "VALIDATION_ERROR", null);
        }
        if (meses <= 0) {
            throw new AppException("Los meses de validez deben ser un número positivo", "VALIDATION_ERROR", null);
        }
    }
}