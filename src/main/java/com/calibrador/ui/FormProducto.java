package com.calibrador.ui.view;

import com.calibrador.ui.controller.ProductoController;
import javax.swing.*;
import java.awt.*;

/**
 * Formulario para agregar/editar productos
 */
public class FormProducto extends JDialog {

    private ProductoController controller;

    private JTextField txtNombre;
    private JTextField txtFechaCalibracion;
    private JTextField txtMeses;
    private JButton btnGuardar;
    private JButton btnCancelar;

    public FormProducto(Frame parent, boolean modal) {
        super(parent, modal);
        controller = new ProductoController();
        initComponents();
        setLocationRelativeTo(parent);
    }

    private void initComponents() {
        setTitle("Agregar Producto");
        setSize(400, 300);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Nombre
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(new JLabel("Nombre:"), gbc);

        gbc.gridx = 1;
        txtNombre = new JTextField(20);
        add(txtNombre, gbc);

        // Fecha Calibración
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(new JLabel("Fecha Calibración (yyyy-MM-dd):"), gbc);

        gbc.gridx = 1;
        txtFechaCalibracion = new JTextField(20);
        add(txtFechaCalibracion, gbc);

        // Meses
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(new JLabel("Meses de Validez:"), gbc);

        gbc.gridx = 1;
        txtMeses = new JTextField(20);
        add(txtMeses, gbc);

        // Botones
        JPanel panelBotones = new JPanel();
        btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());

        btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());

        panelBotones.add(btnGuardar);
        panelBotones.add(btnCancelar);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 2;
        add(panelBotones, gbc);
    }

    private void guardar() {
        boolean guardado = controller.crearProductoDesdeFormulario(
                txtNombre,
                txtFechaCalibracion,
                txtMeses
        );

        if (guardado) {
            dispose();
        }
    }
}
