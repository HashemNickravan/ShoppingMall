package com.shoppingmall.ui.panels;

import com.shoppingmall.model.User;
import com.shoppingmall.service.AuthService;
import com.shoppingmall.ui.util.LoginSuccessListener;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    private final AuthService authService;
    private final LoginSuccessListener listener;

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginPanel(AuthService authService, LoginSuccessListener listener) {
        this.authService = authService;
        this.listener = listener;

        setLayout(new GridLayout(3, 2));

        usernameField = new JTextField();
        passwordField = new JPasswordField();

        JButton loginButton = new JButton("Login");

        add(new JLabel("Username"));
        add(usernameField);
        add(new JLabel("Password"));
        add(passwordField);
        add(new JLabel());
        add(loginButton);

        loginButton.addActionListener(e -> login());
    }

    private void login() {
        try {
            User user = authService.login(
                    usernameField.getText().trim(),
                    new String(passwordField.getPassword())
            );
            listener.onLoginSuccess(user);
        } catch (RuntimeException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage());
        }
    }
}
