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
                } else {
                    messageLabel.setText("Iniciando sesión...");
                    frame.dispose(); 

                    SwingUtilities.invokeLater(() -> new EcommerceGUI());
                }
            }
        });

        frame.setVisible(true);
    }
}
