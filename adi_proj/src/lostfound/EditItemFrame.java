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
import javax.swing.JTextField;

// The window used to edit an item that the user reported.
public class EditItemFrame extends JFrame {

    private Item item;
    private User currentUser;
    private DashboardFrame dashboardFrame;

    private JTextField itemNameField;
    private JTextField placeField;
    private JTextField dateField;
    private JTextField timeField;
    private JTextField contactField;

    public EditItemFrame(Item selectedItem, User user, DashboardFrame dashboard) {
        item = selectedItem;
        currentUser = user;
        dashboardFrame = dashboard;

        setTitle("TrackNest - Edit Item");
        setSize(560, 660);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
        card.setPreferredSize(new Dimension(410, 510));
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Theme.COLUMBIA_BLUE),
                BorderFactory.createEmptyBorder(18, 34, 18, 34)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("Edit Lost Item");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(Theme.TEXT);
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(heading, gbc);

        JLabel itemNameLabel = Theme.label("Item Name");
        gbc.gridy = 1;
        gbc.insets = new Insets(0, 0, 4, 0);
        card.add(itemNameLabel, gbc);

        itemNameField = Theme.field(340, 32);
        itemNameField.setText(item.getItemName());
        gbc.gridy = 2;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(itemNameField, gbc);

        JLabel placeLabel = Theme.label("Place");
        gbc.gridy = 3;
        gbc.insets = new Insets(0, 0, 4, 0);
        card.add(placeLabel, gbc);

        placeField = Theme.field(340, 32);
        placeField.setText(item.getPlace());
        gbc.gridy = 4;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(placeField, gbc);

        JLabel dateLabel = Theme.label("Date (dd-mm-yyyy)");
        gbc.gridy = 5;
        gbc.insets = new Insets(0, 0, 4, 0);
        card.add(dateLabel, gbc);

        dateField = Theme.field(340, 32);
        dateField.setText(item.getLostDate());
        gbc.gridy = 6;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(dateField, gbc);

        JLabel timeLabel = Theme.label("Time (hh:mm)");
        gbc.gridy = 7;
        gbc.insets = new Insets(0, 0, 4, 0);
        card.add(timeLabel, gbc);

        timeField = Theme.field(340, 32);
        timeField.setText(item.getLostTime());
        gbc.gridy = 8;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(timeField, gbc);

        JLabel contactLabel = Theme.label("Contact Number (10 digits)");
        gbc.gridy = 9;
        gbc.insets = new Insets(0, 0, 4, 0);
        card.add(contactLabel, gbc);

        contactField = Theme.field(340, 32);
        contactField.setText(item.getContact());
        gbc.gridy = 10;
        gbc.insets = new Insets(0, 0, 8, 0);
        card.add(contactField, gbc);

        JLabel reportedByLabel = Theme.label("Reported by: " + item.getReportedBy());
        reportedByLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        gbc.gridy = 11;
        gbc.insets = new Insets(0, 0, 10, 0);
        card.add(reportedByLabel, gbc);

        JButton saveButton = Theme.button("Save Changes", Theme.NAVY_BLUE, Color.WHITE);
        saveButton.setPreferredSize(new Dimension(340, 38));
        gbc.gridy = 12;
        gbc.insets = new Insets(0, 0, 6, 0);
        card.add(saveButton, gbc);

        JButton cancelButton = Theme.button("Cancel", Theme.MEDIUM_BLUE, Theme.TEXT);
        cancelButton.setPreferredSize(new Dimension(340, 38));
        gbc.gridy = 13;
        gbc.insets = new Insets(0, 0, 0, 0);
        card.add(cancelButton, gbc);

        body.add(card);
        getRootPane().setDefaultButton(saveButton);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                updateItem();
            }
        });

        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        return body;
    }

    private void updateItem() {
        if (item.getUserId() != currentUser.getUserId()) {
            JOptionPane.showMessageDialog(this, "You can edit only the items you reported");
            return;
        }

        String itemName = itemNameField.getText().trim();
        String place = placeField.getText().trim();
        String date = dateField.getText().trim();
        String time = timeField.getText().trim();
        String contact = contactField.getText().trim();

        if (itemName.equals("") || place.equals("") || date.equals("")
                || time.equals("") || contact.equals("")) {
            JOptionPane.showMessageDialog(this, "Please fill all the fields.");
            return;
        }

        if (!isValidDate(date)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid date in dd-mm-yyyy format.");
            return;
        }

        if (!isValidTime(time)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid time in hh:mm format.");
            return;
        }

        if (!isValidContact(contact)) {
            JOptionPane.showMessageDialog(this, "Please enter a valid 10 digit contact number.");
            return;
        }

        try {
            Item updatedItem = new Item(
                    item.getItemId(),
                    itemName,
                    place,
                    date,
                    time,
                    contact,
                    currentUser.getUserId(),
                    currentUser.getFullName());

            boolean updated = Item.update(updatedItem);

            if (updated) {
                JOptionPane.showMessageDialog(this, "Item updated successfully.");
                dashboardFrame.refreshTable();
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Could not update the item.");
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this,
                    "Database error: " + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    private boolean isValidDate(String date) {
        if (date.length() != 10) {
            return false;
        }

        if (date.charAt(2) != '-' || date.charAt(5) != '-') {
            return false;
        }

        for (int i = 0; i < date.length(); i++) {
            if (i == 2 || i == 5) {
                continue;
            }
            if (!Character.isDigit(date.charAt(i))) {
                return false;
            }
        }

        int day = Integer.parseInt(date.substring(0, 2));
        int month = Integer.parseInt(date.substring(3, 5));
        int year = Integer.parseInt(date.substring(6, 10));

        if (month < 1 || month > 12) {
            return false;
        }

        int[] monthDays = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int maxDay = monthDays[month - 1];

        if (month == 2) {
            if ((year % 400 == 0) || (year % 4 == 0 && year % 100 != 0)) {
                maxDay = 29;
            }
        }

        if (day < 1 || day > maxDay) {
            return false;
        }

        return true;
    }

    private boolean isValidTime(String time) {
        if (time.length() != 5) {
            return false;
        }

        if (time.charAt(2) != ':') {
            return false;
        }

        for (int i = 0; i < time.length(); i++) {
            if (i == 2) {
                continue;
            }
            if (!Character.isDigit(time.charAt(i))) {
                return false;
            }
        }

        int hour = Integer.parseInt(time.substring(0, 2));
        int minute = Integer.parseInt(time.substring(3, 5));

        if (hour < 0 || hour > 23) {
            return false;
        }

        if (minute < 0 || minute > 59) {
            return false;
        }

        return true;
    }

    private boolean isValidContact(String contact) {
        if (contact.length() != 10) {
            return false;
        }

        for (int i = 0; i < contact.length(); i++) {
            if (!Character.isDigit(contact.charAt(i))) {
                return false;
            }
        }

        return true;
    }
}
