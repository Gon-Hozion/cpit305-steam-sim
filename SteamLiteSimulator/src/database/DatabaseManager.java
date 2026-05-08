package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL =
            "jdbc:mysql://localhost:3306/steam_lite_db";

    private static final String USER = "root";

    private static final String PASSWORD = "root123";

    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(URL, USER, PASSWORD);

    }

    public static void testConnection() {

        try (Connection conn = getConnection()) {

            System.out.println("Database connected successfully!");

        } catch (SQLException e) {

            System.out.println("Database connection failed!");

            System.out.println(e.getMessage());

        }

    }

    public static boolean registerUser(String username, String password) {

        String sql =
                "INSERT INTO accounts(username, password, role) VALUES (?, ?, 'user')";

        try (
                Connection conn = getConnection();

                java.sql.PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            stmt.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Registration failed!");

            System.out.println(e.getMessage());

            return false;
        }
    }

    public static String loginUser(String username, String password) {

        String sql =
                "SELECT role FROM accounts WHERE username = ? AND password = ?";

        try (
                Connection conn = getConnection();

                java.sql.PreparedStatement stmt =
                        conn.prepareStatement(sql)
        ) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return rs.getString("role");
            }

        } catch (SQLException e) {

            System.out.println("Login failed!");

            System.out.println(e.getMessage());
        }

        return null;
    }

    public static void showAllGames() {

        String sql = "SELECT * FROM games";

        try (
                Connection conn = getConnection();

                java.sql.PreparedStatement stmt =
                        conn.prepareStatement(sql);

                java.sql.ResultSet rs = stmt.executeQuery()
        ) {

            while (rs.next()) {

                System.out.println(
                        rs.getInt("id") + " - " +
                        rs.getString("title") + " | " +
                        rs.getString("genre") + " | $" +
                        rs.getDouble("price")
                );
            }

        } catch (SQLException e) {

            System.out.println("Failed to load games!");

            System.out.println(e.getMessage());
        }
    }
    
    public static String getAllGamesText() {

    String sql = "SELECT * FROM games";

    StringBuilder result = new StringBuilder();

    try (
            Connection conn = getConnection();

            java.sql.PreparedStatement stmt =
                    conn.prepareStatement(sql);

            java.sql.ResultSet rs = stmt.executeQuery()
    ) {

        while (rs.next()) {

            result.append(rs.getInt("id"))
                    .append(" - ")
                    .append(rs.getString("title"))
                    .append(" | ")
                    .append(rs.getString("genre"))
                    .append(" | $")
                    .append(rs.getDouble("price"))
                    .append("\n");
        }

    } catch (SQLException e) {

        return "Failed to load games: " + e.getMessage();
    }

    if (result.length() == 0) {
        return "No games available.";
    }

    return result.toString();
}
    
   public static String getGameFilePath(int gameId) {

    String sql = "SELECT file_path FROM games WHERE id = ?";

    try (
            Connection conn = getConnection();

            java.sql.PreparedStatement stmt =
                    conn.prepareStatement(sql)
    ) {

        stmt.setInt(1, gameId);

        java.sql.ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return rs.getString("file_path");
        }

    } catch (SQLException e) {
        System.out.println("Failed to get game file path: " + e.getMessage());
    }

    return null;
} 
}