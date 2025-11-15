package ui.view;

import ui.view.LoginView;
import service.CatalogoService;
import algoritmos.ordenamiento.ShellSort;
import domain.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * Interfaz gráfica principal del sistema E-commerce
 */
public class CatalogoView {

    // COMPONENTES PRINCIPALES
    private JFrame frame;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    // PANELES
    private JPanel listadoPanel;

    // CONTROLES DE BÚSQUEDA Y FILTROS
    private JComboBox<String> categoryFilter;
    private JTextField searchField;

    // PAGINACIÓN
    private int paginaActual = 1;
    private JLabel pageStatusLabel;
    private JButton prevButton, nextButton;

    // CARRITO DE COMPRAS
    private CarritoDeCompras carrito;
    private JButton viewCartButton;

    // CONSTANTES
    private static final String TITULO_APP = "Plataforma E-commerce";
    private static final int ANCHO_VENTANA = 1000;
    private static final int ALTO_VENTANA = 690;

    // CONSTRUCTOR
    /*
    * Constructor principal - Inicializa la interfaz gráfica
     */
    public CatalogoView() {
        aplicarLookAndFeel();
        inicializarComponentes();
        crearVentanaPrincipal();
        crearCatalogoPrincipal();
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
    
    private void crearCatalogoPrincipal() {
        createListadoPanel();
        frame.add(listadoPanel, BorderLayout.CENTER);
        paginaActual = 1;
        updateProductView();
    }
    
    /*
    * Muestra la ventana
     */
    private void mostrarVentana() {
        frame.setVisible(true);
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
        
        searchField = new JTextField(20);
        JButton searchHashButton = new JButton("🔍 Buscar por Código");
        JButton searchContentButton = new JButton("📝 Buscar por Texto");
        JButton clearSearchButton = new JButton("🔄 Limpiar");
        
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("Todas las categorías");
        actualizarFiltroCategorias();
        
        searchPanel.add(new JLabel("Categoría"));
        searchPanel.add(categoryFilter);
        searchPanel.add(Box.createHorizontalStrut(20));
        searchPanel.add(searchField);
        searchPanel.add(searchHashButton);
        searchPanel.add(searchContentButton);
        searchPanel.add(clearSearchButton);
        
        // Panel de botones de ordenacion
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortPanel.setBorder(BorderFactory.createTitledBorder("Ordenar por"));
        
        JButton ordenarNombreButton = new JButton("📄 Nombre");
        JButton ordenarPrecioButton = new JButton("💰 Precio");
        JButton ordenarRatingButton = new JButton("⭐ Rating");
        
        sortPanel.add(ordenarRatingButton);
        sortPanel.add(ordenarNombreButton);
        sortPanel.add(ordenarPrecioButton);
        
        // Listeners de búsqueda
        categoryFilter.addActionListener(e -> filtrarPorCategoria());
        searchHashButton.addActionListener(e -> buscarProductoPorCodigo());
        searchContentButton.addActionListener(e -> buscarProductoPorContenido());
        clearSearchButton.addActionListener(e -> limpiarBusqueda());
        
        // Listeners de ordenación
        ordenarNombreButton.addActionListener(e -> ordenarPorNombre());
        ordenarPrecioButton.addActionListener(e -> ordenarPorPrecio());
        ordenarRatingButton.addActionListener(e -> ordenarPorRatingShellSort());
                
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
        prevButton = new JButton("⬅️ Anterior");
        nextButton = new JButton("Siguiente ➡️");
        pageStatusLabel = new JLabel("Página 1 de 1");
        pageStatusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        prevButton.addActionListener(e -> irPaginaAnterior());
        nextButton.addActionListener(e -> irPaginaSiguiente());

        paginationPanel.add(prevButton);
        paginationPanel.add(Box.createHorizontalStrut(20));
        paginationPanel.add(pageStatusLabel);
        paginationPanel.add(Box.createHorizontalStrut(20));
        paginationPanel.add(nextButton);
        
        // Acciones
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton addToCartButton = new JButton("🛒 Agregar al Carrito");
        addToCartButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        addToCartButton.setBackground(new Color(76, 175, 80));
        addToCartButton.setForeground(Color.WHITE);
        addToCartButton.setFocusPainted(false);
        
        viewCartButton = new JButton("🛍️ Ver Carrito (0)");
        viewCartButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        viewCartButton.setBackground(new Color(33, 150, 243));
        viewCartButton.setForeground(Color.WHITE);
        viewCartButton.setFocusPainted(false);
        
        JButton logoutButton = new JButton("🚪 Cerrar Sesión");
        logoutButton.setForeground(Color.RED);
        
        addToCartButton.addActionListener(e -> añadirAlCarrito());
        viewCartButton.addActionListener(e -> mostrarVentanaCarrito());
        logoutButton.addActionListener(e -> cerrarSesion());
        
        actionPanel.add(addToCartButton);
        actionPanel.add(viewCartButton);
        actionPanel.add(Box.createHorizontalStrut(50));
        actionPanel.add(logoutButton);
        
        bottomPanel.add(paginationPanel, BorderLayout.NORTH);
        bottomPanel.add(actionPanel, BorderLayout.CENTER);
        
        return bottomPanel;
    }

    // ACTUALIZACIÓN DE VISTA
    /*
    * Actualiza la vista de productos con paginación
    */
    private void updateProductView(){
        ArrayList<Producto> listaPagina = CatalogoService.obtenerPagina(paginaActual);
        displayCatalogo(listaPagina);
        
        int totalPaginas = CatalogoService.getTotalPaginas();
        pageStatusLabel.setText("Página "+ paginaActual + " de " + totalPaginas);
        
        prevButton.setEnabled(paginaActual > 1);
        nextButton.setEnabled(paginaActual < totalPaginas);
    }
    
    /*
    * Muestra una lista de productos en la tabla
    */
    private void displayCatalogo(List<Producto> listaProductos){
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
    
    private void limpiarBusqueda() {
        searchField.setText("");
        categoryFilter.setSelectedIndex(0);
        resetTableSorter();
        paginaActual = 1;
        updateProductView();
    }
    
    // Ordenación
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
    
    // CARRITO DE COMPRAS
    /*
     * Añade un producto seleccionado al carrito
     */
    private void añadirAlCarrito() {
        int selectedRow = productTable.getSelectedRow();
        
        if (selectedRow == -1) {
            mostrarAdvertencia("Por favor seleccione un producto de la tabla");
            return;
        }
        
        try {
            int modelRow = productTable.convertRowIndexToModel(selectedRow);
            String codigo = (String) tableModel.getValueAt(modelRow, 0);
            
            Producto producto = Ecommerce.buscarProductoPorHash(codigo);
            
            if (producto == null) {
                mostrarError("Producto no encontrado");
                return;
            }
            
            if (producto.getStock() <= 0) {
                mostrarError("Producto sin stock disponible");
                return;
            }
            
            // Solicitar cantidad
            String cantidadStr = JOptionPane.showInputDialog(frame,
                "Ingrese la cantidad de '" + producto.getNombre() + "':\n" +
                "Stock disponible: " + producto.getStock(),
                "Cantidad",
                JOptionPane.QUESTION_MESSAGE);
            
            if (cantidadStr == null) return;
            
            int cantidad = Integer.parseInt(cantidadStr.trim());
            
            if (cantidad <= 0) {
                mostrarError("La cantidad debe ser mayor a cero");
                return;
            }
            
            // Agregar al carrito
            if (carrito.agregarProducto_OLD(producto, cantidad)) {
                mostrarExito("✅ " + cantidad + "x " + producto.getNombre() + 
                           "\nAgregado al carrito exitosamente");
                actualizarContadorCarrito();
            } else {
                mostrarError("Stock insuficiente.\n\n" +
                           "Disponible: " + producto.getStock() + "\n" +
                           "Solicitado: " + cantidad);
            }
            
        } catch (NumberFormatException ex) {
            mostrarError("Por favor ingrese un número válido");
        } catch (Exception ex) {
            mostrarError("Error: " + ex.getMessage());
        }
    }
    
    /*
     * Actualiza el contador de items en el botón del carrito
     */
    private void actualizarContadorCarrito() {
        viewCartButton.setText("🛍️ Ver Carrito (" + carrito.contarItems() + ")");
    }
    
    /*
     * Muestra la ventana del carrito de compras
     */
    private void mostrarVentanaCarrito() {
        JDialog dialog = new JDialog(frame, "Mi Carrito de Compras", true);
        dialog.setSize(700, 550);
        dialog.setLayout(new BorderLayout(10, 10));
        dialog.setLocationRelativeTo(frame);
        
        // Tabla del carrito
        String[] columnNames = {"Código", "Producto", "Precio", "Cantidad", "Subtotal"};
        DefaultTableModel cartTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable cartTable = new JTable(cartTableModel);
        cartTable.setRowHeight(30);
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        // Panel de totales
        JPanel infoPanel = new JPanel(new BorderLayout());
        infoPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        infoPanel.setBackground(new Color(245, 245, 245));
        
        JLabel totalLabel = new JLabel("TOTAL: S/.0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 20));
        totalLabel.setForeground(new Color(76, 175, 80));
        
        JLabel itemsLabel = new JLabel("0 productos");
        itemsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        JPanel labelsPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        labelsPanel.setBackground(new Color(245, 245, 245));
        labelsPanel.add(itemsLabel);
        labelsPanel.add(totalLabel);
        
        infoPanel.add(labelsPanel, BorderLayout.WEST);
        
        // Actualizar vista del carrito
        Runnable refreshCartView = () -> {
            cartTableModel.setRowCount(0);
            double total = 0.0;
            int totalItems = 0;
            
            for (Map.Entry<Producto, Integer> entry : carrito.getItems().entrySet()) {
                Producto p = entry.getKey();
                int cantidad = entry.getValue();
                double subtotal = p.getPrecio() * cantidad;
                total += subtotal;
                totalItems += cantidad;
                
                Object[] rowData = {
                    p.getCodigo(),
                    p.getNombre(),
                    String.format("S/.%.2f", p.getPrecio()),
                    cantidad,
                    String.format("S/.%.2f", subtotal)
                };
                cartTableModel.addRow(rowData);
            }
            
            totalLabel.setText(String.format("TOTAL: S/.%.2f", total));
            itemsLabel.setText(totalItems + " producto" + (totalItems != 1 ? "s" : ""));
        };
        
        refreshCartView.run();
        
        // Botones de acción
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton actualizarButton = new JButton("✏️ Cambiar Cantidad");
        JButton removeButton = new JButton("🗑️ Eliminar");
        JButton vaciarButton = new JButton("🧹 Vaciar Carrito");
        JButton checkoutButton = new JButton("💳 FINALIZAR COMPRA");
        JButton cerrarButton = new JButton("Cerrar");
        
        checkoutButton.setBackground(new Color(76, 175, 80));
        checkoutButton.setForeground(Color.WHITE);
        checkoutButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        checkoutButton.setFocusPainted(false);
        
        // Actualizar cantidad
        actualizarButton.addActionListener(e -> {
            int row = cartTable.getSelectedRow();
            if (row == -1) {
                mostrarAdvertencia("Seleccione un producto");
                return;
            }
            
            try {
                String codigo = (String) cartTableModel.getValueAt(row, 0);
                Producto producto = Ecommerce.buscarProductoPorHash(codigo);
                
                if (producto == null) return;
                
                String nuevaCantStr = JOptionPane.showInputDialog(dialog,
                    "Nueva cantidad para '" + producto.getNombre() + "':\n" +
                    "Stock disponible: " + producto.getStock(),
                    carrito.obtenerCantidad(codigo));
                
                if (nuevaCantStr == null) return;
                
                int nuevaCant = Integer.parseInt(nuevaCantStr.trim());
                
                if (nuevaCant <= 0) {
                    carrito.removerProducto(codigo, 0);
                } else if (producto.getStock() >= nuevaCant) {
                    carrito.removerProducto(codigo, 0);
                    carrito.agregarProducto(producto, nuevaCant);
                } else {
                    mostrarError("Stock insuficiente.\nDisponible: " + producto.getStock());
                    return;
                }
                
                refreshCartView.run();
                actualizarContadorCarrito();
                
            } catch (NumberFormatException ex) {
                mostrarError("Cantidad inválida");
            }
        });
        
        // Eliminar producto
        removeButton.addActionListener(e -> {
            int row = cartTable.getSelectedRow();
            if (row == -1) {
                mostrarAdvertencia("Seleccione un producto para eliminar");
                return;
            }
            
            String codigo = (String) cartTableModel.getValueAt(row, 0);
            String nombre = (String) cartTableModel.getValueAt(row, 1);
            
            int confirm = JOptionPane.showConfirmDialog(dialog,
                "¿Eliminar '" + nombre + "' del carrito?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION);
            
            if (confirm == JOptionPane.YES_OPTION) {
                carrito.removerProducto(codigo, 0);
                refreshCartView.run();
                actualizarContadorCarrito();
            }
        });
        
        // Vaciar carrito
        vaciarButton.addActionListener(e -> {
            if (carrito.estaVacio()) {
                mostrarInformacion("Carrito vacío", "No hay productos en el carrito");
                return;
            }
            
            int confirm = JOptionPane.showConfirmDialog(dialog,
                "¿Vaciar todo el carrito?",
                "Confirmar",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                carrito.vaciar();
                refreshCartView.run();
                actualizarContadorCarrito();
            }
        });
        
        // Finalizar compra
        checkoutButton.addActionListener(e -> {
            if (carrito.estaVacio()) {
                mostrarAdvertencia("El carrito está vacío");
                return;
            }
            
            String resumen = String.format(
                "═══════════════════════════════\n" +
                "     RESUMEN DE COMPRA\n" +
                "═══════════════════════════════\n\n" +
                "Productos: %d items\n" +
                "Total a pagar: S/.%.2f\n\n" +
                "¿Confirmar compra?",
                carrito.contarProductos(),
                carrito.calcularTotal()
            );
            
            int confirm = JOptionPane.showConfirmDialog(dialog,
                resumen,
                "Confirmar Compra",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE);
            
            if (confirm == JOptionPane.YES_OPTION) {
                if (carrito.checkout()) {
                    JOptionPane.showMessageDialog(dialog,
                        "✅ ¡COMPRA EXITOSA!\n\n" +
                        "Gracias por su compra.\n" +
                        "Total pagado: S/." + String.format("%.2f", carrito.calcularTotal()),
                        "Compra Realizada",
                        JOptionPane.INFORMATION_MESSAGE);
                    
                    dialog.dispose();
                    updateProductView();
                    actualizarContadorCarrito();
                } else {
                    mostrarError("Error al procesar la compra");
                }
            }
        });
        
        cerrarButton.addActionListener(e -> dialog.dispose());
        
        buttonPanel.add(actualizarButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(vaciarButton);
        buttonPanel.add(Box.createHorizontalStrut(20));
        buttonPanel.add(checkoutButton);
        buttonPanel.add(cerrarButton);
        
        // Ensamblar diálogo
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 10, 15));
        mainPanel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        mainPanel.add(infoPanel, BorderLayout.SOUTH);
        
        dialog.add(mainPanel, BorderLayout.CENTER);
        dialog.add(buttonPanel, BorderLayout.SOUTH);
        
        dialog.setVisible(true);
    }
    
    // PAGINACIÓN
    /*
     * Navega a la página anterior
     */
    private void irPaginaAnterior() {
        if (paginaActual > 1) {
            paginaActual--;
            updateProductView();
        }
    }
    
    /*
     * Navega a la página siguiente
     */
    private void irPaginaSiguiente() {
        if (paginaActual < Ecommerce.getTotalPaginas()) {
            paginaActual++;
            updateProductView();
        }
    }
    
    // NAVEGACIÓN
    /*
     * Cierra sesión y regresa al login
     */
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(frame,
            "¿Está seguro que desea cerrar la sesión?",
            "Confirmar Cierre",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            frame.dispose();
            SwingUtilities.invokeLater(() -> new LoginView());
        }
    }
    
    // MÉTODOS DE UTILIDAD (MENSAJES)
    /*
     * Muestra un mensaje de error
     */
    private void mostrarError(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Error", 
            JOptionPane.ERROR_MESSAGE);
    }
    
    /*
     * Muestra un mensaje de éxito
     */
    private void mostrarExito(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Éxito", 
            JOptionPane.INFORMATION_MESSAGE);
    }
    
    /*
     * Muestra un mensaje de advertencia
     */
    private void mostrarAdvertencia(String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, "Advertencia", 
            JOptionPane.WARNING_MESSAGE);
    }
    
    /*
     * Muestra un mensaje informativo
     */
    private void mostrarInformacion(String titulo, String mensaje) {
        JOptionPane.showMessageDialog(frame, mensaje, titulo, 
            JOptionPane.INFORMATION_MESSAGE);
    }
}
