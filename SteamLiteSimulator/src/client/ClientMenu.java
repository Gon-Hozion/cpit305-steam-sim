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

    public static void connectToServer() {

        try {
            socket = new Socket("localhost", 5000);

            serverInput = new BufferedReader(
                    new InputStreamReader(socket.getInputStream()));

            serverOutput = new PrintWriter(socket.getOutputStream(), true);

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
            System.out.println("Registration failed. Username may already exist.");
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

            System.out.println("Login successful!");
            System.out.println("Role: " + role);

        } else {
            System.out.println("Invalid username or password.");
        }
    }

    public static void viewGamesMenu() {

        System.out.println("\n=== GAME LIST ===");

        serverOutput.println("VIEW_GAMES");

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

    public static String sendCommand(String command) {

        serverOutput.println(command);

        try {
            return serverInput.readLine();
        } catch (IOException e) {
            return "ERROR";
        }
    }
}