package lostfound;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Represents one lost item and contains all database operations for items.
public class Item {

    private int itemId;
    private String itemName;
    private String place;
    private String lostDate;
    private String lostTime;
    private String contact;
    private int userId;
    private String reportedBy;

    // Used when a new item is reported.
    public Item(String itemName, String place, String lostDate, String lostTime, String contact, int userId) {
        this.itemName = itemName;
        this.place = place;
        this.lostDate = lostDate;
        this.lostTime = lostTime;
        this.contact = contact;
        this.userId = userId;
    }

    // Used when an item is loaded from the database.
    public Item(int itemId, String itemName, String place, String lostDate, String lostTime,
                String contact, int userId, String reportedBy) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.place = place;
        this.lostDate = lostDate;
        this.lostTime = lostTime;
        this.contact = contact;
        this.userId = userId;
        this.reportedBy = reportedBy;
    }

    public int getItemId() {
        return itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public String getPlace() {
        return place;
    }

    public String getLostDate() {
        return lostDate;
    }

    public String getLostTime() {
        return lostTime;
    }

    public String getContact() {
        return contact;
    }

    public int getUserId() {
        return userId;
    }

    public String getReportedBy() {
        return reportedBy;
    }

    // ------------------------------------------------------------------
    // Database operations
    // ------------------------------------------------------------------

    // Gets every lost item and the name of the user who reported it.
    public static ArrayList<Item> getAll() throws SQLException {
        ArrayList<Item> items = new ArrayList<Item>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Database.getConnection();
            String sql = "SELECT i.item_id, i.item_name, i.place, i.lost_date, i.lost_time, "
                    + "i.contact, i.user_id, u.full_name "
                    + "FROM items i JOIN users u ON i.user_id = u.user_id "
                    + "ORDER BY i.item_id DESC";
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                items.add(readRow(rs));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return items;
    }

    // Searches lost items by item name or place.
    public static ArrayList<Item> search(String text) throws SQLException {
        ArrayList<Item> items = new ArrayList<Item>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = Database.getConnection();
            String sql = "SELECT i.item_id, i.item_name, i.place, i.lost_date, i.lost_time, "
                    + "i.contact, i.user_id, u.full_name "
                    + "FROM items i JOIN users u ON i.user_id = u.user_id "
                    + "WHERE i.item_name LIKE ? OR i.place LIKE ? "
                    + "ORDER BY i.item_id DESC";
            ps = conn.prepareStatement(sql);
            ps.setString(1, "%" + text + "%");
            ps.setString(2, "%" + text + "%");
            rs = ps.executeQuery();

            while (rs.next()) {
                items.add(readRow(rs));
            }
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return items;
    }

    // Adds a new lost item report.
    public static boolean add(Item item) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean added = false;

        try {
            conn = Database.getConnection();
            String sql = "INSERT INTO items (item_name, place, lost_date, lost_time, contact, user_id) "
                    + "VALUES (?, ?, ?, ?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, item.getItemName());
            ps.setString(2, item.getPlace());
            ps.setString(3, item.getLostDate());
            ps.setString(4, item.getLostTime());
            ps.setString(5, item.getContact());
            ps.setInt(6, item.getUserId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                added = true;
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return added;
    }

    // Updates an item only if it belongs to the given user.
    public static boolean update(Item item) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean updated = false;

        try {
            conn = Database.getConnection();
            String sql = "UPDATE items SET item_name = ?, place = ?, lost_date = ?, "
                    + "lost_time = ?, contact = ? WHERE item_id = ? AND user_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, item.getItemName());
            ps.setString(2, item.getPlace());
            ps.setString(3, item.getLostDate());
            ps.setString(4, item.getLostTime());
            ps.setString(5, item.getContact());
            ps.setInt(6, item.getItemId());
            ps.setInt(7, item.getUserId());

            int rows = ps.executeUpdate();
            if (rows > 0) {
                updated = true;
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return updated;
    }

    // Deletes an item only if it belongs to the given user.
    public static boolean delete(int itemId, int userId) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean deleted = false;

        try {
            conn = Database.getConnection();
            String sql = "DELETE FROM items WHERE item_id = ? AND user_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);
            ps.setInt(2, userId);

            int rows = ps.executeUpdate();
            if (rows > 0) {
                deleted = true;
            }
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }

        return deleted;
    }

    // Builds one Item from the current row of a result set.
    private static Item readRow(ResultSet rs) throws SQLException {
        return new Item(
                rs.getInt("item_id"),
                rs.getString("item_name"),
                rs.getString("place"),
                rs.getString("lost_date"),
                rs.getString("lost_time"),
                rs.getString("contact"),
                rs.getInt("user_id"),
                rs.getString("full_name"));
    }
}
