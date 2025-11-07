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
    private void aplicarLookAndFeel() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.err.println("Error al aplicar Look and Feel: " + e.getMessage());
        }
    }

    /*
    * Inicializa los componentes principales
     */
    private void inicializarComponentes() {
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
    private void configurarPaneles() {
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
    private void mostrarVentana() {
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
        JButton verListadoButton = crearBotonPrincipal("Ver Listado de Productos");

        // Listeners
        agregarProductoButton.addActionListener(e
                -> mostrarPanel("Agregar"));
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
    private JButton crearBotonPrincipal(String texto) {
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

    /*
    * Crea la tabla de productos
    */
    private void crearTablaProductos() {
        String[] columnNames = {"Código", "Nombre", "Precio", "Stock", "Categoría", "Rating"};
    
        tableModel  = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass( int columnIndex) {
                // Para ordernar correctamente
                switch (columnIndex){
                    case 2: // Precio
                    case 5: // Rating
                        return Double.class;
                    case 3: // Stock
                        return Integer.class;
                    default:
                        return String.class;
                }
            }
        };
        
        productTable  = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setRowHeight(25);
        
        // Configurar sorter
        sorter = new TableRowSorter<>(tableModel);
        productTable.setRowSorter(sorter);
        
        // Ajustar anchos de columnas
        productTable.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
        productTable.getColumnModel().getColumn(1).setPreferredWidth(200); // Nombre
        productTable.getColumnModel().getColumn(2).setPreferredWidth(80);  // Precio
        productTable.getColumnModel().getColumn(3).setPreferredWidth(60);  // Stock
        productTable.getColumnModel().getColumn(4).setPreferredWidth(120); // Categoría
        productTable.getColumnModel().getColumn(5).setPreferredWidth(60);  // Rating
    }
    
    /*
    * Crea el panel superior con búsqueda y filtros
    */
    private JPanel crearPanelSuperior(){
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.Y_AXIS));
        
        // Panel de búsqueda y filtros
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("Búsqueda y Filtros"));
        
        searchField = new JTextField(15);
        JButton searchHashButton = new JButton("Buscar Código (HASH)");
        JButton searchContentButton = new JButton("Buscar Texto (Invertida)");
        
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("Todas las categorías");
        actualizarFiltroCategorias();
        
        searchPanel.add(new JLabel("Categoría"));
        searchPanel.add(categoryFilter);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(new JLabel("Búsqueda:"));
        searchPanel.add(searchField);
        searchPanel.add(searchHashButton);
        searchPanel.add(searchContentButton);
        
        // Panel de botones de ordenacion
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortPanel.setBorder(BorderFactory.createTitledBorder("Ordenación"));
        
        JButton ordenarCodigoButton = new  JButton("Código (QuickSort)");
        JButton ordenarRatingButton = new  JButton("Código (SellSort)");
        JButton ordenarNombreButton = new  JButton("Nombre");
        JButton ordenarPrecioButton = new  JButton("Precio");
        JButton ordenarExternaButton = new  JButton("Código (Externa)");
        JButton resetButton = new  JButton("Código (Externa)");
        
        sortPanel.add(ordenarCodigoButton);
        sortPanel.add(ordenarRatingButton);
        sortPanel.add(ordenarNombreButton);
        sortPanel.add(ordenarPrecioButton);
        sortPanel.add(ordenarExternaButton);
        sortPanel.add(resetButton);
        
        // Listeners de búsqueda
        categoryFilter.addActionListener(e -> filtrarPorCategoria());
        searchHashButton.addActionListener(e -> buscarProductoPorCodigo());
        searchContentButton.addActionListener(e -> buscarProductoPorContenido());
        
        // Listeners de ordenación
        ordenarCodigoButton.addActionListener(e -> ordenarPorQuickSort());
        ordenarRatingButton.addActionListener(e -> ordenarPorRatingShellSort());
        ordenarNombreButton.addActionListener(e -> ordenarPorNombre());
        ordenarPrecioButton.addActionListener(e -> ordenarPorPrecio());
        ordenarExternaButton.addActionListener(e -> ejecutarOrdenacionExterna());
        resetButton.addActionListener(e -> resetearCatalogo());
        
        topPanel.add(searchPanel);
        topPanel.add(sortPanel);
        
        return topPanel;
    }
          
    /*
    * Crea el panel inferior con paginación y acciones
    */
    private JPanel crearPanelInferior(){
        JPanel bottomPanel = new JPanel(new BorderLayout());
        
        // Paginación
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        prevButton = new JButton("<< Anterior");
        nextButton = new JButton("Siguiente >>");
        pageStatusLabel = new JLabel("Página 1 de 1");
        
        prevButton.addActionListener(e -> irPaginaAnterior());
        nextButton.addActionListener(e -> irPaginaSiguiente());

        paginationPanel.add(prevButton);
        paginationPanel.add(pageStatusLabel);
        paginationPanel.add(nextButton);
        
        // Acciones
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton regresarButton = new JButton("Regresar");
        JButton addToCartButton = new JButton("➕ Añadir al Carrito");
        JButton viewCartButton = new JButton("🛒 Ver Carrito (" + carrito.contarItems() + ")");
        JButton logoutButton = new JButton("➡️ Cerrar Sesión");
        
        logoutButton.setForeground(Color.RED);

        regresarButton.addActionListener(e -> mostrarPanel("Main"));
        addToCartButton.addActionListener(e -> añadirAlCarrito());
        viewCartButton.addActionListener(e -> mostrarVentanaCarrito());
        logoutButton.addActionListener(e -> cerrarSesion());
        
        actionPanel.add(regresarButton);
        actionPanel.add(Box.createHorizontalStrut(30));
        actionPanel.add(addToCartButton);
        actionPanel.add(viewCartButton);
        actionPanel.add(Box.createHorizontalGlue());
        actionPanel.add(logoutButton);

        bottomPanel.add(paginationPanel, BorderLayout.CENTER);
        bottomPanel.add(actionPanel, BorderLayout.SOUTH);
        
        return bottomPanel;
    }

    // ACTUALIZACIÓN DE VISTA
    /*
    * Actualiza la vista de productos con paginación
    */
    private void updateProductView(){
        ArrayList<Producto> listaPagina = Ecommerce.getPagina(paginaActual);
        displayCatalogo(listaPagina);
        
        int totalPaginas = Ecommerce.getTotalPaginas();
        pageStatusLabel.setText("Página "+ paginaActual + " de " + totalPaginas);
    }
    
    /*
    * Muestra una lista de productos en la tabla
    */
    private void displaycatalogo(List<Producto> listaProductos){
        tableModel.setRowCount(0);

        for (Producto p : listaProductos){
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

    /*
    * Actualiza el filtro de categorías
    */
    private void actualizarFiltroCategorias() {
        String seleccionActual = (String) categoryFilter.getSelectedItem();
        categoryFilter.removeAllItems();
        categoryFilter.addItem("Todas las categorías");
        
        for (String categoria : Ecommerce.getCategoriasUnicas()){
            categoryFilter.addItem(categoria);
        }
        
        if (seleccionActual != null){
            categoryFilter.setSelectedItem(seleccionActual);
        }    
    }

    // BÚSQUEDAS
    /*
    * Busca producto por código usando Has
    */
    private void buscarProductoPorCodigo() {
        String codigo = searchField.getText().trim();

        if (codigo.isEmpty()) {
            mostrarAdvertencia("Ingrese un código para buscar");
            return;
        }

        Producto productoEncontrado = Ecommerce.buscarProductoPorHash(codigo);

        if (productoEncontrado != null) {
            List<Producto> lista = new ArrayList<>();
            lista.add(productoEncontrado);

            resetTableSorter();
            displayCatalogo(lista);

            pageStatusLabel.setText("Resultado (1 ítem)");
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
        } else {
            mostrarInformacion("Producto no encontrado",
                    "No se encontró ningún producto con el código: " + codigo);
        }
    }

    /*
     * Busca productos por contenido usando Lista Invertida
     */
    private void buscarProductoPorContenido() {
        String palabra = searchField.getText().trim();

        if (palabra.isEmpty()) {
            mostrarAdvertencia("Ingrese una palabra clave para buscar");
            return;
        }

        ArrayList<Producto> resultados = Ecommerce.buscarPorPalabraClave(palabra);

        if (!resultados.isEmpty()) {
            resetTableSorter();
            displayCatalogo(resultados);

            pageStatusLabel.setText("Resultados: " + resultados.size() + " ítems");
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
        } else {
            mostrarInformacion("Sin resultados",
                    "No se encontraron productos que contengan: '" + palabra + "'");
        }
    }
    
    /*
     * Filtra productos por categoría
     */
    private void filtrarPorCategoria() {
        String categoriaSeleccionada = (String) categoryFilter.getSelectedItem();
        
        resetTableSorter();
        
        if ("Todas las categorías".equals(categoriaSeleccionada)) {
            paginaActual = 1;
            updateProductView();
        } else {
            ArrayList<Producto> catalogoCompleto = Ecommerce.getCatalogo();
            List<Producto> listaFiltrada = catalogoCompleto.stream()
                .filter(p -> p.getCategoria().equals(categoriaSeleccionada))
                .collect(Collectors.toList());
            
            displayCatalogo(listaFiltrada);
            
            pageStatusLabel.setText("Filtro: " + listaFiltrada.size() + " ítems");
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
        }
    }
    
    // Ordenación
    /*
     * Ordena por código usando QuickSort
     */
    private void ordenarPorQuickSort() {
        ArrayList<Producto> catalogo = Ecommerce.getCatalogo();
        QuickSort.ordenar(catalogo);
        Ecommerce.setCatalogo(catalogo);
        resetTableSorter();
        paginaActual = 1;
        updateProductView();
        mostrarExito("Catálogo ordenado por código (QuickSort)");
    }
    
    /*
     * Ordena por rating usando ShellSort
     */
    private void ordenarPorRatingShellSort() {
        ArrayList<Producto> catalogo = Ecommerce.getCatalogo();
        ShellSort.ordenarPorRating(catalogo);
        Ecommerce.setCatalogo(catalogo);
        resetTableSorter();
        paginaActual = 1;
        updateProductView();
        mostrarExito("Catálogo ordenado por rating (ShellSort)");
    }
    
    /*
     * Ordena por nombre
     */
    private void ordenarPorNombre() {
        Ecommerce.ordenarCatalogoPorNombre();
        resetTableSorter();
        paginaActual = 1;
        updateProductView();
        mostrarExito("Catálogo ordenado por nombre");
    }
    
    /*
     * Ordena por precio
     */
    private void ordenarPorPrecio() {
        Ecommerce.ordenarCatalogoPorPrecio();
        resetTableSorter();
        paginaActual = 1;
        updateProductView();
        mostrarExito("Catálogo ordenado por precio");
    }
    
    /*
     * Ejecuta ordenación externa
     */
    private void ejecutarOrdenacionExterna() {
        try {
            // Crear archivo de entrada
            File inputFile = new File("catalogo_entrada.txt");
            try (FileWriter writer = new FileWriter(inputFile)) {
                for (Producto p : Ecommerce.getCatalogo()) {
                    writer.write(p.toFileString());
                    writer.write(System.lineSeparator());
                }
            }
            
            // Ejecutar ordenación externa
            File outputFile = new File("catalogo_salida_ordenada.txt");
            OrdenacionExterna.ordenar(inputFile, outputFile);
            
            // Cargar catálogo ordenado
            ArrayList<Producto> catalogoOrdenado = Ecommerce.cargarCatalogoDesdeArchivo(outputFile);
            Ecommerce.setCatalogo(catalogoOrdenado);
            
            resetTableSorter();
            paginaActual = 1;
            updateProductView();
            
            mostrarExito("Ordenación Externa completada.\nResultado guardado en: catalogo_salida_ordenada.txt");
            
            // Limpiar archivos temporales
            inputFile.delete();
            
        } catch (IOException ex) {
            mostrarError("Error durante la ordenación externa: " + ex.getMessage());
        }
    }
    
    /*
     * Resetea el catálogo a su estado original
     */
    private void resetearCatalogo() {
        int confirm = JOptionPane.showConfirmDialog(frame,
            "¿Está seguro de resetear el catálogo?\nSe perderán los cambios no guardados.",
            "Confirmar Reset",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            Ecommerce.resetCatalogo();
            actualizarFiltroCategorias();
            resetTableSorter();
            paginaActual = 1;
            updateProductView();
            mostrarExito("Catálogo reseteado exitosamente");
        }
    }
    
    /*
     * Resetea el ordenador de la tabla
     */
    private void resetTableSorter() {
        if (sorter != null) {
            sorter.setSortKeys(null);
        }
    }
}
