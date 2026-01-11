package com.calibrador.ui.view;

import com.calibrador.ui.controller.CalibradorController;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;

/**
 * Vista principal del sistema de calibración
 * Maneja solo la interfaz gráfica, delega toda la lógica al controlador
 */
public class Calibrador extends JFrame {

    private DefaultTableModel model;
    private CalibradorController controller;

    // Componentes de la UI
    private JPanel jPanel1;
    private JLabel jLabel1;
    private JButton jButton1;
    private JScrollPane jScrollPane1;
    private JTable jTable1;
    private JButton jButton2;
    private JButton jButton3;
    private JButton jButton4;
    private JButton jButton5;
    private JButton jButton6;

    public Calibrador() {
        initComponents();
        setLocationRelativeTo(null);

        // Inicializar modelo de tabla
        model = new DefaultTableModel();
        jTable1.setModel(model);

        // Inicializar controlador
        controller = new CalibradorController(model, jTable1);

        // Cargar datos iniciales
        controller.cargarTabla();
    }

    private void initComponents() {
        jPanel1 = new JPanel();
        jLabel1 = new JLabel();
        jButton1 = new JButton();
        jScrollPane1 = new JScrollPane();
        jTable1 = new JTable();
        jButton2 = new JButton();
        jButton3 = new JButton();
        jButton6 = new JButton();
        jButton5 = new JButton();
        jButton4 = new JButton();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        // Estilo del panel principal
        jPanel1.setBackground(new Color(206, 216, 211));
        jPanel1.setFont(new Font("Vanilla Extract", 0, 13));

        // Título del encabezado
        jLabel1.setFont(new Font("Vanilla Extract", 1, 48));
        jLabel1.setForeground(Color.BLACK);
        jLabel1.setText("CALIBRADOR");

        // Botón de cierre
        jButton1.setFont(new Font("Vanilla Extract", 0, 13));
        jButton1.setText("CERRAR PROGRAMA");
        jButton1.addActionListener(evt -> jButton1ActionPerformed(evt));

        // Configuración de la tabla
        jTable1.setModel(new DefaultTableModel(
                new Object[][]{},
                new String[]{
                        "ID", "NOMBRE", "FECHA CALIBRACIÓN", "FECHA EXPIRACIÓN", "MESES", "ALERTA MOSTRADA"
                }
        ));
        jTable1.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);

        // Personalización del encabezado de la tabla
        JTableHeader header = jTable1.getTableHeader();
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new Color(26, 115, 232));
        headerRenderer.setForeground(Color.WHITE);
        headerRenderer.setFont(new Font("Roboto", Font.BOLD, 14));
        headerRenderer.setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < jTable1.getColumnModel().getColumnCount(); i++) {
            jTable1.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        // Personalización de las filas alternadas de la tabla
        jTable1.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);

                if (isSelected) {
                    cell.setBackground(new Color(220, 240, 255));
                    cell.setForeground(Color.BLACK);
                } else {
                    cell.setBackground(row % 2 == 0 ? Color.WHITE : new Color(247, 247, 247));
                    cell.setForeground(Color.BLACK);
                }

                return cell;
            }
        });

        jScrollPane1.setViewportView(jTable1);

        // Configuración de botones adicionales
        jButton2.setFont(new Font("Vanilla Extract", 0, 13));
        jButton2.setText("EDITAR NOMBRE");
        jButton2.addActionListener(evt -> jButton2ActionPerformed(evt));

        jButton3.setFont(new Font("Vanilla Extract", 0, 13));
        jButton3.setText("EDITAR FECHA CALIBRACIÓN");
        jButton3.addActionListener(evt -> jButton3ActionPerformed(evt));

        jButton6.setFont(new Font("Vanilla Extract", 0, 13));
        jButton6.setText("AGREGAR PRODUCTO");
        jButton6.addActionListener(evt -> jButton6ActionPerformed(evt));

        jButton5.setFont(new Font("Vanilla Extract", 0, 13));
        jButton5.setText("ELIMINAR PRODUCTO");
        jButton5.addActionListener(evt -> jButton5ActionPerformed(evt));

        jButton4.setFont(new Font("Vanilla Extract", 0, 13));
        jButton4.setText("EDITAR MESES");
        jButton4.addActionListener(evt -> jButton4ActionPerformed(evt));

        // Layout del panel principal
        GroupLayout jPanel1Layout = new GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jButton1)
                                                .addGap(184, 184, 184)
                                                .addComponent(jLabel1))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(65, 65, 65)
                                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 880, GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGap(0, 34, Short.MAX_VALUE)
                                .addComponent(jButton2)
                                .addGap(18, 18, 18)
                                .addComponent(jButton3)
                                .addGap(18, 18, 18)
                                .addComponent(jButton5)
                                .addGap(18, 18, 18)
                                .addComponent(jButton6)
                                .addGap(18, 18, 18)
                                .addComponent(jButton4)
                                .addGap(28, 28, 28))
        );
        jPanel1Layout.setVerticalGroup(
                jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jButton1))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addComponent(jLabel1)))
                                .addGap(39, 39, 39)
                                .addComponent(jScrollPane1, GroupLayout.PREFERRED_SIZE, 198, GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3)
                                        .addComponent(jButton5)
                                        .addComponent(jButton6)
                                        .addComponent(jButton4))
                                .addGap(31, 31, 31))
        );

        GroupLayout layout = new GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }

    // ==================== EVENTOS DE LOS BOTONES ====================
    // Ahora solo delegan al controlador

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {
        controller.cerrarAplicacion();
    }

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {
        controller.editarNombre();
    }

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {
        controller.editarFechaCalibracion();
    }

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {
        controller.editarMesesValidez();
    }

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {
        controller.eliminarProducto();
    }

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {
        controller.agregarProducto();
    }
}
