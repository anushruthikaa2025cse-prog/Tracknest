package lostfound;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;

// The main screen that shows all lost items.
public class DashboardFrame extends JFrame {

    private User currentUser;
    private DefaultTableModel tableModel;
    private JTable table;
    private ArrayList<Item> itemList;
    private JTextField searchField;

    public DashboardFrame(User user) {
        currentUser = user;
        itemList = new ArrayList<Item>();

        setTitle("TrackNest - Dashboard");
        setSize(1020, 660);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Theme.BACKGROUND_WHITE);

        add(createHeader(), BorderLayout.NORTH);
        add(createBody(), BorderLayout.CENTER);

        loadAllItems();
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(Theme.NAVY_BLUE);
        header.setBorder(BorderFactory.createEmptyBorder(18, 24, 18, 24));

        JLabel title = new JLabel("TrackNest");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(Color.WHITE);

        JLabel welcome = new JLabel("Welcome, " + currentUser.getFullName());
        welcome.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        welcome.setForeground(Color.WHITE);

        header.add(title, BorderLayout.WEST);
        header.add(welcome, BorderLayout.EAST);
        return header;
    }

    private JPanel createBody() {
        JPanel body = new JPanel(new BorderLayout(0, 12));
        body.setBackground(Theme.BACKGROUND_WHITE);
        body.setBorder(BorderFactory.createEmptyBorder(18, 20, 18, 20));

        body.add(createTopPanel(), BorderLayout.NORTH);
        body.add(createTablePanel(), BorderLayout.CENTER);
        body.add(createButtonPanel(), BorderLayout.SOUTH);
        return body;
    }

    private JPanel createTopPanel() {
        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setBackground(Theme.BACKGROUND_WHITE);

        JLabel heading = new JLabel("All Lost Items");
        heading.setFont(new Font("Segoe UI", Font.BOLD, 18));
        heading.setForeground(Theme.TEXT);
        top.add(heading, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        searchPanel.setBackground(Theme.BACKGROUND_WHITE);

        JLabel searchLabel = new JLabel("Search:");
        searchLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        searchLabel.setForeground(Theme.TEXT);

        searchField = Theme.field(280, 34);

        JButton searchButton = Theme.button("Search", Theme.MEDIUM_BLUE, Theme.TEXT);
        searchButton.setPreferredSize(new Dimension(100, 34));

        JButton showAllButton = Theme.button("Show All", Theme.COLUMBIA_BLUE, Theme.TEXT);
        showAllButton.setPreferredSize(new Dimension(100, 34));

        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        top.add(searchPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchItems();
            }
        });

        showAllButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                searchField.setText("");
                loadAllItems();
            }
        });

        return top;
    }

    private JPanel createTablePanel() {
        String[] columns = {"Item Name", "Place", "Date", "Time", "Contact Number", "Reported By"};

        tableModel = new DefaultTableModel(columns, 0) {
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(28);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        table.setForeground(Theme.TEXT);
        table.setBackground(Theme.LIGHT_BLUE);
        table.setSelectionBackground(Theme.COLUMBIA_BLUE);
        table.setSelectionForeground(Theme.TEXT);
        table.setGridColor(Theme.COLUMBIA_BLUE);
        table.setShowGrid(true);
        table.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 14));
        table.getTableHeader().setBackground(Theme.COLUMBIA_BLUE);
        table.getTableHeader().setForeground(Theme.TEXT);
        table.getTableHeader().setPreferredSize(new Dimension(0, 34));
        table.getTableHeader().setOpaque(true);

        table.getColumnModel().getColumn(0).setPreferredWidth(190);
        table.getColumnModel().getColumn(1).setPreferredWidth(170);
        table.getColumnModel().getColumn(2).setPreferredWidth(110);
        table.getColumnModel().getColumn(3).setPreferredWidth(90);
        table.getColumnModel().getColumn(4).setPreferredWidth(140);
        table.getColumnModel().getColumn(5).setPreferredWidth(170);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(Theme.COLUMBIA_BLUE));
        scrollPane.getViewport().setBackground(Theme.LIGHT_BLUE);

        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Theme.BACKGROUND_WHITE);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Theme.BACKGROUND_WHITE);
        bottom.setBorder(BorderFactory.createEmptyBorder(12, 0, 0, 0));

        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        leftButtons.setBackground(Theme.BACKGROUND_WHITE);

        JButton reportButton = Theme.button("Report Lost Item", Theme.NAVY_BLUE, Color.WHITE);
        reportButton.setPreferredSize(new Dimension(160, 38));

        JButton editButton = Theme.button("Edit Item", Theme.MEDIUM_BLUE, Theme.TEXT);
        editButton.setPreferredSize(new Dimension(110, 38));

        JButton recoveredButton = Theme.button("Mark as Recovered", Theme.MEDIUM_BLUE, Theme.TEXT);
        recoveredButton.setPreferredSize(new Dimension(170, 38));

        JButton deleteButton = Theme.button("Delete Item", Theme.COLUMBIA_BLUE, Theme.TEXT);
        deleteButton.setPreferredSize(new Dimension(110, 38));

        leftButtons.add(reportButton);
        leftButtons.add(editButton);
        leftButtons.add(recoveredButton);
        leftButtons.add(deleteButton);

        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        rightButtons.setBackground(Theme.BACKGROUND_WHITE);

        JButton logoutButton = Theme.button("Logout", Theme.COLUMBIA_BLUE, Theme.TEXT);
        logoutButton.setPreferredSize(new Dimension(100, 38));
        rightButtons.add(logoutButton);

        bottom.add(leftButtons, BorderLayout.CENTER);
        bottom.add(rightButtons, BorderLayout.EAST);

        reportButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                ReportItemFrame reportFrame = new ReportItemFrame(currentUser, DashboardFrame.this);
                reportFrame.setVisible(true);
            }
        });

        editButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                editSelectedItem();
            }
        });

        recoveredButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                markAsRecovered();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                deleteSelectedItem();
            }
        });

        logoutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                logout();
            }
        });

        return bottom;
    }

    // Reloads the table after a report or an edit.
    public void refreshTable() {
        searchField.setText("");
        loadAllItems();
    }

    private void loadAllItems() {
        try {
            itemList = Item.getAll();
            fillTable();
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void searchItems() {
        String text = searchField.getText().trim();

        try {
            if (text.equals("")) {
                itemList = Item.getAll();
            } else {
                itemList = Item.search(text);
            }
            fillTable();
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void fillTable() {
        tableModel.setRowCount(0);

        for (int i = 0; i < itemList.size(); i++) {
            Item item = itemList.get(i);
            Object[] row = {
                    item.getItemName(),
                    item.getPlace(),
                    item.getLostDate(),
                    item.getLostTime(),
                    item.getContact(),
                    item.getReportedBy()
            };
            tableModel.addRow(row);
        }
    }

    private Item getSelectedItem() {
        int row = table.getSelectedRow();

        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Please select an item from the table first.");
            return null;
        }

        return itemList.get(row);
    }

    private boolean isOwner(Item item) {
        if (item.getUserId() == currentUser.getUserId()) {
            return true;
        }
        return false;
    }

    private void editSelectedItem() {
        Item item = getSelectedItem();

        if (item == null) {
            return;
        }

        if (!isOwner(item)) {
            JOptionPane.showMessageDialog(this, "You can edit only the items you reported");
            return;
        }

        EditItemFrame editFrame = new EditItemFrame(item, currentUser, this);
        editFrame.setVisible(true);
    }

    private void markAsRecovered() {
        Item item = getSelectedItem();

        if (item == null) {
            return;
        }

        if (!isOwner(item)) {
            JOptionPane.showMessageDialog(this, "You can mark only the items you reported.");
            return;
        }

        int answer = JOptionPane.showConfirmDialog(this,
                "Mark this item as recovered?\nIt will be permanently deleted from the list.",
                "Mark as Recovered",
                JOptionPane.YES_NO_OPTION);

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean deleted = Item.delete(item.getItemId(), currentUser.getUserId());

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Item marked as recovered.");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Could not mark the item as recovered.");
            }
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void deleteSelectedItem() {
        Item item = getSelectedItem();

        if (item == null) {
            return;
        }

        if (!isOwner(item)) {
            JOptionPane.showMessageDialog(this, "You can delete only the items you reported.");
            return;
        }

        int answer = JOptionPane.showConfirmDialog(this,
                "Delete this item permanently?",
                "Delete Item",
                JOptionPane.YES_NO_OPTION);

        if (answer != JOptionPane.YES_OPTION) {
            return;
        }

        try {
            boolean deleted = Item.delete(item.getItemId(), currentUser.getUserId());

            if (deleted) {
                JOptionPane.showMessageDialog(this, "Item deleted successfully.");
                refreshTable();
            } else {
                JOptionPane.showMessageDialog(this, "Could not delete the item.");
            }
        } catch (SQLException e) {
            showDatabaseError(e);
        }
    }

    private void logout() {
        int answer = JOptionPane.showConfirmDialog(this,
                "Do you want to logout?",
                "Logout",
                JOptionPane.YES_NO_OPTION);

        if (answer == JOptionPane.YES_OPTION) {
            LoginFrame loginFrame = new LoginFrame();
            loginFrame.setVisible(true);
            dispose();
        }
    }

    private void showDatabaseError(SQLException e) {
        JOptionPane.showMessageDialog(this,
                "Database error: " + e.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE);
    }
}
