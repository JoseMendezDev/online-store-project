/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.view;

import service.CarritoService;
import service.CarritoService.ItemCarrito;
import service.CarritoService.ResultadoOperacion;
import domain.Producto;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

/**
 * Diálogo modal para gestionar el carrito de compras
 */
public class CarritoDialog extends JDialog {
    
    private final CarritoService carritoService;
    private final Runnable onCartUpdate;
    private final Runnable onPurchaseComplete;
    
    private DefaultTableModel tableModel;
    private JTable cartTable;
    private JLabel totalLabel;
    private JLabel itemsLabel;
    
    public CarritoDialog(JFrame parent, CarritoService carritoService, 
                        Runnable onCartUpdate, Runnable onPurchaseComplete) {
        super(parent, "🛒 Mi Carrito de Compras", true);
        
        this.carritoService = carritoService;
        this.onCartUpdate = onCartUpdate;
        this.onPurchaseComplete = onPurchaseComplete;
        
        configurarDialogo();
        inicializarComponentes();
        cargarDatos();
        
        setVisible(true);
    }
    
    private void configurarDialogo() {
        setSize(800, 600);
        setLocationRelativeTo(getParent());
        setLayout(new BorderLayout(10, 10));
    }
    
    private void inicializarComponentes() {
        // Panel principal
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        
        // Tabla del carrito
        mainPanel.add(crearPanelTabla(), BorderLayout.CENTER);
        
        // Panel de información (totales)
        mainPanel.add(crearPanelInfo(), BorderLayout.SOUTH);
        
        // Panel de botones
        add(mainPanel, BorderLayout.CENTER);
        add(crearPanelBotones(), BorderLayout.SOUTH);
    }
    
    private JPanel crearPanelTabla() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Productos en el carrito"));
        
        String[] columnNames = {"Código", "Producto", "Precio Unit.", "Cantidad", "Subtotal"};
        
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        cartTable = new JTable(tableModel);
        cartTable.setRowHeight(30);
        cartTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // Configurar anchos
        cartTable.getColumnModel().getColumn(0).setPreferredWidth(80);
        cartTable.getColumnModel().getColumn(1).setPreferredWidth(250);
        cartTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        cartTable.getColumnModel().getColumn(3).setPreferredWidth(80);
        cartTable.getColumnModel().getColumn(4).setPreferredWidth(100);
        
        panel.add(new JScrollPane(cartTable), BorderLayout.CENTER);
        
        return panel;
    }
    
    private JPanel crearPanelInfo() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 0, 0));
        panel.setBackground(new Color(245, 245, 245));
        
        JPanel infoPanel = new JPanel(new GridLayout(2, 1, 0, 5));
        infoPanel.setBackground(new Color(245, 245, 245));
        
        itemsLabel = new JLabel("0 productos");
        itemsLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        
        totalLabel = new JLabel("TOTAL: S/.0.00");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        totalLabel.setForeground(new Color(76, 175, 80));
        
        infoPanel.add(itemsLabel);
        infoPanel.add(totalLabel);
        
        panel.add(infoPanel, BorderLayout.WEST);
        
        return panel;
    }
    
    private JPanel crearPanelBotones() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        
        JButton actualizarButton = new JButton("✏️ Cambiar Cantidad");
        JButton eliminarButton = new JButton("🗑️ Eliminar");
        JButton vaciarButton = new JButton("🧹 Vaciar Todo");
        JButton comprarButton = new JButton("💳 FINALIZAR COMPRA");
        JButton cerrarButton = new JButton("Cerrar");
        
        // Estilos
        comprarButton.setBackground(new Color(76, 175, 80));
        comprarButton.setForeground(Color.WHITE);
        comprarButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        comprarButton.setFocusPainted(false);
        
        vaciarButton.setForeground(Color.RED);
        
        // Listeners
        actualizarButton.addActionListener(e -> actualizarCantidad());
        eliminarButton.addActionListener(e -> eliminarProducto());
        vaciarButton.addActionListener(e -> vaciarCarrito());
        comprarButton.addActionListener(e -> finalizarCompra());
        cerrarButton.addActionListener(e -> dispose());
        
        panel.add(actualizarButton);
        panel.add(eliminarButton);
        panel.add(vaciarButton);
        panel.add(Box.createHorizontalStrut(20));
        panel.add(comprarButton);
        panel.add(cerrarButton);
        
        return panel;
    }
    
    // ========== CARGA DE DATOS ==========
    
    private void cargarDatos() {
        tableModel.setRowCount(0);
        
        double total = 0.0;
        int totalProductos = 0;
        
        for (Map.Entry<Producto, Integer> entry : carritoService.getItems().entrySet()) {
            Producto p = entry.getKey();
            int cantidad = entry.getValue();
            double subtotal = p.getPrecio() * cantidad;
            
            total += subtotal;
            totalProductos += cantidad;
            
            Object[] row = {
                p.getCodigo(),
                p.getNombre(),
                String.format("S/.%.2f", p.getPrecio()),
                cantidad,
                String.format("S/.%.2f", subtotal)
            };
            
            tableModel.addRow(row);
        }
        
        actualizarTotales(total, totalProductos);
    }
    
    private void actualizarTotales(double total, int cantidad) {
        totalLabel.setText(String.format("TOTAL: S/.%.2f", total));
        itemsLabel.setText(cantidad + " producto" + (cantidad != 1 ? "s" : ""));
    }
    
    // ========== ACCIONES ==========
    
    private void actualizarCantidad() {
        int selectedRow = cartTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String codigo = (String) tableModel.getValueAt(selectedRow, 0);
        int cantidadActual = carritoService.obtenerCantidad(codigo);
        
        String input = JOptionPane.showInputDialog(
            this,
            "Nueva cantidad:",
            String.valueOf(cantidadActual)
        );
        
        if (input != null) {
            try {
                int nuevaCantidad = Integer.parseInt(input.trim());
                
                ResultadoOperacion resultado = carritoService.actualizarCantidad(codigo, nuevaCantidad);
                
                if (resultado.isExitoso()) {
                    cargarDatos();
                    onCartUpdate.run();
                    JOptionPane.showMessageDialog(this, "✅ " + resultado.getMensaje(), "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "❌ " + resultado.getMensaje(), "Error", JOptionPane.ERROR_MESSAGE);
                }
                
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Cantidad inválida", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void eliminarProducto() {
        int selectedRow = cartTable.getSelectedRow();
        
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un producto", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String codigo = (String) tableModel.getValueAt(selectedRow, 0);
        String nombre = (String) tableModel.getValueAt(selectedRow, 1);
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Eliminar '" + nombre + "' del carrito?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            ResultadoOperacion resultado = carritoService.removerProducto(codigo);
            
            if (resultado.isExitoso()) {
                cargarDatos();
                onCartUpdate.run();
            }
        }
    }
    
    private void vaciarCarrito() {
        if (carritoService.estaVacio()) {
            JOptionPane.showMessageDialog(this, "El carrito ya está vacío", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            "¿Vaciar todo el carrito?",
            "Confirmar",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            carritoService.vaciar();
            cargarDatos();
            onCartUpdate.run();
        }
    }
    
    private void finalizarCompra() {
        if (carritoService.estaVacio()) {
            JOptionPane.showMessageDialog(this, "El carrito está vacío", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        String resumen = String.format(
            "═══════════════════════════════════\n" +
            "        RESUMEN DE COMPRA\n" +
            "═══════════════════════════════════\n\n" +
            "Productos: %d items\n" +
            "Total a pagar: S/.%.2f\n\n" +
            "¿Confirmar compra?",
            carritoService.contarProductos(),
            carritoService.calcularTotal()
        );
        
        int confirm = JOptionPane.showConfirmDialog(
            this,
            resumen,
            "Confirmar Compra",
            JOptionPane.YES_NO_OPTION,
            JOptionPane.QUESTION_MESSAGE
        );
        
        if (confirm == JOptionPane.YES_OPTION) {
            ResultadoOperacion resultado = carritoService.procesarCompra();
            
            if (resultado.isExitoso()) {
                JOptionPane.showMessageDialog(
                    this,
                    resultado.getMensaje() + "\n\n¡Gracias por su compra!",
                    "Compra Exitosa",
                    JOptionPane.INFORMATION_MESSAGE
                );
                
                onCartUpdate.run();
                onPurchaseComplete.run();
                dispose();
                
            } else {
                JOptionPane.showMessageDialog(
                    this,
                    "❌ " + resultado.getMensaje(),
                    "Error en la Compra",
                    JOptionPane.ERROR_MESSAGE
                );
            }
        }
    }
}
