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
                BufferedReader input
                = new BufferedReader(
                        new InputStreamReader(socket.getInputStream())); PrintWriter output
                = new PrintWriter(socket.getOutputStream(), true)) {

            output.println("Connected to Steam Lite Server");

            String command;

            while ((command = input.readLine()) != null) {

                System.out.println("Received command: " + command);

                if (command.startsWith("REGISTER")) {

                    String[] parts = command.split(" ");

                    if (parts.length == 3) {

                        boolean success
                                = DatabaseManager.registerUser(parts[1], parts[2]);

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

                        String role
                                = DatabaseManager.loginUser(parts[1], parts[2]);

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

                } else if (command.startsWith("DOWNLOAD")) {

                    String[] parts = command.split(" ");

                    if (parts.length == 2) {

                        try {
                            int gameId = Integer.parseInt(parts[1]);

                            String filePath = DatabaseManager.getGameFilePath(gameId);

                            if (filePath == null) {

                                output.println("DOWNLOAD_FAILED");

                            } else {

                                output.println("DOWNLOAD_READY");

                                boolean sent
                                        = FileTransferManager.sendFile(
                                                filePath,
                                                socket.getOutputStream()
                                        );

                                if (!sent) {
                                    System.out.println("File sending failed.");
                                }
                            }

                        } catch (NumberFormatException e) {

                            output.println("INVALID_GAME_ID");
                        }

                    } else {

                        output.println("INVALID_DOWNLOAD_COMMAND");
                    }
                } else if (command.startsWith("RATE_GAME")) {

                    String[] parts = command.split(" ");

                    if (parts.length == 4) {

                        try {
                            int accountId = Integer.parseInt(parts[1]);
                            int gameId = Integer.parseInt(parts[2]);
                            int rating = Integer.parseInt(parts[3]);

                            boolean success
                                    = DatabaseManager.rateGame(accountId, gameId, rating);

                            if (success) {
                                output.println("RATING_SUCCESS");
                            } else {
                                output.println("RATING_FAILED");
                            }

                        } catch (NumberFormatException e) {
                            output.println("INVALID_RATING_COMMAND");
                        }

                    } else {
                        output.println("INVALID_RATING_COMMAND");
                    }
                } else if (command.equals("VIEW_GAMES_RATINGS")) {

                    String games = DatabaseManager.getGamesWithRatingsText();

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
