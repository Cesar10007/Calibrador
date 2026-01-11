package com.calibrador.ui;

import com.calibrador.model.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Componente reutilizable para mostrar tabla de productos
 */
public class TablaProductos extends JPanel {

    private JTable table;
    private DefaultTableModel model;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public TablaProductos() {
        initComponents();
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        model = new DefaultTableModel(
                new Object[][]{},
                new String[]{"ID", "NOMBRE", "FECHA CALIBRACIÓN", "FECHA EXPIRACIÓN", "MESES", "DÍAS RESTANTES"}
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // No editable
            }
        };

        table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Renderer para resaltar filas próximas a vencer
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                // Obtener días restantes
                Object diasObj = table.getValueAt(row, 5);
                if (diasObj != null) {
                    try {
                        long dias = Long.parseLong(diasObj.toString());

                        if (dias < 0) {
                            cell.setBackground(new Color(255, 200, 200)); // Rojo claro - expirado
                        } else if (dias <= 7) {
                            cell.setBackground(new Color(255, 220, 150)); // Naranja - urgente
                        } else if (dias <= 30) {
                            cell.setBackground(new Color(255, 255, 150)); // Amarillo - próximo
                        } else {
                            cell.setBackground(Color.WHITE); // Normal
                        }
                    } catch (NumberFormatException e) {
                        cell.setBackground(Color.WHITE);
                    }
                }

                if (isSelected) {
                    cell.setBackground(new Color(220, 240, 255));
                }

                return cell;
            }
        });

        JScrollPane scrollPane = new JScrollPane(table);
        add(scrollPane, BorderLayout.CENTER);
    }

    public void cargarProductos(List<Producto> productos) {
        model.setRowCount(0);

        for (Producto p : productos) {
            model.addRow(new Object[]{
                    p.getId(),
                    p.getNombre(),
                    p.getFechaCalibracion() != null ? p.getFechaCalibracion().format(DATE_FORMATTER) : "",
                    p.getFechaExpiracion() != null ? p.getFechaExpiracion().format(DATE_FORMATTER) : "",
                    p.getMesesValidez(),
                    p.getDiasRestantes()
            });
        }
    }

    public Producto getProductoSeleccionado(List<Producto> productos) {
        int fila = table.getSelectedRow();
        if (fila >= 0 && fila < productos.size()) {
            return productos.get(fila);
        }
        return null;
    }

    public JTable getTable() {
        return table;
    }
}
