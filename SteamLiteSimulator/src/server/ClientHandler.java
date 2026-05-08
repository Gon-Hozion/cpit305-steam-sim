package server;

import java.io.IOException;
import java.net.Socket;

public class ClientHandler extends Thread {

    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            System.out.println("Client is being handled by: " + Thread.currentThread().getName());

            // For now, just keep connection briefly
            socket.close();

        } catch (IOException e) {
            System.out.println("Client handler error: " + e.getMessage());
        }
    }
}