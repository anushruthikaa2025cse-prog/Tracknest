package lostfound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

// The registration screen for new users.
public class RegisterFrame extends JFrame {

    private JTextField fullNameField;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JPasswordField confirmPasswordField;

    public RegisterFrame() {
        setTitle("TrackNest - Register");
        setSize(540, 640);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BACKGROUND_WHITE);

        add(Theme.header(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new GridBagLayout());
        body.setBackground(Theme.BACKGROUND_WHITE);

        JPanel card = new JPanel(new GridBagLayout());
        card.setBackground(Theme.LIGHT_BLUE);
        card.setPreferredSize(new Dimension(380, 470));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.COLUMBIA_BLUE),
                BorderFactory.createEmptyBorder(22, 34, 22, 34)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("Create your account");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(Theme.TEXT);
        gbc.insets = new Insets(0, 0, 16, 0);
        card.add(heading, gbc);

        JLabel fullNameLabel = Theme.label("Full Name");
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 4, 0);
        card.add(fullNameLabel, gbc);

        fullNameField = Theme.field(300, 34);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 12, 0);
        card.add(fullNameField, gbc);

        JLabel usernameLabel = Theme.label("Username");
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 4, 0);
        card.add(usernameLabel, gbc);

        usernameField = Theme.field(300, 34);
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 12, 0);
        card.add(usernameField, gbc);

        JLabel passwordLabel = Theme.label("Password");
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 4, 0);
        card.add(passwordLabel, gbc);

        passwordField = createPasswordField();
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 12, 0);
        card.add(passwordField, gbc);

        JLabel confirmLabel = Theme.label("Confirm Password");
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 4, 0);
        card.add(confirmLabel, gbc);

        confirmPasswordField = createPasswordField();
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 16, 0);
        card.add(confirmPasswordField, gbc);

        JButton registerButton = Theme.button("Register", Theme.NAVY_BLUE, Color.WHITE);
        registerButton.setPreferredSize(new Dimension(300, 38));
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(registerButton, gbc);

        JButton backButton = Theme.button("Back to Login", Theme.MEDIUM_BLUE, Theme.TEXT);
        backButton.setPreferredSize(new Dimension(300, 38));
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(backButton, gbc);

        body.add(card);
        getRootPane().setDefaultButton(registerButton);

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                registerUser();
            }
        });

        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            }
        });

        return body;
    }

    private JPasswordField createPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        field.setForeground(Theme.TEXT);
        field.setBackground(Theme.LIGHT_BLUE);
        field.setCaretColor(Theme.TEXT);
        field.setPreferredSize(new Dimension(300, 34));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.COLUMBIA_BLUE),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        return field;
    }

    private void registerUser() {
        String fullName = fullNameField.getText().trim();
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String confirmPassword = new String(confirmPasswordField.getPassword());

        if (fullName.equals("") || username.equals("") || password.equals("") || confirmPassword.equals("")) {
            JOptionPane.showMessageDialog(this, "Please fill all the fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Passwords do not match.");
            return;
        }

        try {
            if (User.usernameExists(username)) {
                JOptionPane.showMessageDialog(this, "Username already exists. Please choose another one.");
                return;
            }

            User user = new User(fullName, username, password);
            boolean added = User.add(user);

            if (added) {
                JOptionPane.showMessageDialog(this, "Registration successful. Please login.");
                LoginFrame loginFrame = new LoginFrame();
                loginFrame.setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Could not register. Please try again.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
