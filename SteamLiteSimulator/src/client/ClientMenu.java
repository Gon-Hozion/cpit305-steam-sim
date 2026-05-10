package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ClientMenu {

    private static Scanner input = new Scanner(System.in);

    private static Socket socket;
    private static BufferedReader serverInput;
    private static PrintWriter serverOutput;
    public static String loggedInUsername = "";
    public static String loggedInRole = "";

    public static void connectToServer() {

        try {

            socket = new Socket("localhost", 5000);

            serverInput = new BufferedReader(
                new InputStreamReader(socket.getInputStream()));

            serverOutput = new PrintWriter(
                socket.getOutputStream(),
                true);

            System.out.println(serverInput.readLine());

        } catch (IOException e) {

            System.out.println("Could not connect to server.");

            System.out.println(e.getMessage());

            System.exit(0);
        }
    }

    public static void showMainMenu() {

        connectToServer();

        while (true) {

            System.out.println("\n=== STEAM LITE SIMULATOR ===");

            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. View Games");
            System.out.println("4. Download Game");
            System.out.println("5. Rate Game");
            System.out.println("6. Exit");
            if (loggedInRole.equals("admin")) {
                System.out.println("7. Admin Panel");
            }

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
                    downloadGameMenu();
                    break;

                case 5:
                    rateGameMenu();
                    break;

                case 6:
                    sendCommand("EXIT");
                    System.out.println("Goodbye!");
                    System.exit(0);
                    break;

                case 7:
                    if (loggedInRole.equals("admin")) {
                        adminMenu();
                    } else {
                        System.out.println("Admin access only.");
                    }
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

        String response = sendCommand("REGISTER " + username + " " + password);

        if (response.equals("REGISTER_SUCCESS")) {

            System.out.println("Account created successfully!");

        } else {

            System.out.println(
                "Registration failed. Username may already exist.");
        }
    }

    public static void loginMenu() {

        System.out.println("\n=== LOGIN ===");

        System.out.print("Username: ");
        String username = input.next();

        System.out.print("Password: ");
        String password = input.next();

        String response = sendCommand("LOGIN " + username + " " + password);

        if (response.startsWith("LOGIN_SUCCESS")) {
            String role = response.split(" ")[1];

            loggedInUsername = username;
            loggedInRole = response.split(" ")[1];

            System.out.println("Login successful!");

            System.out.println("Role: " + role);

        } else {

            System.out.println("Invalid username or password.");
        }
    }

    public static void viewGamesMenu() {

        System.out.println("\n=== GAME LIST ===");

        serverOutput.println("VIEW_GAMES_RATINGS");

        try {

            String line = serverInput.readLine();

            if (line.equals("GAMES_START")) {

                while (!(line = serverInput.readLine()).equals("GAMES_END")) {

                    System.out.println(line);
                }

            } else {

                System.out.println(line);
            }

        } catch (IOException e) {

            System.out.println("Failed to receive games from server.");
        }
    }

    public static void downloadGameMenu() {

        System.out.println("\n=== DOWNLOAD GAME ===");

        System.out.print("Enter game ID: ");
        int gameId = input.nextInt();

        serverOutput.println("DOWNLOAD " + gameId);

        try {
            String response = serverInput.readLine();

            if (response.equals("DOWNLOAD_READY")) {

                System.out.println("Downloading game...");

                java.io.FileOutputStream fileOutput = new java.io.FileOutputStream("downloaded_game.zip");

                byte[] buffer = new byte[4096];
                int bytesRead;
                int totalBytes = 0;
                int fileSize = 5000;

                while ((bytesRead = socket.getInputStream().read(buffer)) != -1) {

                    fileOutput.write(buffer, 0, bytesRead);
                    totalBytes += bytesRead;

                    int progress = (totalBytes * 100) / fileSize;

                    if (progress > 100) {
                        progress = 100;
                    }

                    printProgressBar(progress);

                    if (bytesRead < 4096) {
                        break;
                    }
                }

                fileOutput.close();

                System.out.println("\nDownload completed!");

            } else {
                System.out.println("Download failed: " + response);
            }

        } catch (java.io.IOException e) {
            System.out.println("Download error: " + e.getMessage());
        }
    }

    public static void printProgressBar(int progress) {

        int totalBars = 20;
        int filledBars = progress * totalBars / 100;

        StringBuilder bar = new StringBuilder("[");

        for (int i = 0; i < totalBars; i++) {

            if (i < filledBars) {
                bar.append("#");
            } else {
                bar.append("-");
            }
        }

        bar.append("] ").append(progress).append("%");

        System.out.print("\r" + bar.toString());
    }

    public static String sendCommand(String command) {

        serverOutput.println(command);

        try {

            return serverInput.readLine();

        } catch (IOException e) {

            return "ERROR";
        }
    }

    public static void rateGameMenu() {

        if (loggedInUsername.equals("")) {

            System.out.println("You must login first.");
            return;
        }

        System.out.println("\n=== RATE GAME ===");

        System.out.print("Enter game ID: ");
        int gameId = input.nextInt();

        System.out.print("Enter rating (1-5): ");
        int rating = input.nextInt();

        if (rating < 1 || rating > 5) {

            System.out.println("Rating must be between 1 and 5.");
            return;
        }

        String response
            = sendCommand("RATE_GAME "
                + loggedInUsername + " "
                + gameId + " "
                + rating);

        if (response.equals("RATING_SUCCESS")) {

            System.out.println("Rating submitted successfully!");

        } else {

            System.out.println("Rating failed.");
        }
    }

    public static void adminMenu() {

        while (true) {

            System.out.println("\n=== ADMIN PANEL ===");
            System.out.println("1. Add Game");
            System.out.println("2. Delete Game");
            System.out.println("3. View Users");
            System.out.println("4. Back");

            System.out.print("Choose option: ");
            int choice = input.nextInt();

            switch (choice) {

                case 1:
                    adminAddGame();
                    break;

                case 2:
                    adminDeleteGame();
                    break;

                case 3:
                    adminViewUsers();
                    break;

                case 4:
                    return;

                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    public static void adminAddGame() {

        input.nextLine();

        System.out.println("\n=== ADD GAME ===");

        System.out.print("Title: ");
        String title = input.nextLine();

        System.out.print("Developer: ");
        String developer = input.nextLine();

        System.out.print("Genre: ");
        String genre = input.nextLine();

        System.out.print("Price: ");
        double price = input.nextDouble();

        input.nextLine();

        System.out.print("File path: ");
        String filePath = input.nextLine();

        String command
            = "ADMIN_ADD_GAME|" + title + "|" + developer + "|" + genre + "|" + price + "|" + filePath;

        String response = sendCommand(command);

        if (response.equals("ADD_GAME_SUCCESS")) {
            System.out.println("Game added successfully!");
        } else {
            System.out.println("Failed to add game.");
        }
    }

    public static void adminDeleteGame() {

        System.out.println("\n=== DELETE GAME ===");

        System.out.print("Enter game ID: ");
        int gameId = input.nextInt();

        String response
            = sendCommand("ADMIN_DELETE_GAME " + gameId);

        if (response.equals("DELETE_GAME_SUCCESS")) {
            System.out.println("Game deleted successfully!");
        } else {
            System.out.println("Failed to delete game.");
        }
    }

    public static void adminViewUsers() {

        System.out.println("\n=== USERS ===");

        serverOutput.println("ADMIN_VIEW_USERS");

        try {
            String line = serverInput.readLine();

            if (line.equals("USERS_START")) {

                while (!(line = serverInput.readLine()).equals("USERS_END")) {
                    System.out.println(line);
                }

            } else {
                System.out.println(line);
            }

        } catch (java.io.IOException e) {
            System.out.println("Failed to receive users from server.");
        }
    }
}
