package com.calibrador.ui.controller;

import com.calibrador.model.Producto;
import com.calibrador.service.ProductoService;
import com.calibrador.service.ProductoServiceImpl;
import javax.swing.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Controlador específico para operaciones de productos
 * Usado en formularios o ventanas dedicadas a productos
 */
public class ProductoController {

    private final ProductoService productoService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public ProductoController() {
        this.productoService = new ProductoServiceImpl();
    }

    /**
     * Crea un nuevo producto desde campos de texto
     * @param nombreField campo de nombre
     * @param fechaField campo de fecha
     * @param mesesField campo de meses
     * @return true si se creó exitosamente
     */
    public boolean crearProductoDesdeFormulario(JTextField nombreField,
                                                JTextField fechaField,
                                                JTextField mesesField) {
        try {
            String nombre = nombreField.getText();
            LocalDate fecha = LocalDate.parse(fechaField.getText(), DATE_FORMATTER);
            int meses = Integer.parseInt(mesesField.getText());

            boolean creado = productoService.crearProducto(nombre, fecha, meses);

            if (creado) {
                // Limpiar campos
                nombreField.setText("");
                fechaField.setText("");
                mesesField.setText("");
            }

            return creado;

        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Error en los datos del formulario: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Obtiene un producto por ID
     * @param id identificador del producto
     * @return producto encontrado
     */
    public Producto obtenerProducto(int id) {
        return productoService.obtenerProductoPorId(id);
    }

    /**
     * Actualiza un producto completo
     * @param producto producto con datos actualizados
     * @return true si se actualizó exitosamente
     */
    public boolean actualizarProducto(Producto producto) {
        if (producto == null || producto.getId() == null) {
            return false;
        }

        // Actualizar nombre
        boolean nombreActualizado = productoService.actualizarNombreProducto(
                producto.getId(),
                producto.getNombre()
        );

        // Actualizar fecha de calibración
        boolean fechaActualizada = productoService.actualizarFechaCalibracion(
                producto.getId(),
                producto.getFechaCalibracion()
        );

        return nombreActualizado && fechaActualizada;
    }
}
