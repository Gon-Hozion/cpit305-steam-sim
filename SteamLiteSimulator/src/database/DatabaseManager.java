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
}