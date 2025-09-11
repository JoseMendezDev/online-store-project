
package ecommerce;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;


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

    public EcommerceGUI() {
        frame = new JFrame("Plataforma E-commerce");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 450);
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS));

        displayArea = new JTextArea(15, 50);
        displayArea.setEditable(false);
        JScrollPane scrollPanel = new JScrollPane(displayArea);

        JPanel buttonPanel = new JPanel();
        JPanel searchPanel = new JPanel();
        
        
        JButton ordenarIdButton = new JButton("Ordenar por ID");
        ordenarIdButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Ecommerce.resetCatalogo();
                Ecommerce.ordenarCatalogoPorId();
                displayCatalogo("Catalogo Ordenado por ID");
            }
        });

        JButton ordenarPrecioButton = new JButton("Ordenar por Precio");
        ordenarPrecioButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Ecommerce.resetCatalogo();
                Ecommerce.ordenarCatalogoPorPrecio();
                displayCatalogo("Catalogo Ordenado por Precio");
            }
        });
        
        JButton ordenarNombreButton = new JButton("Ordenar por Nombre");
        ordenarNombreButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Ecommerce.resetCatalogo();
                Ecommerce.ordenarCatalogoPorNombre();
                displayCatalogo("Catalogo Ordenado por Nombre");
            }
        });
        
        JButton ordenarNaturalMergeButton = new JButton("Ordenar por Fusion Natural");
        ordenarNaturalMergeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Ecommerce.resetCatalogo();
                Ecommerce.ordenarCatalogoPorFusionNatural();
                displayCatalogo("Catalogo Ordenado por Fusion Natural");
            }
        });
        
        JButton ordenarShellButton = new JButton("Ordenar por Shell Sort");
        ordenarShellButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Ecommerce.resetCatalogo();
                Ecommerce.ordenarCatalogoPorShellSort();
                displayCatalogo("Catalogo Ordenado por Shell Sort");
            }
        });
        
        JButton resetButton = new JButton("Resetear");
        resetButton.addActionListener(new ActionListener() {
           public void actionPerformed(ActionEvent e){
               Ecommerce.resetCatalogo();
               displayCatalogo("Catalogo Reseteado");
           } 
        });

        JLabel searchLabel = new JLabel("Buscar Producto por ID:");
        JTextField searchField = new JTextField(10);
        JButton searchButton = new JButton("Buscar");
        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    int id = Integer.parseInt(searchField.getText());
                    Ecommerce.ordenarCatalogoPorId();
                    Producto productoEncontrado = Ecommerce.buscarProductoPorIdBinaria(id);

                    displayArea.setText("");
                    if (productoEncontrado != null) {
                        displayArea.append("Producto encontrado! \n");
                        displayArea.append(productoEncontrado.toString() + "\n");
                    } else {
                        displayArea.append("Producto con ID " + id + " no encontrado.\n");
                    }

                } catch (NumberFormatException ex) {
                    displayArea.setText("Por favor, ingrese un ID valido.");
                }
            }
        });

        buttonPanel.add(ordenarIdButton);
        buttonPanel.add(ordenarPrecioButton);
        buttonPanel.add(ordenarNombreButton);
        buttonPanel.add(ordenarNaturalMergeButton);
        buttonPanel.add(ordenarShellButton);
        buttonPanel.add(resetButton);
        
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        frame.add(buttonPanel);
        frame.add(searchPanel);
        frame.add(scrollPanel);

        displayCatalogo("Catalogo Inicial");

        frame.setVisible(true);
    }

    private void displayCatalogo(String title) {
        displayArea.setText("--- " + title + " ---\n");
        for (Producto p : Ecommerce.getCatalogo()) {
            displayArea.append(p.toString() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new EcommerceGUI();
            }
        });
    }
}
