/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gui;

import utilidades.HashUtilidades;
import gui.EcommerceGUI;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;
import java.awt.*;

/**
 *
 * @author USER
 */
public class LoginGUI {

    private JFrame frame;
    private JTextField userField;
    private JPasswordField passwordField;
    private JLabel messageLabel;

    private static final String USUARIO_VALIDO = "admin";
    private static String HASH_CONTRASENA_VALIDA = "ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f";

    public LoginGUI() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        frame = new JFrame("Inicio de Sesión");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 10, 10));

        inputPanel.add(new JLabel("Usuario:"));
        userField = new JTextField(20);
        inputPanel.add(userField);

        inputPanel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField(20);
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton loginButton = new JButton("Ingresar");
        JButton forgotPasswordButton = new JButton("¿Olvidaste tu contraseña?");

        loginButton.setPreferredSize(new Dimension(150, 30));
        forgotPasswordButton.setPreferredSize(new Dimension(200, 30));

        buttonPanel.add(loginButton);
        buttonPanel.add(forgotPasswordButton);

        messageLabel = new JLabel("Ingrese sus credenciales");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Espacio
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Espacio
        mainPanel.add(messageLabel);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.pack();
        frame.setLocationRelativeTo(null);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userField.getText();
                String password = new String(passwordField.getPassword());

                if (user.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("Usuario y contraseña no pueden estar vacíos.");
                    return;
                }

                String passwordHashIngresado = HashUtilidades.generarHash(password);

                if (user.equals(USUARIO_VALIDO) && passwordHashIngresado.equals(HASH_CONTRASENA_VALIDA)) {
                    messageLabel.setText("Iniciando sesión...");
                    frame.dispose();
                    SwingUtilities.invokeLater(() -> new EcommerceGUI());
                } else {
                    messageLabel.setText("Credenciales incorrectas.");
                }
            }
        });

        forgotPasswordButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JPasswordField nuevoPasswordField = new JPasswordField(20);
                int option = JOptionPane.showConfirmDialog(frame, nuevoPasswordField,
                        "Ingresa tu nueva contraseña", JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

                String nuevaContrasena = null;
                if (option == JOptionPane.OK_OPTION) {

                    nuevaContrasena = new String(nuevoPasswordField.getPassword());
                }

                if (nuevaContrasena != null && !nuevaContrasena.isEmpty()) {
                    String hashAnterior = HASH_CONTRASENA_VALIDA;
                    String nuevoHash = HashUtilidades.generarHash(nuevaContrasena);
                    HASH_CONTRASENA_VALIDA = nuevoHash;

                    JOptionPane.showMessageDialog(frame,
                            "Contraseña actualizada con éxito.\n\n"
                            + "Hash anterior: " + hashAnterior + "\n"
                            + "Nuevo hash: " + nuevoHash,
                            "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(frame,
                            "Operación cancelada o contraseña inválida.",
                            "Atención", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        frame.setVisible(true);
    }
}
