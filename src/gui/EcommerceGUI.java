package gui;

import negocio.*;
import estructuras.*;
import negocio.Producto;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;
import java.util.stream.Collectors;

public class EcommerceGUI {

    private JFrame frame;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryFilter;
    private JPanel mainPanel, listadoPanel, agregarPanel;
    private JTextField codigoField, nombreField, precioField, stockField, categoriaField, ratingField, searchField;
    private int paginaActual = 1;
    private JLabel pageStatusLabel;
    private JButton prevButton, nextButton;
    private CarritoDeCompras carrito; 

    public EcommerceGUI() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        carrito = new CarritoDeCompras(); // Inicialización del Carrito

        frame = new JFrame("Plataforma E-commerce");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1000, 690);
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
        
        String[] columnNames = {"Código", "Nombre", "Precio", "Stock", "Categoría", "Rating"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        productTable = new JTable(tableModel);
        
        JScrollPane scrollPanel = new JScrollPane(productTable);
        
        // Simulación de inicialización de controles
        searchField = new JTextField(15);
        JButton searchHashButton = new JButton("Buscar Código (HASH)"); 
        JButton searchContentButton = new JButton("Buscar Contenido (Invertida)");
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("Todas las categorías");
        Ecommerce.getCategoriasUnicas().forEach(categoryFilter::addItem);
        JButton ordenarCodigoButton = new JButton("Ordenar Código (Interna)");
        JButton ordenarExternaButton = new JButton("Ordenar Código (Externa)");
        JButton resetButton = new JButton("Resetear Catálogo");
        JButton regresarButton = new JButton("Regresar");
        JButton addToCartButton = new JButton("➕ Añadir al Carrito"); 
        JButton viewCartButton = new JButton("🛒 Ver Carrito");

        // Panel de Controles Superiores (Simulación)
        JPanel topControls = new JPanel(new BorderLayout());
        JPanel searchPanel = new JPanel(new FlowLayout());
        searchPanel.add(categoryFilter);
        searchPanel.add(searchField);
        searchPanel.add(searchHashButton);
        searchPanel.add(searchContentButton);
        JPanel sortPanel = new JPanel(new FlowLayout());
        sortPanel.add(ordenarCodigoButton);
        sortPanel.add(ordenarExternaButton);
        sortPanel.add(resetButton);
        topControls.add(searchPanel, BorderLayout.NORTH);
        topControls.add(sortPanel, BorderLayout.SOUTH);

        listadoPanel.add(topControls, BorderLayout.NORTH);
        listadoPanel.add(scrollPanel, BorderLayout.CENTER);
        
        // Controles Inferiores (Paginación, Carrito, LogOut)
        JPanel bottomControlPanel = new JPanel(new BorderLayout());
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        prevButton = new JButton("<< Anterior");
        nextButton = new JButton("Siguiente >>");
        pageStatusLabel = new JLabel("Página 1 de 1");
        
        paginationPanel.add(prevButton);
        paginationPanel.add(pageStatusLabel);
        paginationPanel.add(nextButton);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        // Botones de acción existentes
        actionPanel.add(regresarButton);
        actionPanel.add(Box.createRigidArea(new Dimension(150, 0)));
        actionPanel.add(addToCartButton);
        actionPanel.add(viewCartButton);
        
        // --- 🔴 Implementación del Botón de Cierre de Sesión (NUEVO) ---
        JButton logoutButton = new JButton("➡️ Cerrar Sesión"); 
        logoutButton.setForeground(Color.RED);
        actionPanel.add(logoutButton); // Añadir el botón
        
        bottomControlPanel.add(paginationPanel, BorderLayout.CENTER);
        bottomControlPanel.add(actionPanel, BorderLayout.SOUTH);
        listadoPanel.add(bottomControlPanel, BorderLayout.SOUTH);

        // --- Listeners de Cierre de Sesión ---
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "¿Está seguro que desea cerrar la sesión?", 
                                                        "Confirmar Cierre", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose(); // Cierra la ventana principal
                new LoginGUI(); // Vuelve a abrir la ventana de Login
            }
        });
    }

    private void createListadoPanel() {
        listadoPanel = new JPanel(new BorderLayout());

        String[] columnNames = {"Código", "Nombre", "Precio", "Stock", "Categoría", "Rating"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        productTable = new JTable(tableModel);
        productTable.setAutoCreateRowSorter(true);

        JScrollPane scrollPanel = new JScrollPane(productTable);
        scrollPanel.setBorder(BorderFactory.createTitledBorder("Listado de Productos"));

        JPanel controlPanel = new JPanel(new BorderLayout());
        
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        JButton ordenarCodigoButton = new JButton("Ordenar Código (Inserción)");
        JButton ordenarNombreButton = new JButton("Ordenar por Nombre");
        JButton ordenarPrecioButton = new JButton("Ordenar por Precio");
        JButton ordenarExternaButton = new JButton("Ordenar Código (Externa)");
        JButton resetButton = new JButton("Resetear");
        
        JPanel searchAndFilterPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(15);
        
        JButton searchHashButton = new JButton("Buscar Código (HASH)");
        JButton searchContentButton = new JButton("Buscar Contenido (Invertida)");
        
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("Todas las categorías");
        for (String categoria : Ecommerce.getCategoriasUnicas()) {
            categoryFilter.addItem(categoria);
        }
        
        searchAndFilterPanel.add(new JLabel("Filtrar por:"));
        searchAndFilterPanel.add(categoryFilter);
        searchAndFilterPanel.add(Box.createRigidArea(new Dimension(20, 0)));
        searchAndFilterPanel.add(searchField);
        searchAndFilterPanel.add(searchHashButton);
        searchAndFilterPanel.add(searchContentButton);

        buttonPanel.add(ordenarCodigoButton);
        buttonPanel.add(ordenarNombreButton);
        buttonPanel.add(ordenarPrecioButton);
        buttonPanel.add(ordenarExternaButton);
        buttonPanel.add(resetButton);
        
        JPanel bottomControlPanel = new JPanel(new BorderLayout());
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        prevButton = new JButton("<< Anterior");
        nextButton = new JButton("Siguiente >>");
        pageStatusLabel = new JLabel("Página 1 de 1");
        
        paginationPanel.add(prevButton);
        paginationPanel.add(pageStatusLabel);
        paginationPanel.add(nextButton);
        
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton regresarButton = new JButton("Regresar");
        
        JButton addToCartButton = new JButton("➕ Añadir al Carrito");
        JButton viewCartButton = new JButton("🛒 Ver Carrito");
        
        actionPanel.add(regresarButton);
        actionPanel.add(Box.createRigidArea(new Dimension(150, 0)));
        actionPanel.add(addToCartButton);
        actionPanel.add(viewCartButton);
        
        bottomControlPanel.add(paginationPanel, BorderLayout.CENTER);
        bottomControlPanel.add(actionPanel, BorderLayout.SOUTH);
        
        JPanel topControls = new JPanel(new GridLayout(2, 1));
        topControls.add(searchAndFilterPanel);
        topControls.add(buttonPanel);
        
        controlPanel.add(topControls, BorderLayout.NORTH);
        
        listadoPanel.add(controlPanel, BorderLayout.NORTH);
        listadoPanel.add(scrollPanel, BorderLayout.CENTER);
        listadoPanel.add(bottomControlPanel, BorderLayout.SOUTH);

        // Ordenación
        ordenarCodigoButton.addActionListener(e -> {
            Ecommerce.ordenarCatalogoPorCodigo();
            paginaActual = 1;
            resetTableSorter();
            updateProductView();
        });
        ordenarNombreButton.addActionListener(e -> {
            Ecommerce.ordenarCatalogoPorNombre();
            paginaActual = 1;
            resetTableSorter();
            updateProductView();
        });
        ordenarPrecioButton.addActionListener(e -> {
            Ecommerce.ordenarCatalogoPorPrecio();
            paginaActual = 1;
            resetTableSorter();
            updateProductView();
        });
        ordenarExternaButton.addActionListener(e -> ejecutarOrdenacionExterna());
        
        resetButton.addActionListener(e -> {
            Ecommerce.resetCatalogo();
            paginaActual = 1; 
            resetTableSorter();
            categoryFilter.setSelectedIndex(0);
            updateProductView(); 
            scrollPanel.setBorder(BorderFactory.createTitledBorder("Listado de Productos")); 
        });
        categoryFilter.addActionListener(e -> filtrarPorCategoria());
        
        searchHashButton.addActionListener(e -> buscarProductoPorCodigo());
        searchContentButton.addActionListener(e -> buscarProductoPorContenido());

        // Paginación
        prevButton.addActionListener(e -> {
            if (paginaActual > 1) {
                paginaActual--;
                updateProductView();
            }
        });
        nextButton.addActionListener(e -> {
            if (paginaActual < Ecommerce.getTotalPaginas()) {
                paginaActual++;
                updateProductView();
            }
        });
        
        regresarButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });
        
        addToCartButton.addActionListener(e -> añadirAlCarrito());
        viewCartButton.addActionListener(e -> mostrarVentanaCarrito());
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
        agregarPanel.add(new JLabel("Rating (0.0 a 5.0):")); 
        agregarPanel.add(ratingField);                      
        agregarPanel.add(guardarButton);
        agregarPanel.add(regresarButton);

        guardarButton.addActionListener(e -> guardarNuevoProducto());
        regresarButton.addActionListener(e -> {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });
    }
    
    private void guardarNuevoProducto() {
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
            if (rating < 0.0 || rating > 5.0) {
                JOptionPane.showMessageDialog(frame, "El Rating debe estar entre 0.0 y 5.0.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                return;
            }

            Producto nuevoProducto = new Producto(codigo, nombre, precio, stock, categoria, rating);
            boolean agregado = Ecommerce.agregarProducto(nuevoProducto);
            
            if (agregado) {
                JOptionPane.showMessageDialog(frame, "Producto agregado con éxito!");
                // Limpiar campos
                codigoField.setText("");
                nombreField.setText("");
                precioField.setText("");
                stockField.setText("");
                categoriaField.setText("");
                ratingField.setText(""); 
            } else {
                JOptionPane.showMessageDialog(frame, "Error: El código de producto ya existe.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(frame, "Precio, Stock y Rating deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateProductView() {
        ArrayList<Producto> listaPagina = Ecommerce.getPagina(paginaActual);
        displayCatalogo(listaPagina); 
        
        int totalPaginas = Ecommerce.getTotalPaginas();
        pageStatusLabel.setText("Página " + paginaActual + " de " + totalPaginas);
        
        prevButton.setEnabled(paginaActual > 1);
        nextButton.setEnabled(paginaActual < totalPaginas);
    }
    
    private void displayCatalogo(ArrayList<Producto> listaProductos) {
        tableModel.setRowCount(0);
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

    private void resetTableSorter() {
        if (productTable.getRowSorter() != null) {
            productTable.getRowSorter().setSortKeys(null);
        }
    }
    
    private void filtrarPorCategoria() {
        String categoriaSeleccionada = (String) categoryFilter.getSelectedItem();
        
        resetTableSorter();
        
        if ("Todas las categorías".equals(categoriaSeleccionada)) {
            paginaActual = 1;
            updateProductView();
        } else {
            ArrayList<Producto> catalogoCompleto = Ecommerce.getCatalogo();
            ArrayList<Producto> listaFiltrada = catalogoCompleto.stream()
                .filter(p -> p.getCategoria().equals(categoriaSeleccionada))
                .collect(Collectors.toCollection(ArrayList::new));
                
            displayCatalogo(listaFiltrada);
            
            pageStatusLabel.setText("Filtro Activo (" + listaFiltrada.size() + " ítems)");
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
        }
    }
    
    //Búsqueda por Código (HASH)
    private void buscarProductoPorCodigo() {
        String codigo = searchField.getText();
        Producto productoEncontrado = Ecommerce.buscarProductoPorHash(codigo);
        
        if (productoEncontrado != null) {
            ArrayList<Producto> lista = new ArrayList<>();
            lista.add(productoEncontrado);
            
            resetTableSorter();
            displayCatalogo(lista);
            
            pageStatusLabel.setText("Resultado (1 ítem)");
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
        } else {
            JOptionPane.showMessageDialog(frame, "Producto con código " + codigo + " no encontrado.", "Búsqueda Fallida", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    //Búsqueda por Contenido (Lista Invertida)
    private void buscarProductoPorContenido() {
        String palabra = searchField.getText();
        if (palabra.trim().isEmpty()) {
            JOptionPane.showMessageDialog(frame, "Ingrese una palabra clave para buscar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        ArrayList<Producto> resultados = Ecommerce.buscarPorPalabraClave(palabra);
        
        if (!resultados.isEmpty()) {
            resetTableSorter();
            displayCatalogo(resultados);
            
            pageStatusLabel.setText("Resultados (Lista Invertida: " + resultados.size() + " ítems)");
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
            
        } else {
            JOptionPane.showMessageDialog(frame, "No se encontraron productos que contengan '" + palabra + "'.", "Búsqueda Fallida", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    private void ejecutarOrdenacionExterna() {
        try {
            File inputFile = new File("catalogo_entrada.txt");
            try (FileWriter writer = new FileWriter(inputFile)) {
                for (Producto p : Ecommerce.getCatalogo()) {
                    writer.write(p.toFileString());
                    writer.write(System.lineSeparator());
                }
            }

            File outputFile = new File("catalogo_salida_ordenada.txt");
            OrdenacionExterna.ordenar(inputFile, outputFile);

            ArrayList<Producto> catalogoOrdenado = Ecommerce.cargarCatalogoDesdeArchivo(outputFile);
            
            Ecommerce.setCatalogo(catalogoOrdenado);
            
            resetTableSorter();
            paginaActual = 1; 
            updateProductView();
            
            JOptionPane.showMessageDialog(frame, 
                    "Ordenación Externa completada con éxito. El resultado se ha cargado en memoria y guardado en: catalogo_salida_ordenada.txt", 
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            inputFile.delete();
            
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(frame, "Error durante la Ordenación Externa: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void añadirAlCarrito() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(frame, "Seleccione un producto de la tabla para añadirlo al carrito.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            String codigo = productTable.getValueAt(selectedRow, 0).toString();
            
            Producto producto = Ecommerce.buscarProductoPorHash(codigo);
            
            if (producto == null) return; 

            String cantidadStr = JOptionPane.showInputDialog(frame, "Ingrese la cantidad de " + producto.getNombre() + ":", "Cantidad", JOptionPane.QUESTION_MESSAGE);
            if (cantidadStr == null) return; // Cancelado

            int cantidad = Integer.parseInt(cantidadStr);

            if (cantidad <= 0) {
                 JOptionPane.showMessageDialog(frame, "La cantidad debe ser mayor a cero.", "Error", JOptionPane.ERROR_MESSAGE);
                 return;
            }

            if (carrito.agregarProducto(producto, cantidad)) {
                JOptionPane.showMessageDialog(frame, cantidad + "x " + producto.getNombre() + " añadido al carrito.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(frame, "Stock insuficiente en el carrito. Stock disponible: " + producto.getStock(), "Error de Stock", JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(frame, "Cantidad inválida.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void mostrarVentanaCarrito() {
        JDialog dialog = new JDialog(frame, "Carrito de Compras", true);
        dialog.setSize(550, 400);
        dialog.setLayout(new BorderLayout());

        String[] columnNames = {"Código", "Nombre", "Precio Unitario", "Cantidad", "Subtotal"};
        DefaultTableModel cartTableModel = new DefaultTableModel(columnNames, 0);
        JTable cartTable = new JTable(cartTableModel);
        
        JLabel totalLabel = new JLabel("Total: S/.0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 14));
        
        Runnable refreshCartView = () -> {
            cartTableModel.setRowCount(0);
            double total = 0.0;
            
            for (Map.Entry<Producto, Integer> entry : carrito.getItems().entrySet()) {
                Producto p = entry.getKey();
                int cantidad = entry.getValue();
                double subtotal = p.getPrecio() * cantidad;
                total += subtotal;
                
                Object[] rowData = {
                    p.getCodigo(),
                    p.getNombre(),
                    String.format("S/.%.2f", p.getPrecio()),
                    cantidad,
                    String.format("S/.%.2f", subtotal)
                };
                cartTableModel.addRow(rowData);
            }
            totalLabel.setText(String.format("Total: S/.%.2f", total));
        };

        refreshCartView.run();

        JButton checkoutButton = new JButton("Finalizar Compra");
        JButton removeButton = new JButton("Remover Ítem Seleccionado");
        
        checkoutButton.addActionListener(e -> {
            if (carrito.calcularTotal() > 0) {
                if (carrito.checkout()) {
                    JOptionPane.showMessageDialog(dialog, "Compra exitosa. Stock actualizado en el catálogo.", "Checkout", JOptionPane.INFORMATION_MESSAGE);
                    dialog.dispose();
                    updateProductView();
                } else {
                     JOptionPane.showMessageDialog(dialog, "Error en el checkout. Revise el stock.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(dialog, "El carrito está vacío.", "Advertencia", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        removeButton.addActionListener(e -> {
            int row = cartTable.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(dialog, "Seleccione un ítem para remover.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            try {
                String codigo = cartTable.getValueAt(row, 0).toString();
                Producto productoParaRemover = Ecommerce.buscarProductoPorHash(codigo);
                
                if (productoParaRemover != null) {
                    carrito.removerProducto(productoParaRemover, 0);
                    refreshCartView.run();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(dialog, "Error al remover el producto.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        JPanel southPanel = new JPanel(new BorderLayout());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(removeButton);
        buttonPanel.add(checkoutButton);
        
        southPanel.add(totalLabel, BorderLayout.WEST);
        southPanel.add(buttonPanel, BorderLayout.EAST);
        southPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        dialog.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        dialog.add(southPanel, BorderLayout.SOUTH);
        dialog.setLocationRelativeTo(frame);
        dialog.setVisible(true);
    }
}