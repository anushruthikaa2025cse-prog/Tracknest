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

// The login screen of the application.
public class LoginFrame extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;

    public LoginFrame() {
        setTitle("TrackNest - Login");
        setSize(540, 480);
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
        card.setPreferredSize(new Dimension(380, 330));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.COLUMBIA_BLUE),
                BorderFactory.createEmptyBorder(24, 34, 24, 34)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("Login to your account");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(Theme.TEXT);
        gbc.insets = new Insets(0, 0, 18, 0);
        card.add(heading, gbc);

        JLabel usernameLabel = Theme.label("Username");
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(usernameLabel, gbc);

        usernameField = Theme.field(300, 34);
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 15, 0);
        card.add(usernameField, gbc);

        JLabel passwordLabel = Theme.label("Password");
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 5, 0);
        card.add(passwordLabel, gbc);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        passwordField.setForeground(Theme.TEXT);
        passwordField.setBackground(Theme.LIGHT_BLUE);
        passwordField.setCaretColor(Theme.TEXT);
        passwordField.setPreferredSize(new Dimension(300, 34));
        passwordField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.COLUMBIA_BLUE),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 16, 0);
        card.add(passwordField, gbc);

        JButton loginButton = Theme.button("Login", Theme.NAVY_BLUE, Color.WHITE);
        loginButton.setPreferredSize(new Dimension(300, 38));
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(loginButton, gbc);

        JButton registerButton = Theme.button("Create New Account", Theme.MEDIUM_BLUE, Theme.TEXT);
        registerButton.setPreferredSize(new Dimension(300, 38));
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(registerButton, gbc);

        body.add(card);
        getRootPane().setDefaultButton(loginButton);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                loginUser();
            }
        });

        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                RegisterFrame registerFrame = new RegisterFrame();
                registerFrame.setVisible(true);
                dispose();
            }
        });

        return body;
    }

    private void loginUser() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        if (username.equals("") || password.equals("")) {
            JOptionPane.showMessageDialog(this, "Please enter your username and password.");
            return;
        }

        try {
            User user = User.login(username, password);

            if (user == null) {
                JOptionPane.showMessageDialog(this,
                        "Wrong username or password. Please try again.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                DashboardFrame dashboardFrame = new DashboardFrame(user);
                dashboardFrame.setVisible(true);
                dispose();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
