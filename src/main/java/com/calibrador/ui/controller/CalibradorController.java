package com.calibrador.ui.controller;

import com.calibrador.model.Producto;
import com.calibrador.service.NotificacionService;
import com.calibrador.service.NotificacionServiceImpl;
import com.calibrador.service.ProductoService;
import com.calibrador.service.ProductoServiceImpl;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Controlador principal de la ventana Calibrador
 * Maneja todos los eventos de la UI y coordina con los servicios
 */
public class CalibradorController {

    private final ProductoService productoService;
    private final NotificacionService notificacionService;
    private final DefaultTableModel tableModel;
    private final JTable table;
    private ScheduledExecutorService scheduler;

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /**
     * Constructor del controlador
     * @param tableModel modelo de la tabla
     * @param table tabla de la vista
     */
    public CalibradorController(DefaultTableModel tableModel, JTable table) {
        this.productoService = new ProductoServiceImpl();
        this.notificacionService = new NotificacionServiceImpl();
        this.tableModel = tableModel;
        this.table = table;

        // Inicializar verificación de fechas
        inicializarScheduler();
    }

    /**
     * Carga todos los productos en la tabla
     */
    public void cargarTabla() {
        List<Producto> productos = productoService.obtenerTodosLosProductos();

        // Limpiar tabla
        tableModel.setRowCount(0);

        // Configurar columnas si es necesario
        if (tableModel.getColumnCount() == 0) {
            tableModel.addColumn("ID");
            tableModel.addColumn("NOMBRE");
            tableModel.addColumn("FECHA CALIBRACIÓN");
            tableModel.addColumn("FECHA EXPIRACIÓN");
            tableModel.addColumn("MESES");
            tableModel.addColumn("ALERTA MOSTRADA");
        }

        // Llenar tabla con productos
        for (Producto producto : productos) {
            Object[] rowData = new Object[6];
            rowData[0] = producto.getId();
            rowData[1] = producto.getNombre();
            rowData[2] = producto.getFechaCalibracion() != null ?
                    producto.getFechaCalibracion().format(DATE_FORMATTER) : "";
            rowData[3] = producto.getFechaExpiracion() != null ?
                    producto.getFechaExpiracion().format(DATE_FORMATTER) : "";
            rowData[4] = producto.getMesesValidez();
            rowData[5] = producto.getAlertaMostrada();

            tableModel.addRow(rowData);
        }
    }

    /**
     * Maneja el evento de cerrar la aplicación
     */
    public void cerrarAplicacion() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
        System.exit(0);
    }

    /**
     * Maneja el evento de editar nombre de producto
     */
    public void editarNombre() {
        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona una fila para editar");
            return;
        }

        try {
            // Obtener datos de la fila seleccionada
            int id = Integer.parseInt(tableModel.getValueAt(filaSeleccionada, 0).toString());
            String nombre = tableModel.getValueAt(filaSeleccionada, 1).toString();

            // Abrir diálogo de edición
            String nuevoNombre = JOptionPane.showInputDialog(null, "Editar Nombre:", nombre);

            if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
                // Actualizar usando el servicio
                boolean actualizado = productoService.actualizarNombreProducto(id, nuevoNombre);

                if (actualizado) {
                    // Recargar la tabla
                    cargarTabla();
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al convertir el ID",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Maneja el evento de editar fecha de calibración
     */
    public void editarFechaCalibracion() {
        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona una fila para editar");
            return;
        }

        try {
            // Obtener datos de la fila seleccionada
            int id = Integer.parseInt(tableModel.getValueAt(filaSeleccionada, 0).toString());
            String fechaActual = tableModel.getValueAt(filaSeleccionada, 2).toString();

            // Abrir diálogo de edición
            String nuevaFechaStr = JOptionPane.showInputDialog(null,
                    "Editar Fecha Calibración (yyyy-MM-dd):",
                    fechaActual);

            if (nuevaFechaStr != null && !nuevaFechaStr.isEmpty()) {
                try {
                    LocalDate nuevaFecha = LocalDate.parse(nuevaFechaStr, DATE_FORMATTER);

                    // Actualizar usando el servicio
                    boolean actualizado = productoService.actualizarFechaCalibracion(id, nuevaFecha);

                    if (actualizado) {
                        // Recargar la tabla
                        cargarTabla();
                    }
                } catch (DateTimeParseException e) {
                    JOptionPane.showMessageDialog(null,
                            "Formato de fecha inválido. Use yyyy-MM-dd",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al convertir el ID",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Maneja el evento de editar meses de validez
     */
    public void editarMesesValidez() {
        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona una fila para editar");
            return;
        }

        try {
            // Obtener datos de la fila seleccionada
            int id = Integer.parseInt(tableModel.getValueAt(filaSeleccionada, 0).toString());
            int mesesActuales = Integer.parseInt(tableModel.getValueAt(filaSeleccionada, 4).toString());

            // Abrir diálogo de edición
            String nuevosMesesStr = JOptionPane.showInputDialog(null,
                    "Editar meses de validez:",
                    mesesActuales);

            if (nuevosMesesStr != null && !nuevosMesesStr.isEmpty()) {
                try {
                    int nuevosMeses = Integer.parseInt(nuevosMesesStr);

                    // Actualizar usando el servicio
                    boolean actualizado = productoService.actualizarMesesValidez(id, nuevosMeses);

                    if (actualizado) {
                        // Recargar la tabla
                        cargarTabla();
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null,
                            "Debe ingresar un número válido para los meses",
                            "Error",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al procesar los datos",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Maneja el evento de eliminar producto
     */
    public void eliminarProducto() {
        int filaSeleccionada = table.getSelectedRow();

        if (filaSeleccionada == -1) {
            JOptionPane.showMessageDialog(null, "Selecciona una fila para eliminar");
            return;
        }

        try {
            // Obtener el ID del producto
            int id = Integer.parseInt(tableModel.getValueAt(filaSeleccionada, 0).toString());

            // Eliminar usando el servicio (ya incluye confirmación)
            boolean eliminado = productoService.eliminarProducto(id);

            if (eliminado) {
                // Recargar la tabla
                cargarTabla();
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null,
                    "Error al convertir el ID",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Maneja el evento de agregar producto
     */
    public void agregarProducto() {
        try {
            // Solicitar datos mediante diálogos
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto:");

            if (nombre == null || nombre.trim().isEmpty()) {
                return; // Usuario canceló
            }

            // Solicitar fecha de calibración
            String fechaCalibracionStr = JOptionPane.showInputDialog(
                    "Ingrese la fecha de calibración (yyyy-MM-dd):");

            if (fechaCalibracionStr == null) {
                return; // Usuario canceló
            }

            LocalDate fechaCalibracion = LocalDate.parse(fechaCalibracionStr, DATE_FORMATTER);

            // Pedir meses de validez
            String mesesStr = JOptionPane.showInputDialog("Ingrese meses de validez:");

            if (mesesStr == null) {
                return; // Usuario canceló
            }

            int meses = Integer.parseInt(mesesStr);

            // Crear usando el servicio
            boolean creado = productoService.crearProducto(nombre, fechaCalibracion, meses);

            if (creado) {
                // Recargar la tabla
                cargarTabla();
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null,
                    "Debe ingresar un número válido de meses",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(null,
                    "Formato de fecha inválido. Use yyyy-MM-dd",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Verifica productos próximos a vencer y muestra notificaciones
     */
    public void verificarFechasYNotificar() {
        List<Producto> productosProximos = productoService.obtenerProductosProximosAVencer();

        if (!productosProximos.isEmpty()) {
            notificacionService.notificarProductosProximosAVencer(productosProximos);
        }
    }

    /**
     * Inicializa el scheduler para verificar fechas periódicamente
     */
    private void inicializarScheduler() {
        // Verificar fechas al iniciar
        verificarFechasYNotificar();

        // Configurar el temporizador para verificar fechas cada día
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::verificarFechasYNotificar, 0, 1, TimeUnit.DAYS);
    }

    /**
     * Detiene el scheduler
     */
    public void detenerScheduler() {
        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdown();
        }
    }
}
