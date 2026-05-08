package server;

import database.DatabaseManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {

        try (
                BufferedReader input =
                        new BufferedReader(
                                new InputStreamReader(socket.getInputStream()));

                PrintWriter output =
                        new PrintWriter(socket.getOutputStream(), true)
        ) {

            output.println("Connected to Steam Lite Server");

            String command;

            while ((command = input.readLine()) != null) {

                System.out.println("Received command: " + command);

                if (command.startsWith("REGISTER")) {

                    String[] parts = command.split(" ");

                    if (parts.length == 3) {

                        boolean success =
                                DatabaseManager.registerUser(parts[1], parts[2]);

                        if (success) {
                            output.println("REGISTER_SUCCESS");
                        } else {
                            output.println("REGISTER_FAILED");
                        }

                    } else {
                        output.println("INVALID_REGISTER_COMMAND");
                    }

                } else if (command.startsWith("LOGIN")) {

                    String[] parts = command.split(" ");

                    if (parts.length == 3) {

                        String role =
                                DatabaseManager.loginUser(parts[1], parts[2]);

                        if (role != null) {
                            output.println("LOGIN_SUCCESS " + role);
                        } else {
                            output.println("LOGIN_FAILED");
                        }

                    } else {
                        output.println("INVALID_LOGIN_COMMAND");
                    }

                } else if (command.equals("VIEW_GAMES")) {

                    String games = DatabaseManager.getAllGamesText();

                    output.println("GAMES_START");

                    String[] lines = games.split("\n");

                    for (String line : lines) {
                        output.println(line);
                    }

                    output.println("GAMES_END");

                } else if (command.equals("EXIT")) {

                    output.println("Goodbye!");
                    break;

                } else {

                    output.println("UNKNOWN_COMMAND");
                }
            }

        } catch (IOException e) {

            System.out.println("Client handler error: " + e.getMessage());

        } finally {

            try {
                socket.close();
            } catch (IOException e) {
                System.out.println("Socket close error: " + e.getMessage());
            }
        }
    }
}