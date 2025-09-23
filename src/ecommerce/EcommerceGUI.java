package ecommerce;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

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
    private JPanel listadoDisplayPanel;
    private JScrollPane scrollPanel;
    private JPanel mainPanel, listadoPanel, agregarPanel;

    private JTextField idField, nombreField, precioField, stockField, categoriaField, searchField;

    public EcommerceGUI() {
        frame = new JFrame("Plataforma E-commerce");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);

        CardLayout cardLayout = new CardLayout();
        frame.setLayout(cardLayout);

        // Inicialización de Paneles
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

        listadoDisplayPanel = new JPanel();
        listadoDisplayPanel.setLayout(new GridLayout(0, 3, 10, 10)); // 3 columnas, filas automaticas

        scrollPanel = new JScrollPane(listadoDisplayPanel);
        scrollPanel.setBorder(BorderFactory.createTitledBorder("Listado de Productos"));

        JPanel controlPanel = new JPanel(new BorderLayout());
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

        controlPanel.add(buttonPanel, BorderLayout.NORTH);
        controlPanel.add(searchPanel, BorderLayout.CENTER);

        listadoPanel.add(buttonPanel, BorderLayout.NORTH);
        listadoPanel.add(scrollPanel, BorderLayout.CENTER);

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
                listadoDisplayPanel.removeAll();
                listadoDisplayPanel.revalidate();
                listadoDisplayPanel.repaint();
                if (productoEncontrado != null) {
                    listadoDisplayPanel.add(createProductCard(productoEncontrado));
                    scrollPanel.setBorder(BorderFactory.createTitledBorder("Resultado de Búsqueda (HASH)"));

                } else {
                    listadoDisplayPanel.add(new JLabel("Producto con ID " + id + " no encontrado."));
                    scrollPanel.setBorder(BorderFactory.createTitledBorder("Resultado de Búsqueda"));

                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Por favor, ingrese un ID válido.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    private void createAgregarPanel() {
        agregarPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        agregarPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        idField = new JTextField(15);
        nombreField = new JTextField(15);
        precioField = new JTextField(15);
        stockField = new JTextField(15);
        categoriaField = new JTextField(15);

        JButton guardarButton = new JButton("Guardar Producto");
        JButton regresarButton = new JButton("Regresar");

        agregarPanel.add(new JLabel("ID:"));
        agregarPanel.add(idField);
        agregarPanel.add(new JLabel("Nombre:"));
        agregarPanel.add(nombreField);
        agregarPanel.add(new JLabel("Precio:"));
        agregarPanel.add(precioField);
        agregarPanel.add(new JLabel("Stock:"));
        agregarPanel.add(stockField);
        agregarPanel.add(new JLabel("Categoría:"));
        agregarPanel.add(categoriaField);
        agregarPanel.add(guardarButton);
        agregarPanel.add(regresarButton);

        guardarButton.addActionListener(e -> {
            try {
                int id = Integer.parseInt(idField.getText());
                String nombre = nombreField.getText();
                double precio = Double.parseDouble(precioField.getText());
                int stock = Integer.parseInt(stockField.getText());
                String categoria = categoriaField.getText();

                Ecommerce.agregarProducto(new Producto(id, nombre, precio, stock, categoria));

                JOptionPane.showMessageDialog(frame, "Producto agregado con éxito!");

                // Limpiar campos
                idField.setText("");
                nombreField.setText("");
                precioField.setText("");
                stockField.setText("");
                categoriaField.setText("");

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "ID y Precio deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        regresarButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });
    }

    private JPanel createProductCard(Producto p) {
        JPanel card = new JPanel(new GridLayout(6, 1));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        card.add(new JLabel("<html><b>" + p.getNombre() + "</b></html>"));
        card.add(new JLabel("ID: " + p.getCodigo()));
        card.add(new JLabel("Precio: $" + String.format("%.2f", p.getPrecio())));
        card.add(new JLabel("Stock: " + p.getStock()));
        card.add(new JLabel("Categoría: " + p.getCategoria()));

        if (p.getStock() <= 10) {
            card.setBackground(new Color(255, 220, 220)); // Rojo claro si hay poco stock
        } else {
            card.setBackground(Color.WHITE);
        }

        return card;
    }

    private void displayCatalogo(String title) {
        scrollPanel.setBorder(BorderFactory.createTitledBorder(title));
        listadoDisplayPanel.removeAll();
        for (Producto p : Ecommerce.getCatalogo()) {
            listadoDisplayPanel.add(createProductCard(p));
        }

        listadoDisplayPanel.revalidate();
        listadoDisplayPanel.repaint();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
}
