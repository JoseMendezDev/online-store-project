/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

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
        frame = new JFrame("Iniciar Sesión");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);
        frame.setLocationRelativeTo(null);
        frame.setLayout(new BorderLayout());

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel inputPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        inputPanel.add(new JLabel("Usuario:"));
        userField = new JTextField(20);
        inputPanel.add(userField);
        inputPanel.add(new JLabel("Contraseña:"));
        passwordField = new JPasswordField(20);
        inputPanel.add(passwordField);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        JButton loginButton = new JButton("Ingresar");
        JButton forgotPasswordButton = new JButton("¿Olvidaste tu contraseña?");
        buttonPanel.add(loginButton);
        buttonPanel.add(forgotPasswordButton);

        messageLabel = new JLabel("");
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        mainPanel.add(inputPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(messageLabel);

        frame.add(mainPanel, BorderLayout.CENTER);
        frame.pack();

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
                String nuevaContrasena = JOptionPane.showInputDialog(frame, "Ingresa tu nueva contraseña:");

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
