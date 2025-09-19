package ecommerce;

import javax.swing.*;
import java.awt.*;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author USER
 */

public class EcommerceGUI {

    private JFrame frame;
    private JTextArea displayArea;
    private JPanel mainPanel, listadoPanel, agregarPanel;
    
    private JTextField idField, nombreField, precioField, searchField;

    public EcommerceGUI() {
        frame = new JFrame("Plataforma E-commerce");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        
        CardLayout cardLayout = new CardLayout();
        frame.setLayout(cardLayout);

        // Paneles
        createMainPanel();
        createListadoPanel();
        createAgregarPanel();
        
        frame.add(mainPanel, "Main");
        frame.add(listadoPanel, "Listado");
        frame.add(agregarPanel, "Agregar");

        frame.setVisible(true);
    }
    
    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        JButton agregarProductoButton = new JButton("Agregar Producto");
        JButton verListadoButton = new JButton("Ver Listado de Productos");
        
        agregarProductoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        verListadoButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        mainPanel.add(agregarProductoButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(verListadoButton);

        agregarProductoButton.addActionListener(e -> ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Agregar"));
        verListadoButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Listado");
            displayCatalogo("Catálogo Inicial");
        });
    }

    private void createListadoPanel() {
        listadoPanel = new JPanel(new BorderLayout());

        displayArea = new JTextArea(20, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPanel = new JScrollPane(displayArea);

        JPanel buttonPanel = new JPanel(new GridLayout(2, 3, 5, 5));
        JButton ordenarIdButton = new JButton("Ordenar por ID");
        JButton ordenarNombreButton = new JButton("Ordenar por Nombre");
        JButton ordenarPrecioButton = new JButton("Ordenar por Precio");
        JButton resetButton = new JButton("Resetear");
        JButton regresarButton = new JButton("Regresar");
        
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Buscar ID");

        buttonPanel.add(ordenarIdButton);
        buttonPanel.add(ordenarNombreButton);
        buttonPanel.add(ordenarPrecioButton);
        buttonPanel.add(resetButton);
        buttonPanel.add(regresarButton);
        
        searchPanel.add(new JLabel("ID a buscar:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        listadoPanel.add(buttonPanel, BorderLayout.NORTH);
        listadoPanel.add(scrollPanel, BorderLayout.CENTER);
        listadoPanel.add(searchPanel, BorderLayout.SOUTH);

        ordenarIdButton.addActionListener(e -> {
            Ecommerce.ordenarCatalogoPorId();
            displayCatalogo("Catálogo Ordenado por ID");
        });
        ordenarNombreButton.addActionListener(e -> {
            Ecommerce.ordenarCatalogoPorNombre();
            displayCatalogo("Catálogo Ordenado por Nombre");
        });
        ordenarPrecioButton.addActionListener(e -> {
            Ecommerce.ordenarCatalogoPorPrecio();
            displayCatalogo("Catálogo Ordenado por Precio");
        });
        resetButton.addActionListener(e -> {
            Ecommerce.resetCatalogo();
            displayCatalogo("Catálogo Reseteado");
        });
        regresarButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });
        searchButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(searchField.getText());
                Producto productoEncontrado = Ecommerce.buscarProductoPorHash(id);
                displayArea.setText("");
                if (productoEncontrado != null) {
                    displayArea.append("¡Producto encontrado!\n");
                    displayArea.append(productoEncontrado.toString() + "\n");
                } else {
                    displayArea.append("Producto con ID " + id + " no encontrado.\n");
                }
            } catch (NumberFormatException ex) {
                displayArea.append("\nPor favor, ingrese un ID válido.");
            }
        });
    }

    private void createAgregarPanel() {
        agregarPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        agregarPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        idField = new JTextField(15);
        nombreField = new JTextField(15);
        precioField = new JTextField(15);
        
        JButton guardarButton = new JButton("Guardar Producto");
        JButton regresarButton = new JButton("Regresar");

        agregarPanel.add(new JLabel("ID:"));
        agregarPanel.add(idField);
        agregarPanel.add(new JLabel("Nombre:"));
        agregarPanel.add(nombreField);
        agregarPanel.add(new JLabel("Precio:"));
        agregarPanel.add(precioField);
        agregarPanel.add(guardarButton);
        agregarPanel.add(regresarButton);

        guardarButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String nombre = nombreField.getText();
                double precio = Double.parseDouble(precioField.getText());

                Ecommerce.agregarProducto(new Producto(id, nombre, precio));
                
                JOptionPane.showMessageDialog(frame, "Producto agregado con éxito!");
                
                // Limpiar campos
                idField.setText("");
                nombreField.setText("");
                precioField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "ID y Precio deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        regresarButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });
    }

    private void displayCatalogo(String title) {
        displayArea.setText("--- " + title + " ---\n");
        for (Producto p : Ecommerce.getCatalogo()) {
            displayArea.append(p.toString() + "\n");
        }
    }
    
        public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}