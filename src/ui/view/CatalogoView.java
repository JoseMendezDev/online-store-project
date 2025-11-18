package ui.view;

import service.CatalogoService;
import service.CarritoService;
import service.AuthService;
import domain.Producto;
import config.AppConfig;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Vista principal del catálogo de productos
 * Refactorizada con separación de responsabilidades
 */
public class CatalogoView extends JFrame {
    
    // Servicios
    private final CatalogoService catalogoService;
    private final CarritoService carritoService;
    private final AuthService authService;
    
    // Componentes UI
    private JTable productTable;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;
    
    private JComboBox<String> categoryFilter;
    private JTextField searchField;
    
    private JLabel pageStatusLabel;
    private JButton prevButton, nextButton;
    private JButton viewCartButton;
    
    private int paginaActual = 1;
    
    public CatalogoView() {
        this.catalogoService = CatalogoService.getInstance();
        this.carritoService = new CarritoService();
        this.authService = AuthService.getInstance();
        
        configurarVentana();
        inicializarComponentes();
        cargarDatos();
        
        setVisible(true);
    }
    
    // ========== CONFIGURACIÓN ==========
    
    private void configurarVentana() {
        setTitle("🛒 " + AppConfig.NOMBRE_APP + " - " + authService.getUsuarioActual().getUsername());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(AppConfig.ANCHO_VENTANA, AppConfig.ALTO_VENTANA);
        setLocationRelativeTo(null);
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void inicializarComponentes() {
        setLayout(new BorderLayout(10, 10));
        
        add(crearPanelSuperior(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearPanelInferior(), BorderLayout.SOUTH);
    }
    
    // ========== PANEL SUPERIOR: Búsqueda y Filtros ==========
    
    private JPanel crearPanelSuperior() {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        // Panel de búsqueda
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.setBorder(BorderFactory.createTitledBorder("🔍 Búsqueda y Filtros"));
        
        searchField = new JTextField(20);
        JButton searchCodeButton = new JButton("Buscar por Código");
        JButton searchTextButton = new JButton("Buscar por Texto");
        JButton clearButton = new JButton("Limpiar");
        
        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("Todas las categorías");
        actualizarFiltroCategorias();
        
        searchPanel.add(new JLabel("Categoría:"));
        searchPanel.add(categoryFilter);
        searchPanel.add(Box.createHorizontalStrut(15));
        searchPanel.add(searchField);
        searchPanel.add(searchCodeButton);
        searchPanel.add(searchTextButton);
        searchPanel.add(clearButton);
        
        // Panel de ordenación
        JPanel sortPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        sortPanel.setBorder(BorderFactory.createTitledBorder("📊 Ordenar por"));
        
        JButton sortNameButton = new JButton("Nombre");
        JButton sortPriceButton = new JButton("Precio");
        JButton sortRatingButton = new JButton("Rating");
        
        sortPanel.add(sortNameButton);
        sortPanel.add(sortPriceButton);
        sortPanel.add(sortRatingButton);
        
        // Listeners
        categoryFilter.addActionListener(e -> filtrarPorCategoria());
        searchCodeButton.addActionListener(e -> buscarPorCodigo());
        searchTextButton.addActionListener(e -> buscarPorTexto());
        clearButton.addActionListener(e -> limpiarBusqueda());
        
        sortNameButton.addActionListener(e -> ordenarPor("nombre"));
        sortPriceButton.addActionListener(e -> ordenarPor("precio"));
        sortRatingButton.addActionListener(e -> ordenarPor("rating"));
        
        panel.add(searchPanel);
        panel.add(sortPanel);
        
        return panel;
    }
    
    // ========== PANEL CENTRAL: Tabla de Productos ==========
    
    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new BorderLayout());
        
        String[] columnNames = {"Código", "Nombre", "Precio", "Stock", "Categoría", "Rating"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
            
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                switch (columnIndex) {
                    case 2: case 5: return Double.class;
                    case 3: return Integer.class;
                    default: return String.class;
                }
            }
        };
        
        productTable = new JTable(tableModel);
        productTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        productTable.setRowHeight(25);
        
        sorter = new TableRowSorter<>(tableModel);
        productTable.setRowSorter(sorter);
        
        // Anchos de columna
        productTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        productTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        productTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        productTable.getColumnModel().getColumn(3).setPreferredWidth(60);
        productTable.getColumnModel().getColumn(4).setPreferredWidth(120);
        productTable.getColumnModel().getColumn(5).setPreferredWidth(60);
        
        panel.add(new JScrollPane(productTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    // ========== PANEL INFERIOR: Paginación y Acciones ==========
    
    private JPanel crearPanelInferior() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Paginación
        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        
        prevButton = new JButton("⬅️ Anterior");
        nextButton = new JButton("Siguiente ➡️");
        pageStatusLabel = new JLabel("Página 1 de 1");
        pageStatusLabel.setFont(new Font("SansSerif", Font.BOLD, 12));
        
        prevButton.addActionListener(e -> cambiarPagina(-1));
        nextButton.addActionListener(e -> cambiarPagina(1));
        
        paginationPanel.add(prevButton);
        paginationPanel.add(Box.createHorizontalStrut(20));
        paginationPanel.add(pageStatusLabel);
        paginationPanel.add(Box.createHorizontalStrut(20));
        paginationPanel.add(nextButton);
        
        // Acciones
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        
        JButton addToCartButton = crearBoton("🛒 Agregar al Carrito", new Color(76, 175, 80));
        viewCartButton = crearBoton("🛍️ Ver Carrito (0)", new Color(33, 150, 243));
        JButton logoutButton = new JButton("🚪 Cerrar Sesión");
        
        addToCartButton.addActionListener(e -> agregarAlCarrito());
        viewCartButton.addActionListener(e -> abrirCarrito());
        logoutButton.addActionListener(e -> cerrarSesion());
        
        actionPanel.add(addToCartButton);
        actionPanel.add(viewCartButton);
        actionPanel.add(Box.createHorizontalStrut(30));
        actionPanel.add(logoutButton);
        
        panel.add(paginationPanel, BorderLayout.NORTH);
        panel.add(actionPanel, BorderLayout.CENTER);
        
        return panel;
    }
    
    private JButton crearBoton(String texto, Color color) {
        JButton button = new JButton(texto);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }
    
    // ========== LÓGICA DE DATOS ==========
    
    private void cargarDatos() {
        actualizarVista();
    }
    
    private void actualizarVista() {
        List<Producto> productos = catalogoService.obtenerPagina(paginaActual);
        mostrarProductos(productos);
        actualizarPaginacion();
    }
    
    private void mostrarProductos(List<Producto> productos) {
        tableModel.setRowCount(0);
        
        for (Producto p : productos) {
            Object[] row = {
                p.getCodigo(),
                p.getNombre(),
                p.getPrecio(),
                p.getStock(),
                p.getCategoria(),
                p.getRating()
            };
            tableModel.addRow(row);
        }
    }
    
    private void actualizarPaginacion() {
        int totalPaginas = catalogoService.calcularTotalPaginas();
        pageStatusLabel.setText("Página " + paginaActual + " de " + totalPaginas);
        prevButton.setEnabled(paginaActual > 1);
        nextButton.setEnabled(paginaActual < totalPaginas);
    }
    
    private void actualizarFiltroCategorias() {
        String seleccion = (String) categoryFilter.getSelectedItem();
        categoryFilter.removeAllItems();
        categoryFilter.addItem("Todas las categorías");
        
        catalogoService.obtenerCategoriasUnicas().forEach(categoryFilter::addItem);
        
        if (seleccion != null) {
            categoryFilter.setSelectedItem(seleccion);
        }
    }
    
    // ========== ACCIONES ==========
    
    private void cambiarPagina(int direccion) {
        int nuevaPagina = paginaActual + direccion;
        int totalPaginas = catalogoService.calcularTotalPaginas();
        
        if (nuevaPagina >= 1 && nuevaPagina <= totalPaginas) {
            paginaActual = nuevaPagina;
            actualizarVista();
        }
    }
    
    private void buscarPorCodigo() {
        String codigo = searchField.getText().trim();
        
        if (codigo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese un código", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        catalogoService.buscarPorCodigoHash(codigo).ifPresentOrElse(
            producto -> {
                mostrarProductos(List.of(producto));
                pageStatusLabel.setText("Resultado: 1 producto");
                desactivarPaginacion();
            },
            () -> JOptionPane.showMessageDialog(this, "Producto no encontrado", "Info", JOptionPane.INFORMATION_MESSAGE)
        );
    }
    
    private void buscarPorTexto() {
        String texto = searchField.getText().trim();
        
        if (texto.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese texto para buscar", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        List<Producto> resultados = catalogoService.buscarPorPalabraClave(texto);
        
        if (resultados.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No se encontraron resultados", "Info", JOptionPane.INFORMATION_MESSAGE);
        } else {
            mostrarProductos(resultados);
            pageStatusLabel.setText("Resultados: " + resultados.size() + " productos");
            desactivarPaginacion();
        }
    }
    
    private void filtrarPorCategoria() {
        String categoria = (String) categoryFilter.getSelectedItem();
        
        if ("Todas las categorías".equals(categoria)) {
            paginaActual = 1;
            actualizarVista();
        } else {
            List<Producto> filtrados = catalogoService.buscarPorCategoria(categoria);
            mostrarProductos(filtrados);
            pageStatusLabel.setText("Filtro: " + filtrados.size() + " productos");
            desactivarPaginacion();
        }
    }
    
    private void limpiarBusqueda() {
        searchField.setText("");
        categoryFilter.setSelectedIndex(0);
        sorter.setSortKeys(null);
        paginaActual = 1;
        actualizarVista();
    }
    
    private void ordenarPor(String criterio) {
        switch (criterio) {
            case "nombre":
                catalogoService.ordenarPorNombre();
                break;
            case "precio":
                catalogoService.ordenarPorPrecio();
                break;
            case "rating":
                catalogoService.ordenarPorRating();
                break;
        }
        
        paginaActual = 1;
        actualizarVista();
        JOptionPane.showMessageDialog(this, "Ordenado por " + criterio, "Éxito", JOptionPane.INFORMATION_MESSAGE);
    }
    
    private void agregarAlCarrito() {
        int selectedRow = productTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int modelRow = productTable.convertRowIndexToModel(selectedRow);
        String codigo = (String) tableModel.getValueAt(modelRow, 0);
        
        catalogoService.buscarPorCodigo(codigo).ifPresent(producto -> {
            if (producto.getStock() <= 0) {
                JOptionPane.showMessageDialog(this, "Producto sin stock", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            
            String cantidadStr = JOptionPane.showInputDialog(
                this,
                "Cantidad para '" + producto.getNombre() + "':\nStock disponible: " + producto.getStock(),
                "1"
            );
            
            if (cantidadStr != null) {
                try {
                    int cantidad = Integer.parseInt(cantidadStr.trim());
                    
                    CarritoService.ResultadoOperacion resultado = carritoService.agregarProducto(producto, cantidad);
                    
                    if (resultado.isExitoso()) {
                        JOptionPane.showMessageDialog(this, "✅ " + resultado.getMensaje(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                        actualizarContadorCarrito();
                    } else {
                        JOptionPane.showMessageDialog(this, "❌ " + resultado.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
    
    private void abrirCarrito() {
        new CarritoDialog(this, carritoService, this::actualizarContadorCarrito, this::actualizarVista);
    }
    
    private void actualizarContadorCarrito() {
        viewCartButton.setText("🛍️ Ver Carrito (" + carritoService.contarItems() + ")");
    }
    
    private void cerrarSesion() {
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Cerrar sesión?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            authService.logout();
            dispose();
            SwingUtilities.invokeLater(() -> new LoginView());
        }
    }
    
    private void desactivarPaginacion() {
        prevButton.setEnabled(false);
        nextButton.setEnabled(false);
    }
}
