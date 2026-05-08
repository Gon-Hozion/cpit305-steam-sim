package server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) {

        try {
            ServerSocket serverSocket = new ServerSocket(5000);

            System.out.println("Server started on port 5000...");
            System.out.println("Waiting for clients...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("New client connected: " + socket.getInetAddress());
            }

        } catch (IOException e) {
            System.out.println("Server error: " + e.getMessage());
        }
    }
}