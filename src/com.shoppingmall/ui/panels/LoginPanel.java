package com.shoppingmall.ui.panels;

import com.shoppingmall.service.AuthService;
import com.shoppingmall.model.User;
import com.shoppingmall.ui.util.LoginSuccessListener;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private final AuthService authService;
    private final JTextField usernameField;
    private final JPasswordField passwordField;
    private final LoginSuccessListener listener;

    public LoginPanel(AuthService authService, LoginSuccessListener listener) {
        this.authService = authService;
        this.listener = listener;

        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Login", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        add(title, gbc);

        gbc.gridwidth = 1;

        gbc.gridy++;
        add(new JLabel("Username:"), gbc);

        gbc.gridx = 1;
        usernameField = new JTextField(15);
        add(usernameField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        add(new JLabel("Password:"), gbc);

        gbc.gridx = 1;
        passwordField = new JPasswordField(15);
        add(passwordField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;

        JButton loginButton = new JButton("Login");
        add(loginButton, gbc);

        loginButton.addActionListener(e -> login());
    }

    private void login() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        User user = authService.login(username, password);

        if (user == null) {
            JOptionPane.showMessageDialog(this,
                    "Invalid username or password",
                    "Login Failed",
                    JOptionPane.ERROR_MESSAGE);
        } else {
            listener.onLoginSuccess(user);
        }

    }
}
