package com.calibrador.service;

import com.calibrador.model.Producto;
import com.calibrador.repository.ProductoRepository;
import com.calibrador.repository.ProductoRepositorySQLite;
import javax.swing.JOptionPane;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementación del servicio de productos
 * Contiene toda la lógica de negocio relacionada con productos de calibración
 */
public class ProductoServiceImpl implements ProductoService {

    private final ProductoRepository productoRepository;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ProductoServiceImpl() {
        this.productoRepository = new ProductoRepositorySQLite();
    }

    // Constructor para inyección de dependencias (testing)
    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public List<Producto> obtenerTodosLosProductos() {
        try {
            return productoRepository.obtenerTodos();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar productos: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
        }
    }

    @Override
    public Producto obtenerProductoPorId(int id) {
        try {
            return productoRepository.obtenerPorId(id);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al obtener producto: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    @Override
    public boolean crearProducto(String nombre, LocalDate fechaCalibracion, int mesesValidez) {
        try {
            // Validar datos de entrada
            validarProducto(nombre, fechaCalibracion, mesesValidez);

            // Calcular fecha de expiración
            LocalDate fechaExpiracion = calcularFechaExpiracion(fechaCalibracion, mesesValidez);

            // Crear producto
            Producto producto = new Producto();
            producto.setNombre(nombre);
            producto.setFechaCalibracion(fechaCalibracion);
            producto.setFechaExpiracion(fechaExpiracion);
            producto.setMesesValidez(mesesValidez);

            // Insertar en la base de datos
            int filasAfectadas = productoRepository.insertar(producto);

            if (filasAfectadas > 0) {
                JOptionPane.showMessageDialog(null, "Producto agregado exitosamente");
                return true;
            }

            return false;

        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null,
                    e.getMessage(),
                    "Error de Validación",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al agregar producto: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public boolean actualizarNombreProducto(int id, String nuevoNombre) {
        try {
            // Validar nombre
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(null,
                        "El nombre no puede estar vacío",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Actualizar en la base de datos
            int resultado = productoRepository.actualizarNombre(id, nuevoNombre);

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null, "Producto actualizado correctamente");
                return true;
            }

            return false;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al actualizar: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public boolean actualizarFechaCalibracion(int id, LocalDate nuevaFechaCalibracion) {
        try {
            // Obtener el producto para obtener los meses de validez
            Producto producto = productoRepository.obtenerPorId(id);

            if (producto == null) {
                JOptionPane.showMessageDialog(null,
                        "Producto no encontrado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Actualizar en la base de datos
            int resultado = productoRepository.actualizarFechaCalibracion(
                    id,
                    nuevaFechaCalibracion,
                    producto.getMesesValidez()
            );

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null,
                        "Fecha de calibración y expiración actualizadas correctamente");
                return true;
            }

            return false;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al actualizar: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public boolean actualizarMesesValidez(int id, int nuevosMeses) {
        try {
            // Validar meses
            if (nuevosMeses <= 0) {
                JOptionPane.showMessageDialog(null,
                        "Los meses de validez deben ser positivos",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Obtener el producto para obtener la fecha de calibración
            Producto producto = productoRepository.obtenerPorId(id);

            if (producto == null) {
                JOptionPane.showMessageDialog(null,
                        "Producto no encontrado",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return false;
            }

            // Actualizar en la base de datos
            int resultado = productoRepository.actualizarMesesValidez(
                    id,
                    nuevosMeses,
                    producto.getFechaCalibracion()
            );

            if (resultado > 0) {
                JOptionPane.showMessageDialog(null,
                        "Meses y fecha de expiración actualizados correctamente");
                return true;
            }

            return false;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al actualizar: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public boolean eliminarProducto(int id) {
        try {
            // Confirmar eliminación
            int confirmacion = JOptionPane.showConfirmDialog(null,
                    "¿Está seguro que desea eliminar este producto?",
                    "Confirmar Eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirmacion == JOptionPane.YES_OPTION) {
                int resultado = productoRepository.eliminar(id);

                if (resultado > 0) {
                    JOptionPane.showMessageDialog(null, "Producto eliminado correctamente");
                    return true;
                }
            }

            return false;

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al eliminar: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public List<Producto> obtenerProductosProximosAVencer() {
        try {
            return productoRepository.obtenerProximosAVencer();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null,
                    "Error al verificar fechas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return new ArrayList<>();
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
    public void validarProducto(String nombre, LocalDate fechaCalibracion, int meses) {
        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }

        // Validar fecha de calibración
        if (fechaCalibracion == null) {
            throw new IllegalArgumentException("La fecha de calibración no puede ser nula");
        }

        // Validar meses
        if (meses <= 0) {
            throw new IllegalArgumentException("Los meses de validez deben ser positivos");
        }
    }
}
