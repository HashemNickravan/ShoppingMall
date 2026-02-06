package com.shoppingmall.ui.panels;

import com.shoppingmall.model.Role;
import com.shoppingmall.service.AuthService;
import com.shoppingmall.ui.MainFrame;

import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {
    private MainFrame mainFrame;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;
    private JComboBox<Role> roleComboBox;
    private JTextField balanceField;

    public RegisterPanel(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        initializeUI();
    }

    private void initializeUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);


        JLabel titleLabel = new JLabel("Shopping Mall - Register");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(titleLabel, gbc);


        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(usernameField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(passwordField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Confirm Password:"), gbc);

        confirmPasswordField = new JPasswordField(20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(confirmPasswordField, gbc);


        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Role:"), gbc);

        roleComboBox = new JComboBox<>(Role.values());
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(roleComboBox, gbc);


        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.anchor = GridBagConstraints.EAST;
        add(new JLabel("Initial Balance (Toman):"), gbc);

        balanceField = new JTextField("0", 20);
        gbc.gridx = 1;
        gbc.anchor = GridBagConstraints.WEST;
        add(balanceField, gbc);


        JPanel buttonPanel = new JPanel(new FlowLayout());

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(e -> handleRegister());
        buttonPanel.add(registerButton);

        JButton backButton = new JButton("Back to Login");
        backButton.addActionListener(e -> mainFrame.showLoginPanel());
        buttonPanel.add(backButton);

        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        add(buttonPanel, gbc);
    }

    private void handleRegister() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());
        Role role = (Role) roleComboBox.getSelectedItem();
        String balanceText = balanceField.getText().trim();


        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                    "Username and password cannot be empty.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this,
                    "Passwords do not match.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        double balance;
        try {
            balance = Double.parseDouble(balanceText);
            if (balance < 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                    "Please enter a valid balance (non-negative number).",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        AuthService authService = mainFrame.getAuthService();
        if (authService.register(username, password, role, balance)) {
            JOptionPane.showMessageDialog(this,
                    "Registration successful! You can now login.",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE);
            clearFields();
            mainFrame.showLoginPanel();
        } else {
            JOptionPane.showMessageDialog(this,
                    "Username already exists.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
        roleComboBox.setSelectedIndex(0);
        balanceField.setText("0");
    }
}
