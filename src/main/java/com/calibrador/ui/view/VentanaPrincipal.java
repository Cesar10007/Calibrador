package com.calibrador.ui.view;

import javax.swing.*;

/**
 * Ventana principal moderna con menú
 */
public class VentanaPrincipal extends JFrame {

    private Calibrador panelPrincipal;

    public VentanaPrincipal() {
        initComponents();
        setLocationRelativeTo(null);
    }

    private void initComponents() {
        setTitle("Sistema de Calibración de Equipos");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1024, 600);

        // Crear menú
        JMenuBar menuBar = new JMenuBar();

        JMenu menuArchivo = new JMenu("Archivo");
        JMenuItem itemSalir = new JMenuItem("Salir");
        itemSalir.addActionListener(e -> System.exit(0));
        menuArchivo.add(itemSalir);

        JMenu menuProductos = new JMenu("Productos");
        JMenuItem itemAgregar = new JMenuItem("Agregar Producto");
        JMenuItem itemListar = new JMenuItem("Listar Productos");
        menuProductos.add(itemAgregar);
        menuProductos.add(itemListar);

        JMenu menuAyuda = new JMenu("Ayuda");
        JMenuItem itemAcerca = new JMenuItem("Acerca de");
        menuAyuda.add(itemAcerca);

        menuBar.add(menuArchivo);
        menuBar.add(menuProductos);
        menuBar.add(menuAyuda);

        setJMenuBar(menuBar);

        // Panel principal
        panelPrincipal = new Calibrador();
        add(panelPrincipal.getContentPane());
    }
}
