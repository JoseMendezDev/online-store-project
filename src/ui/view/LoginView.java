/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package ui.view;

import javax.swing.*;
import java.awt.FlowLayout;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import service.AuthService;
import service.AuthService.ResultadoAuth;

/**
 *
 * @author USER
 */
public class LoginView extends JFrame {

    private final AuthService authService;

    private JTextField userField;
    private JPasswordField passwordField;
    private JLabel messageLabel;
    private JButton loginButton;

    public LoginView() {
        this.authService = AuthService.getInstance();

        configurarVentana();
        inicializarComponentes();
        configurarListeners();

        setVisible(true);
    }

    private void configurarVentana() {
        setTitle("🔐 Inicio de Sesión - E-commerce");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(450, 300);
        setLocationRelativeTo(null);
        setResizable(false);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void inicializarComponentes() {
        setLayout(new BorderLayout());

        // Panel principal
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);

        // Logo/Título
        JLabel titleLabel = new JLabel("🛒 E-COMMERCE SYSTEM");
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 24));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        titleLabel.setForeground(new Color(33, 150, 243));

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));

        // Panel de campos
        JPanel fieldsPanel = new JPanel(new GridLayout(2, 2, 10, 15));
        fieldsPanel.setBackground(Color.WHITE);

        JLabel userLabel = new JLabel("Usuario:");
        userLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        userField = new JTextField(20);
        userField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        JLabel passwordLabel = new JLabel("Contraseña:");
        passwordLabel.setFont(new Font("SansSerif", Font.PLAIN, 14));
        passwordField = new JPasswordField(20);
        passwordField.setFont(new Font("SansSerif", Font.PLAIN, 14));

        fieldsPanel.add(userLabel);
        fieldsPanel.add(userField);
        fieldsPanel.add(passwordLabel);
        fieldsPanel.add(passwordField);

        mainPanel.add(fieldsPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));

        // Panel de botones
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        buttonPanel.setBackground(Color.WHITE);

        loginButton = new JButton("Ingresar");
        loginButton.setPreferredSize(new Dimension(150, 35));
        loginButton.setBackground(new Color(76, 175, 80));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFont(new Font("SansSerif", Font.BOLD, 14));
        loginButton.setFocusPainted(false);
        loginButton.setBorderPainted(false);

        JButton resetButton = new JButton("Cambiar Contraseña");
        resetButton.setPreferredSize(new Dimension(180, 35));
        resetButton.setBackground(new Color(255, 152, 0));
        resetButton.setForeground(Color.WHITE);
        resetButton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        resetButton.setFocusPainted(false);
        resetButton.setBorderPainted(false);

        buttonPanel.add(loginButton);
        buttonPanel.add(resetButton);

        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 15)));

        // Mensaje
        messageLabel = new JLabel("Ingrese sus credenciales");
        messageLabel.setFont(new Font("SansSerif", Font.ITALIC, 12));
        messageLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        messageLabel.setForeground(Color.GRAY);

        mainPanel.add(messageLabel);

        // Info de acceso
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        infoPanel.setBackground(new Color(245, 245, 245));
        infoPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel infoLabel = new JLabel("💡 Usuario por defecto: admin / password123");
        infoLabel.setFont(new Font("SansSerif", Font.PLAIN, 11));
        infoLabel.setForeground(new Color(100, 100, 100));
        infoPanel.add(infoLabel);

        add(mainPanel, BorderLayout.CENTER);
        add(infoPanel, BorderLayout.SOUTH);

        // Listeners de botones
        loginButton.addActionListener(e -> intentarLogin());
        resetButton.addActionListener(e -> mostrarDialogoCambiarPassword());
    }

    private void configurarListeners() {
        // Enter para login
        KeyAdapter enterListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    intentarLogin();
                }
            }
        };

        userField.addKeyListener(enterListener);
        passwordField.addKeyListener(enterListener);
    }

    private void intentarLogin() {
        String username = userField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            mostrarMensaje("⚠️ Complete todos los campos", Color.ORANGE);
            return;
        }

        loginButton.setEnabled(false);
        mostrarMensaje("Iniciando sesión...", new Color(33, 150, 243));

        // Autenticar
        ResultadoAuth resultado = authService.login(username, password);

        if (resultado.isExitoso()) {
            mostrarMensaje("✅ " + resultado.getMensaje(), new Color(76, 175, 80));

            // Pequeña pausa visual
            Timer timer = new Timer(500, e -> {
                dispose();
                SwingUtilities.invokeLater(() -> new CatalogoView());
            });
            timer.setRepeats(false);
            timer.start();

        } else {
            mostrarMensaje("❌ " + resultado.getMensaje(), Color.RED);
            loginButton.setEnabled(true);
            passwordField.setText("");
            passwordField.requestFocus();
        }
    }

    private void mostrarDialogoCambiarPassword() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 10, 10));

        JPasswordField actualField = new JPasswordField(15);
        JPasswordField nuevaField = new JPasswordField(15);

        panel.add(new JLabel("Contraseña actual:"));
        panel.add(actualField);
        panel.add(new JLabel("Nueva contraseña:"));
        panel.add(nuevaField);

        int result = JOptionPane.showConfirmDialog(
                this,
                panel,
                "Cambiar Contraseña - Usuario: admin",
                JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE
        );

        if (result == JOptionPane.OK_OPTION) {
            String passwordActual = new String(actualField.getPassword());
            String passwordNueva = new String(nuevaField.getPassword());

            if (passwordActual.isEmpty() || passwordNueva.isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "Ambas contraseñas son requeridas",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Primero hacer login temporal para cambiar password
            ResultadoAuth loginResult = authService.login("admin", passwordActual);

            if (!loginResult.isExitoso()) {
                JOptionPane.showMessageDialog(this,
                        "Contraseña actual incorrecta",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            ResultadoAuth cambioResult = authService.cambiarPassword(passwordActual, passwordNueva);

            if (cambioResult.isExitoso()) {
                JOptionPane.showMessageDialog(this,
                        "✅ " + cambioResult.getMensaje() + "\n\nPuede usar la nueva contraseña para ingresar.",
                        "Éxito",
                        JOptionPane.INFORMATION_MESSAGE);

                authService.logout(); // Cerrar sesión temporal
            } else {
                JOptionPane.showMessageDialog(this,
                        "❌ " + cambioResult.getMensaje(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void mostrarMensaje(String texto, Color color) {
        messageLabel.setText(texto);
        messageLabel.setForeground(color);
    }
}
