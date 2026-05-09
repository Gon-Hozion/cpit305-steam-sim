package client;

import database.DatabaseManager;

public class TestAdminMethods {

    public static void main(String[] args) {

        boolean added =
                DatabaseManager.addGame(
                        "Test Game",
                        "Test Dev",
                        "Action",
                        5.99,
                        "resources/games/test_game.zip"
                );

        if (added) {
            System.out.println("Game added!");
        }

        System.out.println("\n=== USERS ===");
        System.out.println(DatabaseManager.getAllUsersText());
    }
}