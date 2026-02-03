package com.shoppingmall.ui.panels;

import com.shoppingmall.service.AuthService;

import javax.swing.*;

public class RegisterPanel extends JPanel {

    public RegisterPanel(AuthService authService, Runnable backToLogin,
                         Runnable openCustomer) {

        JTextField usernameField = new JTextField(15);
        JPasswordField passwordField = new JPasswordField(15);
        JButton registerBtn = new JButton("Register");
        JButton backBtn = new JButton("Back");

        registerBtn.addActionListener(e -> {
            try {
                authService.register(
                        usernameField.getText(),
                        new String(passwordField.getPassword())
                );
                openCustomer.run();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        backBtn.addActionListener(e -> backToLogin.run());

        add(new JLabel("Username"));
        add(usernameField);
        add(new JLabel("Password"));
        add(passwordField);
        add(registerBtn);
        add(backBtn);
    }
}
