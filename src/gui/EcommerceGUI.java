package gui;

import domain.*;
import estructuras.*;
import ordenamiento.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Interfaz gráfica principal del sistema E-commerce
 */

public class EcommerceGUI {
    
    // COMPONENTES PRINCIPALES
    private JFrame frame;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    
    // PANELES
    private JPanel mainPanel, listadoPanel, agregarPanel;
    
    // CONTROLES DE BÚSQUEDA Y FILTROS
    private JComboBox<String> categoryFilter;
    private JTextField searchField;

    // CAMPOS DE FOMULARIO
    private JTextField codigoField, nombreField, precioField, stockField, categoriaField, ratingField;
    
    // PAGINACIÓN
    private int paginaActual = 1;
    private JLabel pageStatusLabel;
    private JButton prevButton, nextButton;
    
    // CARRITO DE COMPRAS
    private CarritoDeCompras carrito;
    
    // CONSTANTES
    private static final String TITULO_APP = "Plataforma E-commerce";
    private static final int ANCHO_VENTANA = 1000;
    private static final int ALTO_VENTANA = 690;
    
    // CONSTRUCTOR
    
    /*
    * Constructor principal - Inicializa la interfaz gráfica
    */
    public EcommerceGUI() {
        aplicarLookAndFeel();
        inicializarComponentes();
        crearVentanaPrincipal();
        configurarPaneles();
        mostrarVentana();
    }

    // INICIALIZACIÓN

    /*
    * Aplica el Look and Feel del sistema operativo
    */
    private void aplicarLookAndFeel(){ 
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error al aplicar Look and Feel: " + e.getMessage());
        }
    }
    
    /*
    * Inicializa los componentes principales
    */
    private void inicializarComponentes(){
        carrito = new CarritoDeCompras();
    }

    /*
    * Crea la ventana principal
    */
    private void crearVentanaPrincipal() {
        frame = new JFrame(TITULO_APP);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(ANCHO_VENTANA, ALTO_VENTANA);
        frame.setLocationRelativeTo(null);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new CardLayout()); 
    }

    /*
    * Configura todos los paneles
    */
    private void configurarPaneles(){
        createMainPanel();
        createListadoPanel();
        createAgregarPanel();

        frame.add(mainPanel, "Main");
        frame.add(listadoPanel, "Listado");
        frame.add(agregarPanel, "Agregar");

    }
    
    /*
    * Muestra la ventana
    */
    private void mostrarVentana(){
        frame.setVisible(true);
    }
    
    // PANEL PRINCIPAL (MENÚ)
    
    /*
    * Crea el panel principal con el menú de opciones
    */
    private void createMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));
        
        // TITULO
        JLabel titleLabel = new JLabel("Sustema E-commerce");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Botones principales
        JButton agregarProductoButton = crearBotonPrincipal("Agregar Producto");
        JButton verListadoButton =crearBotonPrincipal("Ver Listado de Productos");
        
        // Listeners
        agregarProductoButton.addActionListener(e ->
            mostrarPanel("Agregar"));
        verListadoButton.addActionListener(e -> {
            paginaActual = 1;
            updateProductView();
            mostrarPanel("Listado");
        });
        
        // Agregar componentes
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(agregarProductoButton);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(verListadoButton);
    }

    /*
    * Crea un botón estilizado para el menú principal
    */
    private JButton crearBotonPrincipal(String texto){
        JButton button = new JButton(texto);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setPreferredSize(new Dimension(250, 40));
        button.setMaximumSize(new Dimension(250, 40));
        return button;
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
    
    // PANEL DE LISTADO DE PRODUCTOS
    
    /*
    * Crea el panel principal de listado de productos
    */
    private void createListadoPanel() {
        listadoPanel = new JPanel(new BorderLayout(10, 10));
        listadoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Tabla de productos
        crearTablaProductos();
        
        // Panel superior: búsqueda, filtros y botones
        JPanel topPanel = crearPanelSuperior();
        
        // Panel inferior: paginación y acciones
        JPanel bottomPanel = crearPanelInferior();
        
        // Agregar componentes
        listadoPanel.add(topPanel, BorderLayout.NORTH);
        listadoPanel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        listadoPanel.add(bottomPanel, BorderLayout.SOUTH);
    }
    
    //AVANCE DE LIMPIEZA DE CÓDIGO

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

        JPanel searchAndFilterPanel = new JPanel(new FlowLayout());
        searchField = new JTextField(15);
        JButton searchHashButton = new JButton("Buscar Código (HASH)");
        JButton searchContentButton = new JButton("Buscar Contenido (Invertida)");
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("Todas las categorías");
        Ecommerce.getCategoriasUnicas().forEach(categoryFilter::addItem);

        searchAndFilterPanel.add(new JLabel("Filtrar por:"));
        searchAndFilterPanel.add(categoryFilter);
        searchAndFilterPanel.add(searchField);
        searchAndFilterPanel.add(searchHashButton);
        searchAndFilterPanel.add(searchContentButton);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        
        JButton ordenarCodigoButton = new JButton("Ordenar Código (QuickSort)"); 
        JButton ordenarRatingShellSortButton = new JButton("Ordenar por Rating (ShellSort)"); 
        JButton ordenarNombreButton = new JButton("Ordenar por Nombre");
        JButton ordenarPrecioButton = new JButton("Ordenar por Precio");
        JButton ordenarExternaButton = new JButton("Ordenar Código (Externa)");
        JButton resetButton = new JButton("Resetear Catálogo");

        buttonPanel.add(ordenarCodigoButton);
        buttonPanel.add(ordenarRatingShellSortButton);
        buttonPanel.add(ordenarNombreButton); 
        buttonPanel.add(ordenarPrecioButton);
        buttonPanel.add(ordenarExternaButton); 
        buttonPanel.add(resetButton);

        JPanel topControls = new JPanel(new GridLayout(2, 1));
        topControls.add(searchAndFilterPanel);
        topControls.add(buttonPanel);

        listadoPanel.add(topControls, BorderLayout.NORTH);
        listadoPanel.add(scrollPanel, BorderLayout.CENTER);

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

        JButton logoutButton = new JButton("➡️ Cerrar Sesión");
        logoutButton.setForeground(Color.RED);

        actionPanel.add(regresarButton);
        actionPanel.add(Box.createRigidArea(new Dimension(50, 0)));
        actionPanel.add(addToCartButton);
        actionPanel.add(viewCartButton);
        actionPanel.add(Box.createHorizontalGlue());
        actionPanel.add(logoutButton);

        bottomControlPanel.add(paginationPanel, BorderLayout.CENTER);
        bottomControlPanel.add(actionPanel, BorderLayout.SOUTH);
        listadoPanel.add(bottomControlPanel, BorderLayout.SOUTH);

        // Ordenación
        ordenarCodigoButton.addActionListener(e -> { 
            ArrayList<Producto> catalogo = Ecommerce.getCatalogo();
            QuickSort.ordenar(catalogo); 
            Ecommerce.setCatalogo(catalogo); 
            paginaActual = 1; 
            updateProductView(); 
        });

        ordenarRatingShellSortButton.addActionListener(e -> {
            ArrayList<Producto> catalogo = Ecommerce.getCatalogo();
            ShellSort.ordenarPorRating(catalogo);
            Ecommerce.setCatalogo(catalogo);
            resetTableSorter();
            paginaActual = 1;
            updateProductView();
        });
        
        ordenarNombreButton.addActionListener(e -> { 
            Ecommerce.ordenarCatalogoPorNombre(); 
            paginaActual = 1; 
            updateProductView(); 
        });
        
        ordenarPrecioButton.addActionListener(e -> { 
            Ecommerce.ordenarCatalogoPorPrecio(); 
            paginaActual = 1; 
            updateProductView(); 
        });
        
        ordenarExternaButton.addActionListener(e -> ejecutarOrdenacionExterna());
        
        resetButton.addActionListener(e -> { 
            Ecommerce.resetCatalogo(); 
            paginaActual = 1; 
            updateProductView(); 
        });
        
        categoryFilter.addActionListener(e -> filtrarPorCategoria());
        searchHashButton.addActionListener(e -> buscarProductoPorCodigo());
        searchContentButton.addActionListener(e -> buscarProductoPorContenido());
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
        
        logoutButton.addActionListener(e -> {
            int confirm = JOptionPane.showConfirmDialog(frame, "¿Está seguro que desea cerrar la sesión?", 
                                                        "Confirmar Cierre", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                frame.dispose(); 
                new LoginGUI(); 
            }
        });

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
    
    // CARRITO DE COMPRAS
    
    /*
    * Añade un producto seleccionado al carrito
    */
    private void añadirAlCarrito() {
        int selectedRow = productTable.getSelectedRow();
        if (selectedRow == -1) {
            mostrarAdvertencia("Seleccione un producto de la tabla");
            return;
        }

        try {
            int modelRow = productTable.convertRowIndexToModel(selectedRow);
            String codigo = (String) tableModel.getValueAt(modelRow, 0);

            Producto producto = Ecommerce.buscarProductoPorHash(codigo);

            if (producto == null) {
                mostrarError("Producto no encontrado")
                return;
            }

            // Solicitar Cantidad
            String cantidadStr = JOptionPane.showInputDialog(frame,
                    "Ingrese la cantidad de '" + producto.getNombre() + "':",
                    "Cantidad",
                    JOptionPane.QUESTION_MESSAGE);

            if (cantidadStr == null) {
                return; // Usuario canceló
            }

            int cantidad = Integer.parseInt(cantidadStr.trim());

            if (cantidad <= 0) {
                mostrarError("La cantidad debe ser mayor a cero");
                return;
            }

            // Agregar al carrito
            if (carrito.agregarProducto(producto, cantidad)) {
                mostrarExito(cantidad + "x " + producto.getNombre() + " añadido al carrito");
                actualizarContadorCarrito();
            } else {
                mostrarError("Stock insuficiente.\nDisponible: " + producto.getStock()
                        + "\nEn carrito: " + carrito.obtenerCantidad(codigo)
                        + "\nSolicitado: " + cantidad);
            }

        } catch (NumberFormatException ex) {
            mostrarError("Cantidad inválida. Ingrese un número entero");
        } catch (Exception ex) {
            mostrarError("Error al añadir al carrito: " + ex.getMessage());
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
