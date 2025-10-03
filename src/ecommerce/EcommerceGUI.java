package ecommerce;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author USER
 */
public class EcommerceGUI
{

    private JFrame frame;
    private JTable productTable;
    private DefaultTableModel tableModel;
    private JComboBox<String> categoryFilter;

    private JPanel mainPanel, listadoPanel, agregarPanel;

    private JTextField codigoField, nombreField, precioField, stockField, categoriaField, ratingField, searchField;

    private int paginaActual = 1;
    private JLabel pageStatusLabel;
    private JButton prevButton, nextButton;

    public EcommerceGUI()
    {
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e)
        {
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

    private void createMainPanel()
    {
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

        verListadoButton.addActionListener(e ->
        {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Listado");
            paginaActual = 1;
            updateProductView();
        });
    }

    private void createListadoPanel()
    {
        listadoPanel = new JPanel(new BorderLayout());

        String[] columnNames =
        {
            "Código", "Nombre", "Precio", "Stock", "Categoría", "Rating"
        };
        tableModel = new DefaultTableModel(columnNames, 0);
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
        JButton searchButton = new JButton("Buscar Código (HASH)");

        categoryFilter = new JComboBox<>();
        categoryFilter.addItem("Todas las categorías");
        categoryFilter.addActionListener(e -> filtrarPorCategoria());

        for (String categoria : Ecommerce.getCategoriasUnicas())
        {
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
        buttonPanel.add(ordenarExternaButton);
        buttonPanel.add(resetButton);

        JPanel paginationPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        prevButton = new JButton("<< Anterior");
        nextButton = new JButton("Siguiente >>");
        pageStatusLabel = new JLabel("Página 1 de 1");

        paginationPanel.add(prevButton);
        paginationPanel.add(pageStatusLabel);
        paginationPanel.add(nextButton);

        JPanel topControls = new JPanel(new GridLayout(2, 1));
        topControls.add(searchAndFilterPanel);
        topControls.add(buttonPanel);

        controlPanel.add(topControls, BorderLayout.NORTH);
        controlPanel.add(paginationPanel, BorderLayout.SOUTH);

        listadoPanel.add(controlPanel, BorderLayout.NORTH);
        listadoPanel.add(scrollPanel, BorderLayout.CENTER);

        JButton regresarButton = new JButton("Regresar");
        listadoPanel.add(regresarButton, BorderLayout.SOUTH);

        ordenarCodigoButton.addActionListener(e ->
        {
            Ecommerce.ordenarCatalogoPorCodigo();
            paginaActual = 1;
            updateProductView();
        });
        ordenarNombreButton.addActionListener(e ->
        {
            Ecommerce.ordenarCatalogoPorNombre();
            paginaActual = 1;
            updateProductView();
        });
        ordenarPrecioButton.addActionListener(e ->
        {
            Ecommerce.ordenarCatalogoPorPrecio();
            paginaActual = 1;
            updateProductView();
        });

        ordenarExternaButton.addActionListener(e ->
        {
            ejecutarOrdenacionExterna();
        });

        resetButton.addActionListener(e ->
        {
            Ecommerce.resetCatalogo();

            paginaActual = 1;
            // Limpia el sorter de la tabla
            if (productTable.getRowSorter() != null)
            {
                productTable.getRowSorter().setSortKeys(null);
            }

            categoryFilter.setSelectedIndex(0);
            updateProductView();
            scrollPanel.setBorder(BorderFactory.createTitledBorder("Listado de Productos"));
        });

        prevButton.addActionListener(e ->
        {
            if (paginaActual > 1)
            {
                paginaActual--;
                updateProductView();
            }
        });

        nextButton.addActionListener(e ->
        {
            if (paginaActual < Ecommerce.getTotalPaginas())
            {
                paginaActual++;
                updateProductView();
            }
        });

        regresarButton.addActionListener(e ->
        {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });

        searchButton.addActionListener(e ->
        {
            String codigo = searchField.getText();
            Producto productoEncontrado = Ecommerce.buscarProductoPorHash(codigo);
            if (productoEncontrado != null)
            {
                ArrayList<Producto> lista = new ArrayList<>();
                lista.add(productoEncontrado);

                if (productTable.getRowSorter() != null)
                {
                    productTable.getRowSorter().setSortKeys(null);
                }

                displayCatalogo(lista);
                scrollPanel.setBorder(BorderFactory.createTitledBorder("Resultado de Búsqueda"));

                pageStatusLabel.setText("Resultado (1 ítem)");
                prevButton.setEnabled(false);
                nextButton.setEnabled(false);
            } else
            {
                JOptionPane.showMessageDialog(frame, "Producto con código " + codigo + " no encontrado.", "Búsqueda Fallida", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    private void updateProductView()
    {
        ArrayList<Producto> listaPagina = Ecommerce.getPagina(paginaActual);
        displayCatalogo(listaPagina);

        int totalPaginas = Ecommerce.getTotalPaginas();
        pageStatusLabel.setText("Página " + paginaActual + " de " + totalPaginas);

        prevButton.setEnabled(paginaActual > 1);
        nextButton.setEnabled(paginaActual < totalPaginas);
    }

    private void ejecutarOrdenacionExterna()
    {
        try
        {
            File inputFile = new File("catalogo_entrada.txt");
            try (FileWriter writer = new FileWriter(inputFile))
            {
                for (Producto p : Ecommerce.getCatalogo())
                {
                    writer.write(p.toFileString());
                    writer.write(System.lineSeparator());
                }
            }

            File outputFile = new File("catalogo_salida_ordenada.txt");
            OrdenacionExterna.ordenar(inputFile, outputFile);

            ArrayList<Producto> catalogoOrdenado = Ecommerce.cargarCatalogoDesdeArchivo(outputFile);

            Ecommerce.setCatalogo(catalogoOrdenado);

            if (productTable.getRowSorter() != null)
            {
                productTable.getRowSorter().setSortKeys(null);
            }
            paginaActual = 1;
            updateProductView();

            JOptionPane.showMessageDialog(frame,
                    "Ordenación Externa completada con éxito. El resultado se ha cargado en memoria y guardado en archivo.",
                    "Éxito", JOptionPane.INFORMATION_MESSAGE);

            inputFile.delete();
            outputFile.delete();

        } catch (IOException ex)
        {
            JOptionPane.showMessageDialog(frame, "Error durante la Ordenación Externa: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createAgregarPanel()
    {
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

        guardarButton.addActionListener(e ->
        {
            try
            {
                String codigo = codigoField.getText();
                String nombre = nombreField.getText();
                double precio = Double.parseDouble(precioField.getText());
                int stock = Integer.parseInt(stockField.getText());
                String categoria = categoriaField.getText();
                double rating = Double.parseDouble(ratingField.getText()); // Lectura del nuevo campo

                if (codigo.length() != 6 || !codigo.matches("\\d+"))
                {
                    JOptionPane.showMessageDialog(frame, "El código debe tener 6 dígitos numéricos.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                if (rating < 0.0 || rating > 5.0)
                {
                    JOptionPane.showMessageDialog(frame, "El Rating debe estar entre 0.0 y 5.0.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                Producto nuevoProducto = new Producto(codigo, nombre, precio, stock, categoria, rating);
                boolean agregado = Ecommerce.agregarProducto(nuevoProducto);

                if (agregado)
                {
                    JOptionPane.showMessageDialog(frame, "Producto agregado con éxito!");
                    // Limpiar campos
                    codigoField.setText("");
                    nombreField.setText("");
                    precioField.setText("");
                    stockField.setText("");
                    categoriaField.setText("");
                    ratingField.setText("");
                } else
                {
                    JOptionPane.showMessageDialog(frame, "Error: El código de producto ya existe.", "Error de Validación", JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex)
            {
                JOptionPane.showMessageDialog(frame, "Precio, Stock y Rating deben ser números válidos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        regresarButton.addActionListener(e ->
        {
            ((CardLayout) frame.getContentPane().getLayout()).show(frame.getContentPane(), "Main");
        });
    }

    private void displayCatalogo(ArrayList<Producto> listaProductos)
    {
        tableModel.setRowCount(0); // Limpia la tabla
        for (Producto p : listaProductos)
        {
            Object[] rowData =
            {
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
    
        private void filtrarPorCategoria()
    {
        String categoriaSeleccionada = (String) categoryFilter.getSelectedItem();
        if (productTable.getRowSorter() != null)
        {
            productTable.getRowSorter().setSortKeys(null);
        }

        if ("Todas las categorías".equals(categoriaSeleccionada))
        {
            paginaActual = 1;
            updateProductView();
        } else
        {
            ArrayList<Producto> catalogoCompleto = Ecommerce.getCatalogo();
            ArrayList<Producto> listaFiltrada = new ArrayList<>();
            for (Producto p : catalogoCompleto)
            {
                if (p.getCategoria().equals(categoriaSeleccionada))
                {
                    listaFiltrada.add(p);
                }
            }
            displayCatalogo(listaFiltrada);

            pageStatusLabel.setText("Filtro Activo (" + listaFiltrada.size() + " ítems)");
            prevButton.setEnabled(false);
            nextButton.setEnabled(false);
        }
    }
}
