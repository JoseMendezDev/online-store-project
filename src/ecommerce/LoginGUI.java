/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ecommerce;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.FlowLayout;

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
    private static final String HASH_CONTRASENA_VALIDA = "d74ff0ee8da3b9806a18802a4664c15383f2a1b199e82194600d116b3f71587d";

    public LoginGUI() {
        frame = new JFrame("Iniciar Sesión");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);
        frame.setLayout(new FlowLayout());

        frame.setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new BoxLayout(loginPanel, BoxLayout.Y_AXIS));

        userField = new JTextField(15);
        passwordField = new JPasswordField(15);
        JButton loginButton = new JButton("Ingresar");
        messageLabel = new JLabel(" ");

        loginPanel.add(new JLabel("Usuario:"));
        loginPanel.add(userField);
        loginPanel.add(new JLabel("Contraseña:"));
        loginPanel.add(passwordField);
        loginPanel.add(loginButton);
        loginPanel.add(messageLabel);

        frame.add(loginPanel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userField.getText();
                String password = new String(passwordField.getPassword());

                if (user.isEmpty() || password.isEmpty()) {
                    messageLabel.setText("Usuario y contraseña no pueden estar vacios.");
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

        frame.setVisible(true);
    }
}
