package com.calibrador.ui.view;

import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.time.temporal.ChronoUnit;

@SuppressWarnings("unused")
public class Calibrador extends javax.swing.JFrame {
    DefaultTableModel model;
    String url = "jdbc:sqlite:" + getClass().getResource("/db/Inventario.db").getPath();
    Connection connect;
    ScheduledExecutorService scheduler;

    public Calibrador() {
        initComponents();
        setLocationRelativeTo(null);
        model = new DefaultTableModel() {};
        jTable1.setModel(model);

        // Inicializar conexión a la base de datos y cargar datos
        connectToDatabase();
        cargarElementos(); // Carga automática al iniciar

        // Verificar fechas al iniciar la aplicación
        verificarFechas(); // Llama a verificarFechas aquí

        // Configurar el temporizador para verificar fechas cada día
        scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(this::verificarFechas, 0, 1, TimeUnit.DAYS);
    }
    private void connectToDatabase() {
        int reintentos = 3;
        while (reintentos > 0) {
            try {
                connect = DriverManager.getConnection(url);
                System.out.println("Conexión a la base de datos establecida.");
                return; // Salir del método si la conexión es exitosa
            } catch (SQLException ex) {
                reintentos--;
                JOptionPane.showMessageDialog(null, "Error de conexión a la base de datos: " + ex.getMessage());
                if (reintentos > 0) {
                    JOptionPane.showMessageDialog(null, "Reintentando conexión... (" + reintentos + " intentos restantes)");
                } else {
                    JOptionPane.showMessageDialog(null, "No se pudo conectar a la base de datos. El programa se cerrará.");
                    System.exit(1);
                }
            }
        }
    }
    private void cargarElementos() {
        String sql = "SELECT * FROM Producto1";
        try (PreparedStatement st = connect.prepareStatement(sql);
             ResultSet resul = st.executeQuery()) {

            ResultSetMetaData metaData = resul.getMetaData();
            int columnCount = metaData.getColumnCount();

            // Limpiar modelo antes de cargar
            model.setRowCount(0);

            // Configurar columnas si es necesario
            if (model.getColumnCount() == 0) {
                for (int i = 1; i <= columnCount; i++) {
                    model.addColumn(metaData.getColumnName(i));
                }
            }

            while (resul.next()) {
                Object[] rowData = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++) {
                    rowData[i - 1] = resul.getObject(i);
                }
                model.addRow(rowData);
            }
        } catch (SQLException x) {
            JOptionPane.showMessageDialog(null,
                    "Error al cargar elementos: " + x.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
    private String obtenerRutaBaseDatos() {
        try {
            // Intentar obtener desde recursos
            URL resourceUrl = getClass().getResource("/db/Inventario.db");
            if (resourceUrl != null) {
                return resourceUrl.toURI().getPath();
            }

            // Intentar desde el directorio de la aplicación
            File appDir = new File(System.getProperty("user.dir"), "db/Inventario.db");
            if (appDir.exists()) {
                return appDir.getAbsolutePath();
            }

            // Intentar desde el directorio de recursos de la aplicación
            File resourcesDir = new File(System.getProperty("user.dir"), "Resources/Inventario.db");
            if (resourcesDir.exists()) {
                return resourcesDir.getAbsolutePath();
            }

            throw new FileNotFoundException("Base de datos no encontrada");
        } catch (Exception e) {
            // Manejo de errores
            System.err.println("Error al localizar base de datos: " + e.getMessage());
            return null;
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents

    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        // Estilo del panel principal
        jPanel1.setBackground(new java.awt.Color(206, 216, 211)); // Azul oscuro
        jPanel1.setFont(new java.awt.Font("Vanilla Extract", 0, 13));

        // Título del encabezado
        jLabel1.setFont(new java.awt.Font("Vanilla Extract", 1, 48));
        jLabel1.setForeground(Color.BLACK);
        jLabel1.setText("CALIBRADOR");

        // Botón de cierre
        jButton1.setFont(new java.awt.Font("Vanilla Extract", 0, 13));
        jButton1.setText("CERRAR PROGRAMA");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        // Configuración de la tabla
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
                new Object[][] {},
                new String[]{
                        "ID", "NOMBRE", "FECHA CALIBRACIÓN", "FECHA EXPIRACIÓN", "MESES", "ALERTA MOSTRADA"
                }
        ));
        jTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_NEXT_COLUMN);

        // Personalización del encabezado de la tabla
        JTableHeader header = jTable1.getTableHeader();
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(new java.awt.Color(26, 115, 232)); // Azul oscuro
        headerRenderer.setForeground(java.awt.Color.WHITE); // Texto blanco
        headerRenderer.setFont(new java.awt.Font("Roboto", java.awt.Font.BOLD, 14)); // Fuente personalizada
        headerRenderer.setHorizontalAlignment(javax.swing.SwingConstants.CENTER); // Texto centrado
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
                    cell.setBackground(new java.awt.Color(220, 240, 255)); // Azul claro para la selección
                    cell.setForeground(java.awt.Color.BLACK); // Texto negro para la selección
                } else {
                    cell.setBackground(row % 2 == 0 ? java.awt.Color.WHITE : new java.awt.Color(247, 247, 247)); // Filas alternadas
                    cell.setForeground(java.awt.Color.BLACK); // Texto negro
                }

                return cell;
            }
        });


        jScrollPane1.setViewportView(jTable1);

        // Configuración de botones adicionales
        jButton2.setFont(new java.awt.Font("Vanilla Extract", 0, 13));
        jButton2.setText("EDITAR NOMBRE");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton3.setFont(new java.awt.Font("Vanilla Extract", 0, 13));
        jButton3.setText("EDITAR FECHA CALIBRACIÓN");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Vanilla Extract", 0, 13));
        jButton6.setText("AGREGAR PRODUCTO");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton5.setFont(new java.awt.Font("Vanilla Extract", 0, 13));
        jButton5.setText("ELIMINAR PRODUCTO");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Vanilla Extract", 0, 13));
        jButton4.setText("EDITAR MESES");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        // Layout del panel principal
        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jButton1)
                                                .addGap(184, 184, 184)
                                                .addComponent(jLabel1))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(65, 65, 65)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 880, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
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
                jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jButton1))
                                        .addGroup(jPanel1Layout.createSequentialGroup()
                                                .addGap(24, 24, 24)
                                                .addComponent(jLabel1)))
                                .addGap(39, 39, 39)
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 198, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 164, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jButton2)
                                        .addComponent(jButton3)
                                        .addComponent(jButton5)
                                        .addComponent(jButton6)
                                        .addComponent(jButton4))
                                .addGap(31, 31, 31))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }
// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        int filaSeleccionada = jTable1.getSelectedRow();

        if (filaSeleccionada != -1) {
            try {
                // Obtener datos de la fila seleccionada
                int id = Integer.parseInt(model.getValueAt(filaSeleccionada, 0).toString());
                String nombre = model.getValueAt(filaSeleccionada, 1).toString();

                // Abrir diálogo de edición
                String nuevoNombre = JOptionPane.showInputDialog(
                        this,
                        "Editar Nombre:",
                        nombre
                );

                if (nuevoNombre != null && !nuevoNombre.isEmpty()) {
                    // Preparar consulta SQL para actualizar
                    PreparedStatement ps = connect.prepareStatement(
                            "UPDATE Producto1 SET NOMBRE = ? WHERE ID = ?"
                    );
                    ps.setString(1, nuevoNombre);
                    ps.setInt(2, id);

                    // Ejecutar actualización
                    int resultado = ps.executeUpdate();

                    if (resultado > 0) {
                        // Actualizar tabla
                        model.setValueAt(nuevoNombre, filaSeleccionada, 1);
                        JOptionPane.showMessageDialog(
                                this,
                                "Producto actualizado correctamente"
                        );

                        // Recargar la tabla para mostrar los cambios
                        cargarElementos();
                    }

                    ps.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al actualizar: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al convertir el ID",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una fila para editar"
            );
        }
    }
    public static class UtilidadDeNotificacion {
        private static Image imagen;

        static {
            try {
                // Cargar un icono predeterminado
                imagen = Toolkit.getDefaultToolkit().getImage(
                        UtilidadDeNotificacion.class.getResource("/icono.png")
                );

                // Si no encuentras un icono, usa un icono del sistema
                if (imagen == null) {
                    imagen = Toolkit.getDefaultToolkit().getImage(
                            String.valueOf(Toolkit.getDefaultToolkit().getScreenSize())
                    );
                }
            } catch (Exception e) {
                // Crear un icono básico si falla la carga
                imagen = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
                Graphics g = imagen.getGraphics();
                g.setColor(Color.BLUE);
                g.fillRect(0, 0, 16, 16);
                g.dispose();
            }
        }

        public static void mostrarNotificacion(String titulo, String mensaje) {
            try {
                // Verificar soporte del sistema de bandeja
                if (!SystemTray.isSupported()) {
                    System.out.println("¡El área de notificación no es compatible!");
                    // Mostrar como JOptionPane si no hay soporte de bandeja
                    JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
                    return;
                }

                // Obtener la bandeja del sistema
                SystemTray tray = SystemTray.getSystemTray();

                // Crear icono de bandeja
                TrayIcon trayIcon = new TrayIcon(imagen, "Notificación de Calibración");
                trayIcon.setImageAutoSize(true);
                trayIcon.setToolTip("Notificación de Calibración");

                // Añadir un menú emergente opcional
                PopupMenu popup = new PopupMenu();
                MenuItem defaultItem = new MenuItem("Cerrar");
                defaultItem.addActionListener(e -> tray.remove(trayIcon));
                popup.add(defaultItem);
                trayIcon.setPopupMenu(popup);

                // Añadir el icono a la bandeja
                tray.add(trayIcon);

                // Mostrar mensaje
                trayIcon.displayMessage(titulo, mensaje, TrayIcon.MessageType.INFO);

            } catch (AWTException e) {
                // Mostrar como JOptionPane si falla la notificación del sistema
                System.err.println("Error al mostrar notificación del sistema: " + e.getMessage());
                JOptionPane.showMessageDialog(null, mensaje, titulo, JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }


    private void verificarFechas() {
        try {
            String sql = "SELECT NOMBRE, FECHACALIBRACION, FECHAEXPIRACION FROM Producto1";
            PreparedStatement statement = connect.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            java.util.List<String> productosCercanosAVencer = new ArrayList<>();

            while (resultSet.next()) {
                String nombre = resultSet.getString("NOMBRE");
                String fechaCalibracionStr = resultSet.getString("FECHACALIBRACION");
                String fechaExpiracionStr = resultSet.getString("FECHAEXPIRACION");

                // Parsear las fechas directamente desde los strings
                LocalDate fechaCalibracion = LocalDate.parse(fechaCalibracionStr, formatter);
                LocalDate fechaExpiracion = LocalDate.parse(fechaExpiracionStr, formatter);

                // Calcular días restantes
                long diasRestantes = ChronoUnit.DAYS.between(LocalDate.now(), fechaExpiracion);

                // Verificar si la fecha de expiración está próxima
                if (diasRestantes <= 30 && diasRestantes >= 0) {
                    productosCercanosAVencer.add(nombre + " expira en " + diasRestantes + " días");
                }
            }

            // Mostrar notificación si hay productos próximos a vencer
            if (!((ArrayList<?>) productosCercanosAVencer).isEmpty()) {
                String mensaje = "Los siguientes productos están próximos a vencer:\n" + String.join("\n", productosCercanosAVencer);
                UtilidadDeNotificacion.mostrarNotificacion("Alerta de Expiración", mensaje);
            }

            // Cerrar el ResultSet y el Statement
            resultSet.close();
            statement.close();
        } catch (SQLException | DateTimeParseException e) {
            JOptionPane.showMessageDialog(
                    this,
                    "Error al verificar fechas: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        int filaSeleccionada = jTable1.getSelectedRow();

        if (filaSeleccionada != -1) {
            try {
                // Obtener datos de la fila seleccionada
                int id = Integer.parseInt(model.getValueAt(filaSeleccionada, 0).toString());
                String nombreProducto = model.getValueAt(filaSeleccionada, 1).toString();
                String fechaActualCalibracion = model.getValueAt(filaSeleccionada, 2).toString();

                // Obtener meses de validez existentes (si es posible)
                int mesesValidez = 12; // Valor por defecto si no se puede recuperar
                try {
                    PreparedStatement getMeses = connect.prepareStatement(
                            "SELECT MESES_VALIDEZ FROM Producto1 WHERE ID = ?"
                    );
                    getMeses.setInt(1, id);
                    ResultSet rs = getMeses.executeQuery();
                    if (rs.next()) {
                        mesesValidez = rs.getInt("MESES_VALIDEZ");
                    }
                } catch (SQLException ex) {
                    // Sí falla, mantener valor por defecto
                    System.out.println("No se pudo recuperar meses de validez: " + ex.getMessage());
                }

                // Nueva fecha de calibración
                String nuevaFechaCalibracion = JOptionPane.showInputDialog(
                        this,
                        "Editar Fecha Calibración:",
                        fechaActualCalibracion
                );

                if (nuevaFechaCalibracion != null && !nuevaFechaCalibracion.isEmpty()) {
                    // Calcular nueva fecha de expiración
                    LocalDate fechaCalibracion = LocalDate.parse(nuevaFechaCalibracion, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    LocalDate nuevaFechaExpiracion = fechaCalibracion.plusMonths(mesesValidez);

                    // Formatear fechas
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String fechaExpiracionFormateada = nuevaFechaExpiracion.format(formatter);

                    // Preparar consulta SQL para actualizar
                    PreparedStatement ps = connect.prepareStatement(
                            "UPDATE Producto1 SET FECHACALIBRACION = ?, FECHAEXPIRACION = ? WHERE ID = ?"
                    );
                    ps.setString(1, nuevaFechaCalibracion);
                    ps.setString(2, fechaExpiracionFormateada);
                    ps.setInt(3, id);

                    // Ejecutar actualización
                    int resultado = ps.executeUpdate();

                    if (resultado > 0) {
                        // Actualizar tabla
                        model.setValueAt(nuevaFechaCalibracion, filaSeleccionada, 2);
                        model.setValueAt(fechaExpiracionFormateada, filaSeleccionada, 3);

                        JOptionPane.showMessageDialog(
                                this,
                                "Fecha de calibración y expiración actualizadas correctamente"
                        );

                        // Recargar la tabla para mostrar los cambios
                        cargarElementos();

                        // Configurar nueva alerta de expiración
                        configurarAlertaExpiracion(nombreProducto, nuevaFechaExpiracion);
                    }

                    ps.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al actualizar: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al convertir el ID",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una fila para editar"
            );
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            // Obtener la fecha actual del sistema
            // LocalDate fechaActual = LocalDate.now(); // No se utiliza

            // Preparar PreparedStatement
            String sql = "INSERT INTO Producto1 (NOMBRE, FECHACALIBRACION, FECHAEXPIRACION, MESES_VALIDEZ) VALUES (?, ?, ?, ?)";
            PreparedStatement pst = connect.prepareStatement(sql);

            // Obtener datos mediante diálogos
            String nombre = JOptionPane.showInputDialog("Ingrese el nombre del producto:");

            // Solicitar fecha de calibración
            String fechaCalibracionString = JOptionPane.showInputDialog("Ingrese la fecha de calibración (yyyy-MM-dd):");
            LocalDate fechaCalibracion = LocalDate.parse(fechaCalibracionString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

            // Pedir meses de validez
            String mesesString = JOptionPane.showInputDialog("Ingrese meses de validez:");
            int meses = Integer.parseInt(mesesString);

            // Calcular fecha de expiración basada en la fecha de calibración
            LocalDate fechaExpiracion = fechaCalibracion.plusMonths(meses);

            // Formatear fechas
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            String fechaCalibracionFormateada = fechaCalibracion.format(formatter);
            String fechaExpiracionFormateada = fechaExpiracion.format(formatter);

            if (nombre != null && !nombre.isEmpty()) {
                pst.setString(1, nombre);
                pst.setString(2, fechaCalibracionFormateada);
                pst.setString(3, fechaExpiracionFormateada);
                pst.setInt(4, meses);

                // Ejecutar inserción
                int filasAfectadas = pst.executeUpdate();

                if (filasAfectadas > 0) {
                    JOptionPane.showMessageDialog(this, "Producto agregado exitosamente");
                    // Actualizar tabla
                    cargarElementos();

                    // Configurar monitoreo de expiración
                    configurarAlertaExpiracion(nombre, fechaExpiracion);
                }

                pst.close();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Error al agregar producto: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Debe ingresar un número válido de meses",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        } catch (java.time.format.DateTimeParseException e) {
            JOptionPane.showMessageDialog(this,
                    "Formato de fecha inválido. Use yyyy-MM-dd",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Metodo para configurar alerta de expiración
    private void configurarAlertaExpiracion(String nombreProducto, LocalDate fechaExpiracion) {
        Timer timer = new Timer(5000, new ActionListener() { // Cambia a 5 segundos en lugar de un día
            @Override
            public void actionPerformed(ActionEvent e) {
                LocalDate hoy = LocalDate.now();
                Period periodoRestante = Period.between(hoy, fechaExpiracion);

                // Cambia la condición para que muestre la alerta más fácilmente
                if (periodoRestante.getMonths() <= 1 && periodoRestante.getDays() <= 30) {
                    JOptionPane.showMessageDialog(null,
                            "ALERTA: El producto " + nombreProducto + " expira en " +
                                    periodoRestante.getMonths() + " meses y " +
                                    periodoRestante.getDays() + " días"
                    );
                }
            }
        });
        timer.start(); // Iniciar monitoreo
    }//GEN-LAST:event_jButton6ActionPerformed


    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        int filaSeleccionada = jTable1.getSelectedRow();

        if (filaSeleccionada != -1) {
            try {
                // Obtener el ID del elemento seleccionado
                int id = Integer.parseInt(model.getValueAt(filaSeleccionada, 0).toString());

                // Confirmar eliminación
                int confirmacion = JOptionPane.showConfirmDialog(
                        this,
                        "¿Está seguro que desea eliminar este elemento?",
                        "Confirmar Eliminación",
                        JOptionPane.YES_NO_OPTION
                );

                if (confirmacion == JOptionPane.YES_OPTION) {
                    // Preparar consulta SQL para eliminar
                    PreparedStatement ps = connect.prepareStatement(
                            "DELETE FROM Producto1 WHERE ID = ?"
                    );
                    ps.setInt(1, id);

                    // Ejecutar eliminación
                    int resultado = ps.executeUpdate();

                    if (resultado > 0) {
                        // Eliminar fila de la tabla
                        model.removeRow(filaSeleccionada);
                        JOptionPane.showMessageDialog(
                                this,
                                "Producto eliminado correctamente"
                        );
                    }

                    ps.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al eliminar: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al convertir el ID",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una fila para eliminar"
            );
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        int filaSeleccionada = jTable1.getSelectedRow();

        if (filaSeleccionada != -1) {
            try {
                // Obtener el ID y los datos actuales de la fila seleccionada
                int id = Integer.parseInt(model.getValueAt(filaSeleccionada, 0).toString());
                int mesesValidezActual = Integer.parseInt(model.getValueAt(filaSeleccionada, 4).toString()); // Asumiendo que los meses están en la columna 4 (índice 4)

                // Pedir al usuario el nuevo valor de meses
                String nuevoMesesString = JOptionPane.showInputDialog(
                        this,
                        "Editar meses de validez:",
                        mesesValidezActual
                );

                if (nuevoMesesString != null && !nuevoMesesString.isEmpty()) {
                    int nuevosMeses = Integer.parseInt(nuevoMesesString);

                    // Recuperar la fecha de calibración
                    String fechaCalibracionString = model.getValueAt(filaSeleccionada, 2).toString(); // Fecha calibración en columna 2
                    LocalDate fechaCalibracion = LocalDate.parse(fechaCalibracionString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

                    // Calcular nueva fecha de expiración
                    LocalDate nuevaFechaExpiracion = fechaCalibracion.plusMonths(nuevosMeses);

                    // Formatear la nueva fecha de expiración
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                    String fechaExpiracionFormateada = nuevaFechaExpiracion.format(formatter);

                    // Actualizar el valor en la base de datos
                    PreparedStatement ps = connect.prepareStatement(
                            "UPDATE Producto1 SET MESES_VALIDEZ = ?, FECHAEXPIRACION = ? WHERE ID = ?"
                    );
                    ps.setInt(1, nuevosMeses);
                    ps.setString(2, fechaExpiracionFormateada);
                    ps.setInt(3, id);

                    // Ejecutar la actualización en la base de datos
                    int resultado = ps.executeUpdate();

                    if (resultado > 0) {
                        // Actualizar la tabla con los nuevos valores
                        model.setValueAt(nuevosMeses, filaSeleccionada, 4); // Columna de "MESES"
                        model.setValueAt(fechaExpiracionFormateada, filaSeleccionada, 3); // Columna de "FECHA EXPIRACIÓN"

                        JOptionPane.showMessageDialog(
                                this,
                                "Meses y fecha de expiración actualizados correctamente"
                        );

                        // Recargar los elementos de la tabla para mostrar los cambios
                        cargarElementos();
                    }

                    ps.close();
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Error al actualizar: " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(
                        this,
                        "Debe ingresar un número válido para los meses",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } else {
            JOptionPane.showMessageDialog(
                    this,
                    "Selecciona una fila para editar"
            );
        }
    }//GEN-LAST:event_jButton4ActionPerformed


    private void validarEntradaProducto(String nombre, String fechaCalibracion, int meses) throws IllegalArgumentException {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del producto no puede estar vacío");
        }

        try {
            LocalDate.parse(fechaCalibracion, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Formato de fecha inválido. Use yyyy-MM-dd");
        }

        if (meses <= 0) {
            throw new IllegalArgumentException("Los meses de validez deben ser positivos");
        }
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables

    private static class fechaCalibracion {

        private static LocalDate plusMonths(int nuevosMeses) {
            throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
        }

        public fechaCalibracion() {
        }
    }
}