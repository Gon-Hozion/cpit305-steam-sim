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
            System.out.println("5. Exit");

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
}
