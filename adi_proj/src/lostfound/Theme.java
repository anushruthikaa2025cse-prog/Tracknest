package lostfound;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

// All shared colours and small UI helpers used by the screens.
public final class Theme {

    public static final Color BACKGROUND_WHITE = new Color(0xFFFFFF);
    public static final Color LIGHT_BLUE = new Color(0xEAF4FA);
    public static final Color COLUMBIA_BLUE = new Color(0xB9D9EB);
    public static final Color MEDIUM_BLUE = new Color(0x7FA8C4);
    public static final Color NAVY_BLUE = new Color(0x0E2A47);
    public static final Color TEXT = new Color(0x16283D);

    private static final Font LABEL_FONT = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font BUTTON_FONT = new Font("Segoe UI", Font.BOLD, 14);

    private Theme() {
    }

    // The blue title bar shared by the login, register, report and edit screens.
    public static JPanel header() {
        JPanel header = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 22));
        header.setPreferredSize(new Dimension(0, 84));
        header.setBackground(NAVY_BLUE);

        JLabel title = new JLabel("TrackNest");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);
        header.add(title);

        return header;
    }

    public static JButton button(String text, Color background, Color foreground) {
        JButton button = new JButton(text);
        button.setFont(BUTTON_FONT);
        button.setBackground(background);
        button.setForeground(foreground);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setOpaque(true);
        return button;
    }

    public static JLabel label(String text) {
        JLabel label = new JLabel(text);
        label.setFont(LABEL_FONT);
        label.setForeground(TEXT);
        return label;
    }

    public static JTextField field(int width, int height) {
        JTextField field = new JTextField();
        field.setFont(LABEL_FONT);
        field.setForeground(TEXT);
        field.setBackground(LIGHT_BLUE);
        field.setCaretColor(TEXT);
        field.setPreferredSize(new Dimension(width, height));
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(COLUMBIA_BLUE),
                BorderFactory.createEmptyBorder(5, 8, 5, 8)));
        return field;
    }
}
