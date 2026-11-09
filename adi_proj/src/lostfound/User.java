package lostfound;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// Represents one user and contains all database operations for users.
public class User {

    private int userId;
    private String fullName;
    private String username;
    private String password;

    // Used when a new user registers.
    public User(String fullName, String username, String password) {
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    // Used when a user is loaded from the database.
    public User(int userId, String fullName, String username, String password) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.password = password;
    }

    public int getUserId() {
        return userId;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    // ------------------------------------------------------------------
    // Database operations
    // ------------------------------------------------------------------

    // Checks if a username is already taken.
    public static boolean usernameExists(String username) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        boolean exists = false;

        try {
            conn = Database.getConnection();
            String sql = "SELECT user_id FROM users WHERE username = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            rs = ps.executeQuery();

            if (rs.next()) {
                exists = true;
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

        return exists;
    }

    // Saves a new user in the database.
    public static boolean add(User user) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        boolean added = false;

        try {
            conn = Database.getConnection();
            String sql = "INSERT INTO users (full_name, username, password) VALUES (?, ?, ?)";
            ps = conn.prepareStatement(sql);
            ps.setString(1, user.getFullName());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());

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

    // Checks the username and password and returns the user if they match.
    public static User login(String username, String password) throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        User user = null;

        try {
            conn = Database.getConnection();
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ps.setString(2, password);
            rs = ps.executeQuery();

            if (rs.next()) {
                user = new User(
                        rs.getInt("user_id"),
                        rs.getString("full_name"),
                        rs.getString("username"),
                        rs.getString("password"));
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

        return user;
    }
}
