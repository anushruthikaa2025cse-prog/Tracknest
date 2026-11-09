package lostfound;

import javax.swing.JOptionPane;
import javax.swing.UIManager;

// Starts the application.
public class Main {

    public static void main(String[] args) {
        try {
            // The cross-platform look keeps the custom colours the same on every computer.
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            // If the look and feel fails, Java uses its default one.
        }

        try {
            Database.createTables();
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Could not start the application.\n" + e.getMessage(),
                    "Startup Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }
}
