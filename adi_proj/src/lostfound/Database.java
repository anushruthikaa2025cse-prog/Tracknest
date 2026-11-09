package lostfound;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// This class opens the SQLite database and creates the tables.
public class Database {

    private static final String URL = "jdbc:sqlite:lostfound.db";

    // Returns a connection to the database file.
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }

    // Creates the users and items tables if they do not exist yet.
    public static void createTables() throws SQLException {
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = getConnection();

            String usersSql = "CREATE TABLE IF NOT EXISTS users ("
                    + "user_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "full_name TEXT NOT NULL, "
                    + "username TEXT NOT NULL UNIQUE, "
                    + "password TEXT NOT NULL)";
            ps = conn.prepareStatement(usersSql);
            ps.execute();
            ps.close();
            ps = null;

            String itemsSql = "CREATE TABLE IF NOT EXISTS items ("
                    + "item_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "item_name TEXT NOT NULL, "
                    + "place TEXT NOT NULL, "
                    + "lost_date TEXT NOT NULL, "
                    + "lost_time TEXT NOT NULL, "
                    + "contact TEXT NOT NULL, "
                    + "user_id INTEGER NOT NULL, "
                    + "FOREIGN KEY (user_id) REFERENCES users(user_id))";
            ps = conn.prepareStatement(itemsSql);
            ps.execute();
            ps.close();
            ps = null;
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
    }
}
