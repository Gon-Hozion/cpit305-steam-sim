package client;

import database.DatabaseManager;

public class TestDatabaseMethods {
    public static void main(String[] args) {
        DatabaseManager.registerUser("testuser", "1234");

        String role = DatabaseManager.loginUser("testuser", "1234");

        if (role != null) {
            System.out.println("Login successful. Role: " + role);
        } else {
            System.out.println("Login failed.");
        }

        DatabaseManager.showAllGames();
    }
}