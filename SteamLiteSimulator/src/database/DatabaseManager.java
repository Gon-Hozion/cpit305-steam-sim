package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseManager {

    private static final String URL
            = "jdbc:mysql://localhost:3306/steam_lite_db";

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

        String sql
                = "INSERT INTO accounts(username, password, role) VALUES (?, ?, 'user')";

        try (
                Connection conn = getConnection(); java.sql.PreparedStatement stmt
                = conn.prepareStatement(sql)) {

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

        String sql
                = "SELECT role FROM accounts WHERE username = ? AND password = ?";

        try (
                Connection conn = getConnection(); java.sql.PreparedStatement stmt
                = conn.prepareStatement(sql)) {

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
                Connection conn = getConnection(); java.sql.PreparedStatement stmt
                = conn.prepareStatement(sql); java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                System.out.println(
                        rs.getInt("id") + " - "
                        + rs.getString("title") + " | "
                        + rs.getString("genre") + " | $"
                        + rs.getDouble("price")
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
                Connection conn = getConnection(); java.sql.PreparedStatement stmt
                = conn.prepareStatement(sql); java.sql.ResultSet rs = stmt.executeQuery()) {

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
                Connection conn = getConnection(); java.sql.PreparedStatement stmt
                = conn.prepareStatement(sql)) {

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

    public static boolean rateGame(int accountId, int gameId, int rating) {

        String sql
                = "INSERT INTO ratings(account_id, game_id, rating) VALUES (?, ?, ?)";

        try (
                Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.setInt(2, gameId);
            stmt.setInt(3, rating);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Failed to rate game!");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static String getGamesWithRatingsText() {

        String sql
                = "SELECT g.id, g.title, g.genre, g.price, AVG(r.rating) AS avg_rating "
                + "FROM games g "
                + "LEFT JOIN ratings r ON g.id = r.game_id "
                + "GROUP BY g.id, g.title, g.genre, g.price";

        StringBuilder result = new StringBuilder();

        try (
                Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql); java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                double avgRating = rs.getDouble("avg_rating");

                result.append(rs.getInt("id"))
                        .append(" - ")
                        .append(rs.getString("title"))
                        .append(" | ")
                        .append(rs.getString("genre"))
                        .append(" | $")
                        .append(rs.getDouble("price"))
                        .append(" | Rating: ");

                if (rs.wasNull()) {
                    result.append("No ratings yet");
                } else {
                    result.append(String.format("%.1f/5", avgRating));
                }

                result.append("\n");
            }

        } catch (SQLException e) {
            return "Failed to load games with ratings: " + e.getMessage();
        }

        return result.toString();
    }

    public static boolean addGame(String title, String developer, String genre, double price, String filePath) {

        String sql
                = "INSERT INTO games(title, developer, genre, price, file_path) VALUES (?, ?, ?, ?, ?)";

        try (
                Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, title);
            stmt.setString(2, developer);
            stmt.setString(3, genre);
            stmt.setDouble(4, price);
            stmt.setString(5, filePath);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Failed to add game!");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean deleteGame(int gameId) {

        String sql = "DELETE FROM games WHERE id = ?";

        try (
                Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, gameId);

            int rows = stmt.executeUpdate();

            return rows > 0;

        } catch (SQLException e) {
            System.out.println("Failed to delete game!");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static String getAllUsersText() {

        String sql = "SELECT id, username, role FROM accounts";

        StringBuilder result = new StringBuilder();

        try (
                Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql); java.sql.ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                result.append(rs.getInt("id"))
                        .append(" - ")
                        .append(rs.getString("username"))
                        .append(" | Role: ")
                        .append(rs.getString("role"))
                        .append("\n");
            }

        } catch (SQLException e) {
            return "Failed to load users: " + e.getMessage();
        }

        if (result.length() == 0) {
            return "No users found.";
        }

        return result.toString();
    }

    public static int getAccountIdByUsername(String username) {

        String sql = "SELECT id FROM accounts WHERE username = ?";

        try (
                Connection conn = getConnection(); java.sql.PreparedStatement stmt
                = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            java.sql.ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return rs.getInt("id");
            }

        } catch (SQLException e) {

            System.out.println(e.getMessage());
        }

        return -1;
    }

    public static boolean recordDownload(int accountId, int gameId) {

        String sql
                = "INSERT INTO downloads(account_id, game_id, download_count) VALUES (?, ?, 1)";

        try (
                Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, accountId);
            stmt.setInt(2, gameId);

            stmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            System.out.println("Failed to record download!");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static String getUserDownloadsText(String username) {

        String sql
                = "SELECT g.title, d.download_count, d.last_downloaded "
                + "FROM downloads d "
                + "JOIN accounts a ON d.account_id = a.id "
                + "JOIN games g ON d.game_id = g.id "
                + "WHERE a.username = ?";

        StringBuilder result = new StringBuilder();

        try (
                Connection conn = getConnection(); java.sql.PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            java.sql.ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                result.append(rs.getString("title"))
                        .append(" | Downloads: ")
                        .append(rs.getInt("download_count"))
                        .append(" | Last: ")
                        .append(rs.getTimestamp("last_downloaded"))
                        .append("\n");
            }

        } catch (SQLException e) {
            return "Failed to load downloads: " + e.getMessage();
        }

        if (result.length() == 0) {
            return "No downloads yet.";
        }

        return result.toString();
    }
}
