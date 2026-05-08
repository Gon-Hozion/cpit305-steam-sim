package client;

import database.DatabaseManager;
import java.util.Scanner;

public class ClientMenu {

    private static Scanner input = new Scanner(System.in);

    public static void showMainMenu() {

        while (true) {

            System.out.println("\n=== STEAM LITE SIMULATOR ===");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Games");
            System.out.println("4. Exit");

            System.out.print("Choose option: ");
            int choice = input.nextInt();

            switch (choice) {

                case 1:
                    registerMenu();
                    break;

                case 2:
                    loginMenu();
                    break;

                case 3:
                    viewGamesMenu();
                    break;

                case 4:
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    public static void registerMenu() {

        System.out.println("\n=== REGISTER ===");

        System.out.print("Username: ");
        String username = input.next();

        System.out.print("Password: ");
        String password = input.next();

        boolean success = DatabaseManager.registerUser(username, password);

        if (success) {
            System.out.println("Account created successfully!");
        } else {
            System.out.println("Registration failed. Username may already exist.");
        }
    }

    public static void loginMenu() {

        System.out.println("\n=== LOGIN ===");

        System.out.print("Username: ");
        String username = input.next();

        System.out.print("Password: ");
        String password = input.next();

        String role = DatabaseManager.loginUser(username, password);

        if (role != null) {
            System.out.println("Login successful!");
            System.out.println("Role: " + role);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public static void viewGamesMenu() {

        System.out.println("\n=== GAME LIST ===");

        DatabaseManager.showAllGames();
    }
}