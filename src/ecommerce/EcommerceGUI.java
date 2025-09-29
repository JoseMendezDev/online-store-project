package ecommerce;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
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
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryFilter;
    
    private JPanel mainPanel, listadoPanel, agregarPanel;

    private JTextField codigoField, nombreField, precioField, stockField, categoriaField, ratingField, searchField;

    public EcommerceGUI() {
        
        try{
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e){
            e.printStackTrace();
        }
        
        frame = new JFrame("Plataforma E-commerce");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 700);
        frame.setLocationRelativeTo(null);

        CardLayout cardLayout = new CardLayout();
        frame.setLayout(cardLayout);

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
            displayCatalogo(Ecommerce.getCatalogo());
        });
    }

    private void createListadoPanel() {
        listadoPanel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"Código", "Nombre", "Precio", "Stock", "Categoría", "Rating"};
        tableModel = new DefaultTableModel(columnNames, 0);
        productTable = new JTable(tableModel);

        productTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPanel = new JScrollPane(productTable);
        scrollPanel.setBorder(BorderFactory.createTitledBorder("Listado de Productos"));

        JPanel controlPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));

        JButton ordenarCodigoButton = new JButton("Ordenar por Código");
        JButton ordenarNombreButton = new JButton("Ordenar por Nombre");
        JButton ordenarPrecioButton = new JButton("Ordenar por Precio");
        JButton resetButton = new JButton("Resetear");
        
        JPanel searchAndFilterPanel = new JPanel(new FlowLayout());
        
        searchField = new JTextField(15);
        JButton searchButton = new JButton("Buscar Código");
        
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("Todas las categorías");
        categoryFilter.addActionListener(e -> filtrarPorCategoria());
        
        for (String categoria : Ecommerce.getCategoriasUnicas()){
            categoryFilter.addItem(categoria);
        }
        
        searchAndFilterPanel.add(new JLabel("Filtrar por:"));
        searchAndFilterPanel.add(categoryFilter);
        searchAndFilterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        searchAndFilterPanel.add(new JLabel("Código a buscar:"));
        searchAndFilterPanel.add(searchField);
        searchAndFilterPanel.add(searchButton);

        buttonPanel.add(ordenarCodigoButton);
        buttonPanel.add(ordenarNombreButton);
        buttonPanel.add(ordenarPrecioButton);
        buttonPanel.add(resetButton);
        
        controlPanel.add(searchAndFilterPanel, BorderLayout.NORTH);
        controlPanel.add(buttonPanel, BorderLayout.CENTER);
    
        listadoPanel.add(controlPanel, BorderLayout.NORTH);
        listadoPanel.add(scrollPanel, BorderLayout.CENTER);
        
        JButton regresarButton = new JButton("Regresar");
        listadoPanel.add(regresarButton, BorderLayout.SOUTH);

        ordenarCodigoButton.addActionListener(e -> {
            Ecommerce.ordenarCatalogoPorCodigo();
            displayCatalogo(Ecommerce.getCatalogo());
        });
        ordenarNombreButton.addActionListener(e -> {
            Ecommerce.ordenarCatalogoPorNombre();
            displayCatalogo(Ecommerce.getCatalogo());
        });
        ordenarPrecioButton.addActionListener(e -> {
            Ecommerce.ordenarCatalogoPorPrecio();
            displayCatalogo(Ecommerce.getCatalogo());
        });
        resetButton.addActionListener(e -> {
            Ecommerce.resetCatalogo();
            
            if (productTable.getRowSorter() != null){
                productTable.getRowSorter().setSortKeys(null);
            }
            categoryFilter.setSelectedIndex(0);
            displayCatalogo(Ecommerce.getCatalogo());
        });
        regresarButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });
        searchButton.addActionListener(e -> {
            String codigo = searchField.getText();
            Producto productoEncontrado = Ecommerce.buscarProductoPorHash(codigo);
            if (productoEncontrado != null) {
                ArrayList<Producto> lista = new ArrayList<>();
                lista.add(productoEncontrado);
                displayCatalogo(lista);
                scrollPanel.setBorder(BorderFactory.createTitledBorder("Resultado de Búsqueda"));
            } else {
                JOptionPane.showMessageDialog(frame, "Producto con código " + codigo + " no encontrado.", "Búsqueda Fallida", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void createAgregarPanel() {
        agregarPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        agregarPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        codigoField = new JTextField(15);
        nombreField = new JTextField(15);
        precioField = new JTextField(15);
        stockField = new JTextField(15);
        categoriaField = new JTextField(15);
        ratingField = new JTextField(15);
        
        JButton guardarButton = new JButton("Guardar Producto");
        JButton regresarButton = new JButton("Regresar");

        agregarPanel.add(new JLabel("Código:"));
        agregarPanel.add(codigoField);
        agregarPanel.add(new JLabel("Nombre:"));
        agregarPanel.add(nombreField);
        agregarPanel.add(new JLabel("Precio:"));
        agregarPanel.add(precioField);
        agregarPanel.add(new JLabel("Stock:"));
        agregarPanel.add(stockField);
        agregarPanel.add(new JLabel("Categoría:"));
        agregarPanel.add(categoriaField);
        agregarPanel.add(new JLabel("Rating:"));
        agregarPanel.add(ratingField);
        agregarPanel.add(guardarButton);
        agregarPanel.add(regresarButton);

        guardarButton.addActionListener(e -> {
            try {
                String codigo = codigoField.getText();
                String nombre = nombreField.getText();
                double precio = Double.parseDouble(precioField.getText());
                int stock = Integer.parseInt(stockField.getText());
                String categoria = categoriaField.getText();
                double rating = Double.parseDouble(ratingField.getText());
                
                if (codigo.length() != 6 || !codigo.matches("\\d+")) {
                    JOptionPane.showMessageDialog(frame, "El código debe tener 6 dígitos numéricos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                boolean agregado = Ecommerce.agregarProducto(new Producto(codigo, nombre, precio, stock, categoria, rating));
                
                if (agregado) {
                    JOptionPane.showMessageDialog(frame, "Producto agregado con éxito!");
                    codigoField.setText("");
                    nombreField.setText("");
                    precioField.setText("");
                    stockField.setText("");
                    categoriaField.setText("");
                } else {
                    JOptionPane.showMessageDialog(frame, "Error: El código de producto ya existe.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(frame, "Precio y Stock deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        regresarButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });
    }
    
    private void displayCatalogo(ArrayList<Producto> listaProductos) {
        tableModel.setRowCount(0); // Limpia la tabla
        for (Producto p : listaProductos) {
            Object[] rowData = {
                p.getCodigo(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock(),
                p.getCategoria(),
                p.getRating()
            };
            tableModel.addRow(rowData);
        }
    }

    private void filtrarPorCategoria() {
        String categoriaSeleccionada = (String) categoryFilter.getSelectedItem();
        ArrayList<Producto> catalogoCompleto = Ecommerce.getCatalogo();
        
        if ("Todas las categorías".equals(categoriaSeleccionada)) {
            displayCatalogo(catalogoCompleto);
        } else {
            ArrayList<Producto> listaFiltrada = new ArrayList<>();
            for (Producto p : catalogoCompleto) {
                if (p.getCategoria().equals(categoriaSeleccionada)) {
                    listaFiltrada.add(p);
                }
            }
            displayCatalogo(listaFiltrada);
        }
    }

    /*public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginGUI());
    }
*/
}
